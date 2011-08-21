package org.jtalks.common.util;

import com.googlecode.flyway.core.exception.FlywayException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * This class contains unit tests for {@link FlywayWrapper} class.
 * Date: 22.08.11<br />
 * Time: 0:15<br />
 *
 * @author Alexey Malev
 */
public class FlywayWrapperTest {
    private FlywayWrapper sut;

    @BeforeTest
    public void setUp() {
        sut = new FlywayWrapper();
    }

    /**
     * Exception is thrown here as no real environment available.
     */
    @Test(expectedExceptions = FlywayException.class)
    public void testMigrateWithFlywayEnabled() {
        sut.setEnabled(true);
        sut.migrate();
    }

    @Test
    public void testMigrateWithFlywayDisabled() {
        sut.setEnabled(false);
        assertEquals(sut.migrate(), 0);
    }

}
