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
package org.jtalks.common.model.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

/**
 * @author Kirill Afonin
 * @author Ancient_Mariner
 */
public class UserTest {
    private static final String SALT = "salt";
    private User sut;

    @BeforeMethod
    public void setUp() {
        sut = new User("username", "email@mail.com", "pass", SALT);
    }

    @Test
    public void testSpringSecurityDefaults() {
        assertTrue(sut.isAccountNonExpired());
        assertTrue(sut.isAccountNonLocked());
        assertTrue(sut.isCredentialsNonExpired());
        assertTrue(sut.isEnabled());
    }

    @Test
    public void testSetUserNameEncodingSymbolReplacement(){

        String expected = "Hello%20World";
        String unencoded = "Hello World";

        sut.setUsername(unencoded);
        assertEquals(sut.getEncodedUsername(), expected);
    }
        
    @Test
    public void testUserDefaultAuthority() {
        GrantedAuthority expectedAuthority = new GrantedAuthorityImpl("ROLE_USER");

        assertTrue(sut.getAuthorities().contains(expectedAuthority));
    }

    @Test
    public void testSetNullAvatar() {
        sut.setAvatar(null);

        assertTrue(Arrays.equals(sut.getAvatar(), new byte[0]));
    }

    @Test
    public void testSetNormalAvatar() {
        byte[] testAvatar = new byte[] {0, 1, 2};

        sut.setAvatar(testAvatar);

        assertTrue(Arrays.equals(sut.getAvatar(), testAvatar));
    }

    @Test
    public void testSetDefaultPermanentBan() {
        assertEquals(sut.isPermanentBan(), false);
    }

    @Test
    public void testSetDefaultBanExpiredDate() {
        assertNull(sut.getBanExpirationDate());
    }

    @Test
    public void testSetDefaultBanDescription() {
        assertNull(sut.getBanReason());
    }

    @Test
    public void testGetSalt() {
        assertEquals(sut.getSalt(), SALT);
    }

    @Test
    public void testGetAvatarClonesCurrent() {
        assertEquals(sut.getAvatar(), new byte[0], "Returned unexpected array");
    }
}
