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
 * Forum branch that contains topics related to branch theme.
 * 
 * @author Pavel Vervenko
 */
public class Branch extends Entity {

    private static final String BRANCH_DESCRIPTION_ILLEGAL_LENGTH = "{branch.description.length_constraint_violation}";
    private static final String BRANCH_CANT_BE_VOID = "{branch.name.emptiness_constraint_violation}";
    private static final String BRANCH_NAME_ILLEGAL_LENGTH = "{branch.name.length_constraint_violation}";

    public static final int BRANCH_NAME_MAX_LENGTH = 80;
    public static final int BRANCH_DESCRIPTION_MAX_LENGTH = 255;

    @NotBlank(message = BRANCH_CANT_BE_VOID)
    @Length(max = BRANCH_NAME_MAX_LENGTH, message = BRANCH_NAME_ILLEGAL_LENGTH)
    private String name;

    @Length(max = BRANCH_DESCRIPTION_MAX_LENGTH, message = BRANCH_DESCRIPTION_ILLEGAL_LENGTH)
    private String description;
    private Integer position;
    private Section section;

    private Group moderatorsGroup;

    /**
     * Default constructor, protected for using only by hibernate
     */
    protected Branch() {
    }

    /**
     * Create PoulpeBranch with name and description
     * 
     * @param name - name for new PoulpeBranch
     * @param description - description for new PoulpeBranch
     */
    public Branch(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Get branch name which briefly describes the topics contained in it.
     * 
     * @return PoulpeBranch name as String
     */
    public String getName() {
        return name;
    }

    /**
     * Set branch name.
     * 
     * @param name - PoulpeBranch name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get branch description.
     * 
     * @return PoulpeBranch description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set branch description which contains additional information about the
     * branch.
     * 
     * @param description - PoulpeBranch description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return its parent section
     */
    public Section getSection() {
        return section;
    }

    /**
     * Sets the section in which this branch is.
     * 
     * @param section the parent section
     */
    public void setSection(Section section) {
        this.section = section;
    }

    /**
     * Gets the position of this branch within all branches of its parent
     * section
     * 
     * @return the position
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Sets the position of this branch within all branches of its parent
     * section
     * 
     * @param position the position to set
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * Sets a {@link Group} of moderators for this branch. This field is an extra information about moderators, since we
     * already have ACL records that explain what permissions groups have on branches, but this moderators group is just
     * for convenience - in order to easily work with moderators.
     *
     * @param moderatorsGroup a group of moderators for this branch
     */
    public void setModeratorsGroup(Group moderatorsGroup) {
        this.moderatorsGroup = moderatorsGroup;
    }

    /**
     * Returns a {@link Group} of moderators of this branch. This field is an extra information about moderators, since
     * we already have ACL records that explain what permissions groups have on branches, but this moderators group is
     * just for convenience - in order to easily work with moderators.
     *
     * @return a group of moderators for this branch
     */
    public Group getModeratorsGroup() {
        return moderatorsGroup;
    }

    @Override
    public String toString() {
        return "PoulpeBranch [id=" + getId() + ", name=" + name + ", description=" + description + ", position="
                + position + "]";
    }
}
