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

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * The Property entity which contains configuration for components. Components have default properties (like JCommune
 * has some number of predefined properties) with default values which are represented by Property. And when actual
 * component is created, default properties are copied to that component with their default values and those particular
 * properties are also represented by Property class.
 *
 * @author Vahluev Vyacheslav
 * @author Ancient_Mariner
 */
public class Property extends Entity {

    private static final String PROPERTY_NAME_ILLEGAL_LENGTH = "{property.name.length_constraint_violation}";
    private static final String PROPERTY_CANT_BE_VOID = "{property.name.emptiness_constraint_violation}";

    public static final int PROPERTY_NAME_MAX_LENGTH = 256;

    /**
     * Property's name
     */
    @NotBlank(message = PROPERTY_CANT_BE_VOID)
    @Length(max = PROPERTY_NAME_MAX_LENGTH, message = PROPERTY_NAME_ILLEGAL_LENGTH)
    private String name;
    /**
     * Property's value
     */
    private String value;

    /**
     * Property's validation rule
     */
    private String validationRule;

    /**
     * Default constructor, sets nothing, used only by hibernate
     */
    protected Property() {
    }

    /**
     * Constructor which sets name and value of the property
     *
     * @param name  of the property
     * @param value of the property
     */
    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the name of the property
     *
     * @return name of the property
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the property
     *
     * @param name new name of the property
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of the property
     *
     * @return current value of the property
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the property
     *
     * @param value new value of the property
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the validation rule of the property
     *
     * @return current validationRule of the property
     */
    public String getValidationRule() {
        return validationRule;
    }

    /**
     * Sets the validation rule of the property
     *
     * @param validationRule new validationRule of the property
     */
    public void setValidationRule(String validationRule) {
        this.validationRule = validationRule;
    }
}
