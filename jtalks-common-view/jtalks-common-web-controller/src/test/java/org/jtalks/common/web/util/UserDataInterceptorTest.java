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
package org.jtalks.common.web.util;

import org.jtalks.common.model.entity.User;
import org.jtalks.common.service.SecurityService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * @author Kirill Afonin
 */
public class UserDataInterceptorTest {
    private final String USER_NAME = "username";
    private final String ENCODED_USER_NAME = "username";
    private final String FIRST_NAME = "first name";
    private final String LAST_NAME = "last name";
    private final String EMAIL = "mail@mail.com";
    private final String PASSWORD = "password";

    private UserDataInterceptor interceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private SecurityService securityService;

    @BeforeMethod
    public void setUp() throws Exception {
        securityService = mock(SecurityService.class);
        interceptor = new UserDataInterceptor(securityService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testPostHandle() throws Exception {
        User user = getUser();
        when(securityService.getCurrentUser()).thenReturn(user);

        interceptor.postHandle(request, response, null, null);

        assertEquals(request.getAttribute("encodedUserName"), ENCODED_USER_NAME);
        verify(securityService).getCurrentUser();
    }

    @Test
    public void testPostHandleWithoutCurrentUser() throws Exception {
        when(securityService.getCurrentUser()).thenReturn(null);

        interceptor.postHandle(request, response, null, null);

        assertEquals(request.getAttribute("encodedUserName"), null);
        verify(securityService).getCurrentUser();
    }

    private User getUser() {
        User newUser = new User(USER_NAME, EMAIL, PASSWORD);
        newUser.setFirstName(FIRST_NAME);
        newUser.setLastName(LAST_NAME);
        return newUser;
    }
}
