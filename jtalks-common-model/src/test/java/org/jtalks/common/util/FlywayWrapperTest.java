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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * This class contains unit tests for {@link FlywayWrapper} class.
 * Date: 22.08.11<br />
 * Time: 0:15<br />
 *
 * @author Alexey Malev
 */
public class FlywayWrapperTest {
    private FlywayWrapper sut;

    @BeforeTest
    public void setUp() {
        sut = new FlywayWrapper();
    }

    /**
     * Exception is thrown here as no real environment available.
     * Currently disable due to caused exception during compilation.
     *
     * Currently under discussion about test deleting process
     */
    @Test(expectedExceptions = FlywayException.class, enabled = false)
    public void testMigrateWithFlywayEnabled() {
        sut.setEnabled(true);
        sut.migrate();
    }

    @Test
    public void testMigrateWithFlywayDisabled() {
        sut.setEnabled(false);
        assertEquals(sut.migrate(), 0);
    }

    @Test
    public void testSmartInitDisabled() {
        try {
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            when(dataSource.getConnection()).thenReturn(connection);
            Statement statement = mock(Statement.class);
            when(connection.createStatement()).thenReturn(statement);

            sut.setDataSource(dataSource);
            sut.setTable("table");
            sut.setEnabled(false);

            sut.smartInit();

            verify(dataSource, times(0)).getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("SQLExcepton shouldn't be thrown here.", e);
        }
    }

    @Test
    public void testSmartInitWithExistentTable() {
        try {
            FlywayWrapper sut = spy(new FlywayWrapper());
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            when(dataSource.getConnection()).thenReturn(connection);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);
            when(resultSet.next()).thenReturn(true);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(connection.prepareStatement(any(String.class))).thenReturn(statement);

            sut.setDataSource(dataSource);
            sut.setTable("table");
            sut.setEnabled(true);

            sut.smartInit();

            verify(dataSource, times(1)).getConnection();
            verify((Flyway) sut, times(0)).init();
        } catch (SQLException e) {
            throw new IllegalStateException("SQLExcepton shouldn't be thrown here.", e);
        }
    }

    @Test(expectedExceptions = FlywayException.class)
    public void testSmartInitWithNonExistentTable() {
        try {
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            when(dataSource.getConnection()).thenReturn(connection);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);
            when(resultSet.next()).thenReturn(false);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(connection.prepareStatement(any(String.class))).thenReturn(statement);

            sut.setDataSource(dataSource);
            sut.setTable("table");
            sut.setEnabled(true);

            sut.smartInit();
            verify((Flyway) sut, times(1)).init();
        } catch (SQLException e) {
            throw new IllegalStateException("SQLExcepton shouldn't be thrown here.", e);
        }
    }

    @Test(expectedExceptions = FlywayException.class)
    public void testSmartInitOnMiscFail() {
        try {
            DataSource dataSource = mock(DataSource.class);
            when(dataSource.getConnection()).thenThrow(new SQLException());

            sut.setDataSource(dataSource);
            sut.setTable("table");
            sut.setEnabled(true);

            sut.smartInit();
        } catch (SQLException e) {
            throw new IllegalStateException("SQLExcepton shouldn't be thrown here.", e);
        }
    }
}
