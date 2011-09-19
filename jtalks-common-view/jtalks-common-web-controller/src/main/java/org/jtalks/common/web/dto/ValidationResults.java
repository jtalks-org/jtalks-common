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
package org.jtalks.common.web.dto;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>This class used for easier entity validation results representation.
 * In some meaning, this is DTO pattern usage.</p>
 * <p>Returned JSON object contain three fields:</p>
 * <ul>
 * <li><code>globalErrors</code> - list of string messages describing
 * global validation errors (not related to particular field);</li>
 * <li><p><code>fieldErrors</code> - list of the errors related to particular fields in the following format:</p>
 * Each fieldError has field name as the key and an array of strings
 * containing list of error messages related to that field;
 * </li>
 * <li><code>success</code> - it's value is <code>true</code> if validation passed and <code>false</code> otherwise
 * (i.e. there are some field or global errors)</li>
 * </ul>
 * Date: 15.08.11<br />
 * Time: 22:59<br />
 *
 * @author Alexey Malev
 */
public final class ValidationResults {

    private boolean success;

    private Map<String, List<String>> fieldErrors;

    private List<String> globalErrors;

    private long entityId;

    /**
     * Constructor accepting binding result. It parses global and field errors.
     *
     * @param bindingResult BindingResult to be parsed.
     */
    public ValidationResults(BindingResult bindingResult) {
        this.success = !bindingResult.hasErrors();

        populateFieldErrors(bindingResult);
        populateGlobalErrors(bindingResult);
    }

    /**
     * This method populates field errors from the specified {@link BindingResult} to the {@code fieldErrors} structure
     *
     * @param bindingResult Binding result to populate field errors from
     */
    private void populateFieldErrors(BindingResult bindingResult) {
        fieldErrors = new HashMap<String, List<String>>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            List<String> currentFieldErrors = fieldErrors.get(error.getField());
            if (currentFieldErrors == null) {
                currentFieldErrors = new ArrayList<String>();
            }

            currentFieldErrors.add(error.getDefaultMessage());
            fieldErrors.put(error.getField(), currentFieldErrors);
        }
    }

    /**
     * This method populates global (not related to particular field) errors from the specified {@link BindingResult}
     * to the {@code globalErrors} structure
     *
     * @param bindingResult Binding result to populate global errors from
     */
    private void populateGlobalErrors(BindingResult bindingResult) {
        globalErrors = new ArrayList<String>(bindingResult.getGlobalErrorCount());
        for (ObjectError error : bindingResult.getGlobalErrors()) {
            globalErrors.add(error.getDefaultMessage());
        }
    }

    /**
     * @param bindingResult BindingResult to be parsed.
     * @param entityId      if entity was created, this field is used to transfer entity's id
     */
    public ValidationResults(BindingResult bindingResult, long entityId) {
        this(bindingResult);
        this.entityId = entityId;
    }


    /**
     * Returns the status of validation descibed by this validation result.
     *
     * @return <code>true</code> if validation was successful;<br />
     *         <code>false</code> otherwise.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * This method returns map of field errors of the current validation.
     * Field names mapped to lists of string error messages related to the field.
     *
     * @return Map of field errors described above.
     */
    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }

    /**
     * This method returns list of global errors.
     *
     * @return List of global (not related to particular field) validation errors.
     */
    public List<String> getGlobalErrors() {
        return globalErrors;
    }

    /**
     * Getter for entity id
     *
     * @return entity id
     */
    public long getEntityId() {
        return entityId;
    }
}
