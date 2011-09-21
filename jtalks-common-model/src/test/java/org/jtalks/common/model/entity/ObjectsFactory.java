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

import org.hibernate.Session;

/**
 * @author Kirill Afonin
 * @author Alexandre Teterin
 */
//TODO: split this class on 2: objects factory and persisted objects factory
public final class ObjectsFactory {
    private ObjectsFactory() {
    }

    public static void setSession(Session session) {
        ObjectsFactory.session = session;
    }

    private static Session session;

    public static User getDefaultUser() {
        return getUser("username", "username@mail.com");
    }

    public static User getUser(String username, String email) {
        User newUser = new User(username, email, "password");
        newUser.setFirstName("first name");
        newUser.setLastName("last name");
        return newUser;
    }

    private static <T> T persist(T entity) {
        session.save(entity);
        return entity;
    }

}
