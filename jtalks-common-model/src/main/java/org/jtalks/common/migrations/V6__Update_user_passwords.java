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

import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.jtalks.common.util.SaltGenerator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Migration for generate salt and encode existing users password.
 *
 * @author Masich Ivan
 */
public class V6__Update_user_passwords implements JavaMigration {

    private ApplicationContext context;

    private RowMapper<HashMap<String, String>> rowMapper = new RowMapper<HashMap<String, String>>() {
        /**
         * @inheritDoc
         */
        @Override
        public HashMap<String, String> mapRow(ResultSet resultSet, int i) throws SQLException {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", resultSet.getString("ID"));
            map.put("password", resultSet.getString("PASSWORD"));

            return map;
        }
    };

    /**
     * @inheritDoc
     */
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        MessageDigestPasswordEncoder passwordEncoder = getContext().getBean(MessageDigestPasswordEncoder.class);
        SaltGenerator saltGenerator = getContext().getBean(SaltGenerator.class);

        List<HashMap<String, String>> users = jdbcTemplate.query(
            "SELECT `ID`, `PASSWORD` FROM `USERS` WHERE `SALT` = ''",
                getRowMapper()
        );

        for (HashMap<String, String> user : users) {

            String salt = saltGenerator.generate();
            String encodedPassword = passwordEncoder.encodePassword(
                user.get("password"), salt
            );

            jdbcTemplate.update(
                "UPDATE `USERS` SET `PASSWORD` = ?, `SALT` = ? WHERE `ID` = ?",
                encodedPassword, salt, Integer.parseInt(user.get("id"))
            );
        }
    }

    /**
     * Get application context object.
     *
     * @return {@link ApplicationContext} object with password encoder and salt generator beans.
     */
    private ApplicationContext getContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("org/jtalks/common/service/security-password-context.xml");
        }

        return context;
    }

    /**
     * Set application context object.
     *
     * @param context {@link ApplicationContext} object with password encoder and salt generator beans.
     */
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public RowMapper<HashMap<String, String>> getRowMapper() {
        return rowMapper;
    }

    public void setRowMapper(RowMapper<HashMap<String, String>> rowMapper) {
        this.rowMapper = rowMapper;
    }
}
