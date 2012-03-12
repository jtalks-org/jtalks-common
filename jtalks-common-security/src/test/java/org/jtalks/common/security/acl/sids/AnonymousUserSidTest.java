package org.jtalks.common.security.acl.sids;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author stanislav bashkirtsev
 */
public class AnonymousUserSidTest {
    @Test
    public void testIsAnonymous() throws Exception {
        assertTrue(AnonymousUserSid.isAnonymous("anonymousUser"));
    }

    @Test
    public void testIsAnonymous_shortened() throws Exception {
        assertFalse(AnonymousUserSid.isAnonymous("anonymous"));
    }

    @Test
    public void testIsAnonymous_prolonged() throws Exception {
        assertFalse(AnonymousUserSid.isAnonymous("anonymousUser1"));
    }

    @Test
    public void testIsAnonymous_withNull() throws Exception {
        assertFalse(AnonymousUserSid.isAnonymous(null));
    }
    
    @Test
    public void testCreate() {
        AnonymousUserSid sid =  AnonymousUserSid.create();
        assertEquals(sid.getSidId(), "user:anonymousUser");
        assertSame(AnonymousUserSid.create(), sid);
    }
}
