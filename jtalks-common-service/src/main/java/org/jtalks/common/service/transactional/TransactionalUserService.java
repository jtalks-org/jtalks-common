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
package org.jtalks.common.service.transactional;

import org.jtalks.common.model.dao.UserDao;
import org.jtalks.common.model.entity.User;
import org.jtalks.common.service.SecurityService;
import org.jtalks.common.service.UserService;
import org.jtalks.common.service.exceptions.DuplicateEmailException;
import org.jtalks.common.service.exceptions.DuplicateUserException;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.common.service.exceptions.WrongPasswordException;
import org.jtalks.common.service.security.SecurityConstants;
import org.jtalks.common.util.SaltGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

import java.io.*;

/**
 * User service class. This class contains method needed to manipulate with User persistent entity.
 *
 * @author Osadchuck Eugeny
 * @author Kirill Afonin
 * @author Alexandre Teterin
 * @author Masich Ivan
 * @author Dmitry Sokolov
 */
public class TransactionalUserService extends AbstractTransactionalEntityService<User, UserDao> implements UserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private SecurityService securityService;
    private MessageDigestPasswordEncoder passwordEncoder;
    private SaltGenerator saltGenerator;

    /**
     * Create an instance of User entity based service
     *
     * @param dao             for operations with data storage
     * @param securityService for security
     * @param passwordEncoder for encode password
     * @param saltGenerator   for generate new salt
     */
    public TransactionalUserService(
        UserDao dao,
        SecurityService securityService,
        MessageDigestPasswordEncoder passwordEncoder,
        SaltGenerator saltGenerator
    ) {
        this.dao = dao;
        this.securityService = securityService;
        this.passwordEncoder = passwordEncoder;
        this.saltGenerator = saltGenerator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getByUsername(String username) throws NotFoundException {
        User user = dao.getByUsername(username);
        if (user == null) {
            String msg = "User " + username + " not found.";
            logger.info(msg);
            throw new NotFoundException(msg);
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getByEncodedUsername(String encodedUsername) throws NotFoundException {
        User user = dao.getByEncodedUsername(encodedUsername);
        if (user == null) {
            String msg = "User " + encodedUsername + " not found.";
            logger.info(msg);
            throw new NotFoundException(msg);
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User registerUser(User user) throws DuplicateUserException, DuplicateEmailException {
        if (isUserExist(user)) {
            String msg = "User " + user.getUsername() + " already exists!";
            logger.warn(msg);
            throw new DuplicateUserException(msg);
        }
        if (isEmailExist(user.getEmail())) {
            String msg = "E-mail " + user.getEmail() + " already exists!";
            logger.warn(msg);
            throw new DuplicateEmailException(msg);
        }

        String salt = saltGenerator.generate();
        user.setSalt(salt);

        String encodedPassword = passwordEncoder.encodePassword(user.getPassword(), salt);
        user.setPassword(encodedPassword);

        dao.saveOrUpdate(user);

        logger.info("User registered: {}", user.getUsername());
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLastLoginTime(User user) {
        user.updateLastLoginTime();
        dao.saveOrUpdate(user);
    }

    /**
     * Check user for existance.
     *
     * @param user user for check existance
     * @return {@code true} if user with given username exist
     */
    private boolean isUserExist(User user) {
        return user.getUsername().equals(SecurityConstants.ANONYMOUS_USERNAME) || dao.isUserWithUsernameExist(
            user.getUsername());
    }

    /**
     * Check email for existance.
     *
     * @param email email for check existance
     * @return {@code true} if email exist
     */
    private boolean isEmailExist(String email) {
        return dao.isUserWithEmailExist(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User editUserProfile(String email, String firstName, String lastName, String currentPassword,
                                String newPassword, byte[] avatar)
        throws DuplicateEmailException, WrongPasswordException {

        User currentUser = securityService.getCurrentUser();
        boolean changePassword = newPassword != null;
        if (changePassword) {
            String currentEncodedPassword = passwordEncoder.encodePassword(currentPassword, currentUser.getSalt());
            
            if (currentPassword == null || !currentUser.getPassword().equals(currentEncodedPassword)) {
                throw new WrongPasswordException();
            } else {
                currentUser.setPassword(newPassword);
            }
        }

        boolean changeEmail = !currentUser.getEmail().equals(email);
        if (changeEmail && isEmailExist(email)) {
            throw new DuplicateEmailException();
        }

        currentUser.setEmail(email);
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        if (avatar.length > 0) {
            currentUser.setAvatar(avatar);
        }

        String salt = saltGenerator.generate();
        currentUser.setSalt(salt);

        String encodedPassword = passwordEncoder.encodePassword(currentUser.getPassword(), salt);
        currentUser.setPassword(encodedPassword);

        dao.saveOrUpdate(currentUser);
        return currentUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAvatarFromCurrentUser() {
        User user = securityService.getCurrentUser();
        user.setAvatar(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getDefaultAvatar() {
        InputStream inputStream = getClass().getResourceAsStream("/org/jtalks/common/service/default_avatar.jpg");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        byte[] bytes = new byte[512];
        int readBytes;
        byte[] byteData = null;
        try {
            while ((readBytes = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, readBytes);
            }
            byteData = outputStream.toByteArray();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            byteData = new byte[]{};
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return byteData;
    }

}
