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

import java.util.ArrayList;
import java.util.List;

/**
 * Forum section that contains branches.
 * 
 * @author Tatiana Birina
 * @author Guram Savinov
 */
public class Section extends Entity {
   /**
     * Error message if section description is illegal length
     */
    private static final String SECTION_DESCRIPTION_ILLEGAL_LENGTH =
                                                        "{section.description.length_constraint_violation}";
    /**
     * Error message if section name is illegal length
     */
    private static final String SECTION_NAME_ILLEGAL_LENGTH = "{section.name.length_constraint_violation}";
    private static final String SECTION_CANT_BE_VOID = "{section.name.emptiness_constraint_violation}";

    public static final int SECTION_NAME_MAX_LENGTH = 80;
    public static final int SECTION_DESCRIPTION_MAX_LENGTH = 255;

    @NotBlank(message = SECTION_CANT_BE_VOID)
    @Length(max = SECTION_NAME_MAX_LENGTH, message = SECTION_NAME_ILLEGAL_LENGTH)
    private String name;

    @Length(max = SECTION_DESCRIPTION_MAX_LENGTH, message = SECTION_DESCRIPTION_ILLEGAL_LENGTH)
    private String description;

    private Integer position;
    private List<Branch> branches = new ArrayList<Branch>();

    /**
     * Default constructor, protected for using only by Hibernate and subclasses
     */
    protected Section() {
    }

    /**
     * Creates a section with empty list of branches setting section a name
     * 
     * @param name for new section
     */
    public Section(String name) {
        this.name = name;
    }

    /**
     * Constructor with name and description, creates a section with empty list of branches
     * 
     * @param name - name for new section
     * @param description - description for new section
     */
    public Section(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Set section name which briefly describes the topics contained in it.
     * 
     * @return name section.
     */
    public String getName() {
        return name;
    }

    /**
     * Set section name.
     * 
     * @param name - name for section.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get section description.
     * 
     * @return description for section
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set section description which contains additional information about the
     * section.
     * 
     * @param description - description for section
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get section position.
     * 
     * @return position
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Set section position.
     * 
     * @param position - position for section
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * Get section branches
     * 
     * @return list of branches
     */
    public List<Branch> getBranches() {
        return branches;
    }

    /**
     * Set section branches
     * 
     * @param branches - list of branches
     */
    protected void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    /**
     * Adds branch to the section or updates if it exist.
     * 
     * @param branch the branch for adding to section
     */
    public void addOrUpdateBranch(Branch branch) {
        for (int index = 0; index < branches.size(); index++) {
            long id = branches.get(index).getId();
            
            if (id != 0 && id == branch.getId()) {
                branches.set(index, branch);
                return;
            }
        }
        
        branches.add(branch);
    }

    /**
     * Delete branch from the section.
     * 
     * @param branch the branch for deleting from section
     */
    public void deleteBranch(Branch branch) {
        branches.remove(branch);
    }

    @Override
    public String toString() {
        return "PoulpeSection [id=" + getId() + ", name=" + name + ", description=" + description + "]";
    }


}