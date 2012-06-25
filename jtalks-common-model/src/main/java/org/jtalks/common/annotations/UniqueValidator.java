package org.jtalks.common.annotations;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jtalks.common.ApplicationContextAccessor;
import org.jtalks.common.model.entity.Entity;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Ensures uniqueness of the field value given using an hql query provided.
 *
 * @author Evgeniy Naumenko
 * @see Unique
 */
public class UniqueValidator implements ConstraintValidator<Unique, String> {
    private Class<? extends Entity> entity;
    private String field;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(Unique annotation) {
        this.entity = annotation.entity();
        this.field = annotation.field();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
//        return (value != null) && dao.isResultSetEmpty(entity, field, value);
        return true;
    }

    private Session getSession(SessionFactory sessionFactory) {
        return SessionFactoryUtils.getSession(sessionFactory, true);
    }

    private SessionFactory getSessionFactory() {
        return ApplicationContextAccessor.getBean(SessionFactory.class);
    }
}
