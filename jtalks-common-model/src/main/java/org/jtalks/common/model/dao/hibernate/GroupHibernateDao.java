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

    public GroupHibernateDao(SessionFactory sessionFactory) {
        super(sessionFactory, Group.class);
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