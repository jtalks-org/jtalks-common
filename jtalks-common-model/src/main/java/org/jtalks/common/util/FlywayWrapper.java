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
package org.jtalks.common.util;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.exception.FlywayException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Wrapper that allows disabling flyway migrations, schema cleanup and initialization.
 *
 * @author Kirill Afonin
 * @author Alexey Malev
 */
public class FlywayWrapper extends Flyway {
    private boolean enabled = false;

    /**
     * Can be used for disabling/enabling migrations and init.
     * They are disabled by default.
     *
     * @param enabled is migrations enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int migrate() {
        if (enabled) {
            return super.migrate();
        }
        return 0;
    }

    /**
     * <p>This method performs {@link Flyway#init()} if there is no metadata table in the specified schema and if
     * <code>smartInit()</code> is enabled.</p>
     * <p><b>It is strongly recommended to disable this in production usage.</b></p>
     *
     * @throws FlywayException Any {@link SQLException} thrown inside the method
     *                         is wrapped into {@link FlywayException}
     */
    public void smartInit() {
        if (this.enabled) {
            try {
                Connection connection = this.getDataSource().getConnection();
                try {
                    PreparedStatement checkTableExistenceStatement = connection.prepareStatement("show tables like ?");
                    try {
                        checkTableExistenceStatement.setString(1, this.getTable());
                        ResultSet fetchedTableNames = checkTableExistenceStatement.executeQuery();
                        try {
                            if (!fetchedTableNames.next()) {
                                super.init();
                            }
                        } finally {
                            fetchedTableNames.close();
                        }
                    } finally {
                        checkTableExistenceStatement.close();
                    }
                } finally {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new FlywayException(e.getLocalizedMessage(), e);
            }
        }
    }
}
