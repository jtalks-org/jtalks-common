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
package org.jtalks.common.migrations;

import org.jtalks.common.util.SaltGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * @author Masich Ivan
 */
public class V6__Update_user_passwordsTest {
    private static final String PASSWORD = "qaz123";
    private static final String SALT = "0123456789";
    private static final String ENCODED_PASSWORD = "a7hf32a7hf32a7hf32";
    private static final String ID = "10";
    private static final V6__Update_user_passwords MIGRATION = new V6__Update_user_passwords();

    @Test
    public void testMigrate() throws Exception {

        MessageDigestPasswordEncoder encoder = mock(MessageDigestPasswordEncoder.class);
        when(encoder.encodePassword(anyString(), anyString())).thenReturn(ENCODED_PASSWORD);

        SaltGenerator saltGenerator = mock(SaltGenerator.class);
        when(saltGenerator.generate()).thenReturn(SALT);

        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBean(MessageDigestPasswordEncoder.class)).thenReturn(encoder);
        when(context.getBean(SaltGenerator.class)).thenReturn(saltGenerator);

        HashMap<String, String> user = new HashMap<String, String>();
        user.put("id", ID);
        user.put("password", PASSWORD);

        List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
        users.add(user);

        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        when(jdbc.query(anyString(), any(RowMapper.class))).thenReturn(users);

        MIGRATION.setContext(context);

        MIGRATION.migrate(jdbc);

        verify(saltGenerator).generate();
        verify(encoder).encodePassword(PASSWORD, SALT);
        verify(jdbc).update(anyString(), eq(ENCODED_PASSWORD), eq(SALT), eq(Integer.parseInt(ID)));
    }

    @Test
    public void testRowMapper() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString("ID")).thenReturn(ID);
        when(resultSet.getString("PASSWORD")).thenReturn(PASSWORD);

        HashMap<String, String> result = MIGRATION.getRowMapper().mapRow(resultSet, 1);
        assertEquals(result.get("id"), ID);
        assertEquals(result.get("password"), PASSWORD);
    }
}
