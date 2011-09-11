/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.common.util;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.exception.FlywayException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
     * smartInit()
     * is enabled.</p>
     * <p><b>It is strongly recommended to disable this in production usage.</b></p>
     *
     * @throws FlywayException Any {@link SQLException} thrown inside the method is wrapped into {@link FlywayException}
     */
    public void smartInit() throws FlywayException {
        if (this.enabled) {
            String query = "select 1 from " + this.getTable();
            try {
                Connection connection = this.getDataSource().getConnection();
                try {
                    Statement checkTableExistenceStatement = connection.createStatement();
                    try {
                        checkTableExistenceStatement.executeQuery(query);

                    } catch (SQLException e) {
                        //exception handling used as a fork - there is no other way to unify table existence check for
                        //different DMBS.

                        //if the query has thrown an exception - there is no table in schema
                        super.init();
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
