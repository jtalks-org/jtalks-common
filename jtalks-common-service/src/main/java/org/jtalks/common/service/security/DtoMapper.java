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
package org.jtalks.common.service.security;

/**
 * <p>This class is used to retrieve DTO mapping to entities.</p>
 * <p>Mappings are being configured using annotation {@link ModelEntity},
 * added to class should be mapped to model class.</p>
 * Date: 16.09.2011<br />
 * Time: 15:19
 *
 * @author Alexey Malev
 */
public class DtoMapper {
    /**
     * This method resolves the mapping of the specified class.
     *
     * @param classname Fully-qualified classname, mapping of which needs to be resolved.
     * @return Mapped class, if there is mapping for the class corresponds to the specified argument;
     *         <code>null</code> if there is no such class.
     *         appropriate mapping otherwise.
     * @throws IllegalStateException If there is no class with <code>classname</code> can be resolved/
     */
    public Class getMapping(String classname) {
        try {
            Class providedClass = Class.forName(classname);
            ModelEntity annotation = (ModelEntity) providedClass.getAnnotation(ModelEntity.class);

            return annotation == null ? null : annotation.value();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class [" + classname + "] not found.", e);
        }
    }
}
