/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.common.web.controller;

import org.jtalks.common.model.entity.User;
import org.jtalks.common.service.SecurityService;
import org.jtalks.common.service.UserService;
import org.jtalks.common.service.exceptions.DuplicateEmailException;
import org.jtalks.common.service.exceptions.DuplicateUserException;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.common.service.exceptions.WrongPasswordException;
import org.jtalks.common.web.dto.user.EditUserProfileDto;
import org.jtalks.common.web.dto.user.RegisterUserDto;
import org.jtalks.common.web.dto.user.UserViewDto;
import org.jtalks.common.web.validation.ImageFormats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Controller for User related actions: registration.
 *
 * @author Kirill Afonin
 * @author Alexandre Teterin
 * @author Max Malakhov
 * @author Eugeny Batov
 * @author Dmitriy Butakov
 * @author Dmitry Sokolov
 */
@Controller
public class UserController {
    public static final String EDIT_PROFILE = "editProfile";
    public static final String REGISTRATION = "registration";
    public static final String EDITED_USER = "editedUser";

    private final SecurityService securityService;
    private final UserService userService;

    /**
     * This method turns the trim binder on. Trim bilder
     * removes leading and trailing spaces from the submitted fields.
     *
     * @param binder Binder object to be injected
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /**
     * Assign {@link UserService} to field.
     *
     * @param userService     {@link UserService} to be injected
     * @param securityService {@link SecurityService} used for accessing to current logged in user
     */
    @Autowired
    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    /**
     * Render registration page with binded object to form.
     *
     * @return {@code ModelAndView} with "registration" view and empty
     *         {@link RegisterUserDto} with name "newUser
     */
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registrationPage() {
        RegisterUserDto newUser = new RegisterUserDto();
        return new ModelAndView(REGISTRATION).addObject("newUser", newUser);
    }

    /**
     * Register {@link User} from populated in form {@link RegisterUserDto}.
     *
     * @param userDto {@link RegisterUserDto} populated in form
     * @param result  result of {@link RegisterUserDto} validation
     * @return redirect to / if registration successful or back to "/registration" if failed
     */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registerUser(@Valid @ModelAttribute("newUser") RegisterUserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView(REGISTRATION);
        }

        try {
            userService.registerUser(userDto.createUser());
            return new ModelAndView("redirect:/");
        } catch (DuplicateUserException e) {
            result.rejectValue("username", "validation.duplicateuser");
        } catch (DuplicateEmailException e) {
            result.rejectValue("email", "validation.duplicateemail");
        }
        return new ModelAndView(REGISTRATION);
    }

    /**
     * Show page with user info.
     *
     * @param encodedUsername {@link User#getEncodedUsername()}
     * @return user details view with {@link User} object
     * @throws NotFoundException if user with given id not found
     */
    @RequestMapping(value = "/user/{encodedUsername}", method = RequestMethod.GET)
    public ModelAndView show(@PathVariable("encodedUsername") String encodedUsername) throws NotFoundException {
        UserViewDto userViewDto = new UserViewDto(userService.getByEncodedUsername(encodedUsername));

        return new ModelAndView("userDetails").addObject("user", userViewDto);
    }

    /**
     * Show edit user profile page for current logged in user.
     *
     * @return edit user profile page
     * @throws NotFoundException throws if current logged in user was not found
     */
    @RequestMapping(value = "/user/edit", method = RequestMethod.GET)
    public ModelAndView editProfilePage() throws NotFoundException {
        User user = securityService.getCurrentUser();
        EditUserProfileDto editedUser = new EditUserProfileDto(user);
        editedUser.setAvatar(new MockMultipartFile("avatar", "", ImageFormats.JPG.getContentType(), user.getAvatar()));
        return new ModelAndView(EDIT_PROFILE).addObject(EDITED_USER, editedUser);
    }

    /**
     * Update user profile info. Check if the user enter valid data and update profile in database.
     * In error case return into the edit profile page and draw the error.
     *
     * @param userDto dto populated by user
     * @param result  binding result which contains the validation result
     * @return in case of errors return back to edit profile page, in another case return to user detalis page
     * @throws NotFoundException   - throws if current logged in user was not found
     * @throws java.io.IOException - throws in case of access errors (if the temporary store fails)
     */
    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public ModelAndView editProfile(@Valid @ModelAttribute(EDITED_USER) EditUserProfileDto userDto,
                                    BindingResult result) throws NotFoundException, IOException {

        User user = securityService.getCurrentUser();

        if (result.hasErrors()) {
            //we should show current user avatar (if any)
            //if no file was uploaded, or if there were validation errors on avatar field
            if ((userDto.getAvatar().getSize() == 0) || (result.hasFieldErrors("avatar"))) {
                userDto.setAvatar(new MockMultipartFile("avatar", "", ImageFormats.JPG.getContentType(),
                                                        user.getAvatar()));
            }
            return new ModelAndView(EDIT_PROFILE, EDITED_USER, userDto);
        }

        User editedUser;
        try {
            editedUser = userService.editUserProfile(userDto.getEmail(), userDto.getFirstName(), userDto.getLastName(),
                                                     userDto.getCurrentUserPassword(), userDto.getNewUserPassword(),
                                                     userDto.getAvatar().getBytes());
        } catch (DuplicateEmailException e) {
            result.rejectValue("email", "validation.duplicateemail");
            return new ModelAndView(EDIT_PROFILE);
        } catch (WrongPasswordException e) {
            result.rejectValue("currentUserPassword", "label.incorrectCurrentPassword",
                               "Password does not match to the current password");
            return new ModelAndView(EDIT_PROFILE);
        }
        return new ModelAndView(new StringBuilder().append("redirect:/user/").append(editedUser.getEncodedUsername())
                                                   .append(".html").toString());
    }


    /**
     * Remove avatar from user profile.
     *
     * @return edit user profile page
     */
    @RequestMapping(value = "/user/remove/avatar", method = RequestMethod.POST)
    public ModelAndView removeAvatarFromCurrentUser() {
        User user = securityService.getCurrentUser();
        userService.removeAvatarFromCurrentUser();
        EditUserProfileDto editedUser = new EditUserProfileDto(user);
        return new ModelAndView(EDIT_PROFILE, EDITED_USER, editedUser);
    }

    /**
     * Write user avatar  in response for rendering it on html pages.
     *
     * @param response        servlet response
     * @param encodedUsername {@link User#getEncodedUsername()}
     * @throws NotFoundException   - throws if user with given encodedUsername not found
     * @throws java.io.IOException - throws if an output exception occurred
     */
    @RequestMapping(value = "/show/{encodedUsername}/avatar", method = RequestMethod.GET)
    public void renderAvatar(HttpServletResponse response, @PathVariable("encodedUsername") String encodedUsername)
        throws NotFoundException, IOException {
        User user = userService.getByEncodedUsername(encodedUsername);
        response.setContentType("image/jpeg");
        response.setContentLength(user.getAvatar().length);
        response.getOutputStream().write(user.getAvatar());
    }

}
