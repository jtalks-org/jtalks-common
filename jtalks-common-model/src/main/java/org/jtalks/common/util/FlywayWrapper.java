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

/**
 * Wrapper that allows disabling flyway migrations, schema cleanup and initialization.
 *
 * @author Kirill Afonin
 * @author Alexey Malev
 */
public class FlywayWrapper extends Flyway {
    private boolean enabled = true;

    /**
     * Can be used for disabling/enabling migrations.
     * Migrations enabled by default.
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
     * {@inheritDoc}
     */
    @Override
    public void clean() {
        if (this.enabled) {
            super.clean();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws FlywayException {
        if (this.enabled) {
            super.init();
        }
    }
}
