package org.jtalks.common.service.mixin.creation;

import org.joda.time.DateTime;
import org.jtalks.common.model.entity.User;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

/**
 * This class contains unit tests for {@link CreationAwareImpl}
 * Date: 18.09.2011<br />
 * Time: 14:28
 *
 * @author Alexey Malev
 */
public class CreationAwareImplTest {
    private CreationAwareImpl sut;
    private DateTime creationDate;
    private User createdBy;

    @BeforeTest
    public void setUp() {
        creationDate = new DateTime();
        createdBy = mock(User.class);

        sut = new CreationAwareImpl(creationDate, createdBy);
    }

    @Test
    public void testConstructor() {
        assertEquals(sut.getCreatedBy(), createdBy);
        assertEquals(sut.getCreationDate(), creationDate);
    }

    @Test
    public void testSetCreationDate() {
        DateTime newCreationDate = new DateTime(System.currentTimeMillis() + 1000);

        sut.setCreationDate(newCreationDate);

        assertEquals(sut.getCreationDate(), newCreationDate);
    }

    @Test
    public void testSetModifiedBy() {
        User newCreatedBy = mock(User.class);

        sut.setCreatedBy(newCreatedBy);

        assertEquals(sut.getCreatedBy(), newCreatedBy);
    }
}
