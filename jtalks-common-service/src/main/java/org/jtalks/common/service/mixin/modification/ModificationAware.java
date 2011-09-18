package org.jtalks.common.service.mixin.modification;

import org.joda.time.DateTime;
import org.jtalks.common.model.entity.User;

/**
 * <p>This interface extends {@link ModificationAwareEntity} by adding setter for creation date and the creator.</p>
 * Date: 18.09.2011<br />
 * Time: 14:14
 *
 * @author Alexey Malev
 */
public interface ModificationAware extends ModificationAwareEntity {
    /**
     * This method sets entity modification date to specified value.
     *
     * @param modificationDate New entity modification date.
     */
    void setModificationDate(DateTime modificationDate);

    /**
     * This method sets last user who modified entity, to specified value.
     *
     * @param user Last user modified entity.
     */
    void setModifiedBy(User user);
}
