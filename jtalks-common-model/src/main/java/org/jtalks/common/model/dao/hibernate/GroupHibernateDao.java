package org.jtalks.common.model.dao.hibernate;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.jtalks.common.model.dao.GroupDao;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.User;
import ru.javatalks.utils.general.Assert;

import java.util.List;

/**
 *
 */
public class GroupHibernateDao extends GenericDao<Group> implements GroupDao {

    public GroupHibernateDao(SessionFactory sessionFactory, Class<Group> type) {
        super(sessionFactory, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Group> getAll() {
        return session().createQuery("from Group").list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Group> getMatchedByName(String name) {
        Assert.throwIfNull(name, "name");

        Query query = session().createQuery("from Group g where g.name like ?");
        query.setString(0, "%" + name + "%");

        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> getGroupsOfUser(User user) {
        Assert.throwIfNull(user, "user");

        Query query = session().createQuery("from Group g where g.users contains ?");
        query.setParameter(0, "%" + user + "%");

        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group getGroupByName(String name) {
        Assert.throwIfNull(name, "name");

        Query query = session().createQuery("from Group g where g.name = ?");
        query.setString(0, name);

        return (Group) query.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Group group) {
        session().update(group);

        group.getUsers().clear();
        saveOrUpdate(group);
        super.delete(group);
    }
}