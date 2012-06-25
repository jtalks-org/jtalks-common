package org.jtalks.common.annotations;

import org.jtalks.common.model.entity.Entity;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Marks the field to check it's value for non-existence in a database. You should specify an Entity and a field to
 * search for value in.
 * <p/>
 * <p>Works only for sting variables as for now.
 *
 * @author Evgeniy Naumenko
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {

    /**
     * Resource bundle code for error message
     */
    String message();

    /**
     * Groups settings for this validation constraint
     */
    Class<?>[] groups() default {};

    /**
     * Payload, no used here
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Entity to be verified
     */
    Class<? extends Entity> entity();

    /**
     * Field to be checked
     */
    String field();
}