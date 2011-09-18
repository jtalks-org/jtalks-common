package org.jtalks.common.service.mixin.modification;

import org.joda.time.DateTime;
import org.jtalks.common.model.entity.User;

/**
 * <p>This class is responsible for storing entity last modification date and the last user who modified an entity.</p>
 * Date: 18.09.2011<br />
 * Time: 14:17
 *
 * @author Alexey Malev
 */
public class ModificationAwareImpl implements ModificationAware {

    private DateTime modificationDate;
    private User modifiedBy;

    /**
     * This constructor creates an object with the specified {@code modificationDate} and {@code modifiedBy}
     *
     * @param modificationDate Last modification date of an entity.
     * @param modifiedBy       Last user modified an entity.
     */
    ModificationAwareImpl(DateTime modificationDate, User modifiedBy) {
        this.modificationDate = modificationDate;
        this.modifiedBy = modifiedBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setModificationDate(DateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setModifiedBy(User user) {
        this.modifiedBy = user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DateTime getModificationDate() {
        return this.modificationDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getModifiedBy() {
        return this.modifiedBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateModification(DateTime modificationDate, User modifiedBy) {
        if (modificationDate != null) {
            if ((this.modificationDate != null) && (modificationDate.compareTo(this.modificationDate) < 0)) {
                throw new IllegalArgumentException("Current modification date [" +
                                                         this.modificationDate +
                                                         "] is after provided modification date [" +
                                                         modificationDate + "].");
            } else {
                this.modificationDate = modificationDate;
            }
        } else {
            if (this.modificationDate != null) {
                throw new IllegalArgumentException("Cannot replace entity modification date [" +
                                                         this.modificationDate + "] with null.");
            }
        }

        if ((this.modifiedBy != null) && (modifiedBy == null)) {
            throw new IllegalArgumentException("Cannot replace last user modified entity: [" + this.modifiedBy +
                                                     "] with null.");
        }

        this.modifiedBy = modifiedBy;
    }
}
