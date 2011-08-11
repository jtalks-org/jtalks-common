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
package org.jtalks.common.model.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.jtalks.common.model.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

/**
 * @author Kirill Afonin
 * @author Alexey Malev
 */
@ContextConfiguration(locations = {"classpath:/org/jtalks/common/model/entity/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class UserHibernateDAOTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private UserDao DAO;
    @Autowired
    private SessionFactory sessionFactory;
    private Session session;

    private User user;

    @BeforeMethod
    public void setUp() throws Exception {
        session = sessionFactory.getCurrentSession();
        ObjectsFactory.setSession(session);

        this.user = ObjectsFactory.getDefaultUser();
    }

    /*===== Common methods =====*/

    @Test
    public void testSave() {
        DAO.saveOrUpdate(user);

        assertNotSame(user.getId(), 0, "Id not created");

        session.evict(user);
        User result = (User) session.get(User.class, user.getId());

        assertReflectionEquals(user, result);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testSaveUserWithUniqueViolation() {
        User user2 = ObjectsFactory.getDefaultUser();

        DAO.saveOrUpdate(user);
        DAO.saveOrUpdate(user2);
    }

    @Test
    public void testGet() {
        session.save(user);

        User result = DAO.get(user.getId());

        assertNotNull(result);
        assertEquals(result.getId(), user.getId());
    }

    @Test
    public void testGetInvalidId() {
        assertNull(DAO.get(-567890L));
    }

    @Test
    public void testUpdate() {
        String newName = "new name";
        session.save(user);
        user.setFirstName(newName);

        DAO.saveOrUpdate(user);
        session.evict(user);
        User result = (User) session.get(User.class, user.getId());//!

        assertEquals(result.getFirstName(), newName);
    }

    @Test(expectedExceptions = Exception.class)
    public void testUpdateNotNullViolation() {
        session.save(user);
        user.setUsername(null);

        DAO.saveOrUpdate(user);
    }

    @Test
    public void testDelete() {
        session.save(user);

        boolean result = DAO.delete(user.getId());
        int userCount = getCount();

        assertTrue(result, "Entity is not deleted");
        assertEquals(userCount, 0);
    }

    @Test
    public void testDeleteInvalidId() {

        assertFalse(DAO.delete(-100500L), "Entity deleted");
    }

    /*===== UserDao specific methods =====*/

    @Test
    public void testGetByUsername() {
        session.save(user);

        User result = DAO.getByUsername(user.getUsername());

        assertNotNull(result);
        assertReflectionEquals(user, result);
    }

    @Test
    public void testGetByUsernameNotExist() {
        session.save(user);

        User result = DAO.getByUsername("Name");

        assertNull(result);
    }

    @Test
    public void testIsUserWithEmailExist() {
        session.save(user);

        boolean result = DAO.isUserWithEmailExist(user.getEmail());

        assertTrue(result, "User not exist");
    }

    @Test
    public void testIsUserWithEmailNotExist() {
        session.save(user);

        boolean result = DAO.isUserWithEmailExist("dick@head.com");

        assertFalse(result, "User exist");
    }

    @Test
    public void testIsUserWithUsernameExist() {
        session.save(user);

        boolean result = DAO.isUserWithUsernameExist(user.getUsername());

        assertTrue(result, "User not exist");
    }

    @Test
    public void testIsUserWithUsernameNotExist() {
        session.save(user);

        boolean result = DAO.isUserWithUsernameExist("qwertyuio");

        assertFalse(result, "User exist");
    }

    @Test
    public void testIsExistPositiveScenario() {
        session.save(user);
        assertTrue(DAO.isExist(user.getId()));
    }

    @Test
    public void testIsExistNegativeScenario() {
        assertFalse(DAO.isExist(34827897345340L));
    }

    @Test
    public void testUserNotLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void testUserAccountNotExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void testUserCredentialsAreNotExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testUserEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    public void testUpdateLastLogin()
    {
        DateTime currentLastLogin = user.getLastLogin();
        user.updateLastLoginTime();

        assertNotSame(user.getLastLogin(), currentLastLogin);
    }

    @Test
    public void testUpdateUser()
    {
        DateTime currentLastLoginTime = user.getLastLogin();
        session.save(user);
        user.updateLastLoginTime();
        DAO.update(user);

        assertNotSame(user.getLastLogin(), currentLastLoginTime);
    }

    private int getCount() {
        return ((Number) session.createQuery("select count(*) from User").uniqueResult()).intValue();
    }
}
