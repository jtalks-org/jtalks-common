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
package org.jtalks.common.security.acl;

import com.google.common.annotations.VisibleForTesting;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.User;
import org.springframework.security.acls.domain.DefaultSidFactory;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.domain.SidFactory;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Decides what implementation of {@link Sid} should be created by the string representation of the sid name (or sid
 * id). There are might be either standard {@link Sid}s or custom sids like {@link UserGroupSid}. If you want to add
 * another possible implementation, take a look at the method {@link #getCustomSids()}.
 *
 * @author stanislav bashkirtsev
 * @see Sid
 * @see UniversalSid
 */
public class JtalksSidFactory implements SidFactory {
    private final static List<SidConfiguration> CUSTOM_SIDS = getCustomSids();

    /**
     * This is a static factory, it shouldn't be instantiated.
     */
    public JtalksSidFactory() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sid createPrincipal(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            return new UserSid((User) authentication.getPrincipal());
        } else {
            return new UserSid(authentication.getPrincipal().toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Sid> createGrantedAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities) {
        return new DefaultSidFactory().createGrantedAuthorities(grantedAuthorities);
    }

    public List<Sid> create(List<? extends Entity> receivers){
        List<Sid> sids = new ArrayList<Sid>(receivers.size());
        for(Entity next: receivers){
            sids.add(create(next));
        }
        return sids;
    }
    
    public Sid create(Entity receiver){
        for (SidConfiguration customSidEntry : CUSTOM_SIDS) {
            if (receiver.getClass() == customSidEntry.getReceiverClass()) {
                try {
                    return customSidEntry.getSidClass().getDeclaredConstructor(receiver.getClass()).newInstance(receiver);
                } catch (InstantiationException e) {
                    throw new SidClassIsNotConcreteException(customSidEntry.getSidClass(), e);
                } catch (IllegalAccessException e) {
                    throw new SidWithoutRequiredConstructorException(customSidEntry.getSidClass(), e);
                } catch (InvocationTargetException e) {
                    throw new SidConstructorThrewException(customSidEntry.getSidClass(), e);
                } catch (NoSuchMethodException e) {
                    throw new SidWithoutRequiredConstructorException(customSidEntry.getSidClass(), e);
                }
            }
        }
        return null;
    }
    
    /**
     * Looks at the format of the {@code sidName} and finds out what sid implementation should be created. If the
     * specified name doesn't comply with the format of custom sids (prefix + {@link UniversalSid#SID_NAME_SEPARATOR} +
     * entity id), then ordinary Spring Security implementations are used (either {@link PrincipalSid} or {@link
     * GrantedAuthoritySid} which is defined by the second parameter {@code principal}.
     *
     * @param sidName   the name of the sid (its id) to look at its format and decide what implementation of sid should
     *                  be created
     * @param principal pass {@code true} if it's some kind of entity ID (like user or group), or {@code false} if it's
     *                  some standard role ({@link GrantedAuthoritySid}
     * @return created instance of sid that has the {@code sidName} as the sid id inside
     */
    @Override
    public Sid create(@Nonnull String sidName, boolean principal) {
        Sid toReturn = parseCustomSid(sidName);
        if (toReturn == null) {
            if (principal) {
                toReturn = new PrincipalSid(sidName);
            } else {
                toReturn = new GrantedAuthoritySid(sidName);
            }
        }
        return toReturn;
    }

    /**
     * Iterates through all the mapping from {@link #getCustomSids()} and if it finds some custom sid class that should
     * be instantiated (it looks at the sid name that should start with sid prefix), then it instantiates it via
     * Reflection.
     *
     * @param sidName the name of the sid to find the respective sid implementation
     * @return the instantiated sid implementation that complies with the pattern of specified sid name or {@code null}
     *         if no mapping for that name was found and there are no appropriate custom implementations of sid
     */
    private static Sid parseCustomSid(String sidName) {
        for (SidConfiguration customSidEntry : CUSTOM_SIDS) {
            if (sidName.startsWith(customSidEntry.getSidPrefix())) {
                try {
                    return customSidEntry.getSidClass().getDeclaredConstructor(String.class).newInstance(sidName);
                } catch (InstantiationException e) {
                    throw new SidClassIsNotConcreteException(customSidEntry.getSidClass(), e);
                } catch (IllegalAccessException e) {
                    throw new SidWithoutRequiredConstructorException(customSidEntry.getSidClass(), e);
                } catch (InvocationTargetException e) {
                    throw new SidConstructorThrewException(customSidEntry.getSidClass(), e);
                } catch (NoSuchMethodException e) {
                    throw new SidWithoutRequiredConstructorException(customSidEntry.getSidClass(), e);
                }
            }
        }
        return null;
    }

    /**
     * Gets the mapping of the sid implementations to the sid name formats. All the custom sids should have the prefix
     * like {@link UserGroupSid#SID_PREFIX} by which we can find out what type of sid it is.
     *
     * @return the mapping of the sid implementations to the string format of the sids
     */
    private static List<SidConfiguration> getCustomSids() {
        List<SidConfiguration> sidConfigurations = new ArrayList<SidConfiguration>(2);
        sidConfigurations.add(new SidConfiguration(UserGroupSid.SID_PREFIX, Group.class, UserGroupSid.class));
        sidConfigurations.add(new SidConfiguration(UserSid.SID_PREFIX, User.class, UserSid.class));
        return sidConfigurations;
    }

    @VisibleForTesting
    static void addMapping(String sidPrefix, Class<?> receiverClass, Class<? extends UniversalSid> sidClass) {
        CUSTOM_SIDS.add(new SidConfiguration(sidPrefix, receiverClass, sidClass));
    }

    /**
     * Can be thrown if the sid implementation is not a concrete class.
     *
     * @author stanislav bashkirtsev
     */
    public static class SidClassIsNotConcreteException extends RuntimeException {
        /**
         * @param sidClass the class of the sid implementation that caused the problem
         * @param ex       reflection exception that originally caused the error when factory tried to instantiate the
         *                 class
         */
        public SidClassIsNotConcreteException(Class<? extends UniversalSid> sidClass, Throwable ex) {
            super(sidClass + " class is not a concrete class: its either an interface or abstract.", ex);
        }
    }

    /**
     * All the custom sid implementations should have at least one public constructor that accepts a string parameter -
     * the sid id. If there is no such constructor, the factory cannot instantiate the class correctly and throws this
     * exception.
     *
     * @author stanislav bashkirtsev
     */
    public static class SidWithoutRequiredConstructorException extends RuntimeException {
        /**
         * @param sidClass the class of the sid implementation that caused the problem
         * @param ex       reflection exception that originally caused the error when factory tried to instantiate the
         *                 class
         */
        public SidWithoutRequiredConstructorException(Class<? extends UniversalSid> sidClass, Throwable ex) {
            super(sidClass + " doesnt have a public constructor with single String argument.", ex);
        }
    }

    /**
     * Is thrown when the custom sid implementation threw an exception inside of its constructor while the factory tried
     * to instantiate it.
     */
    public static class SidConstructorThrewException extends RuntimeException {
        /**
         * @param sidClass the class that has the public constructor with one string parameter which was tried to be
         *                 instantiated
         * @param ex       the root exception that was thrown when the constructor was invoked
         */
        public SidConstructorThrewException(Class<? extends UniversalSid> sidClass, Throwable ex) {
            super(sidClass + ". While initiating the class, it threw an exception.", ex);
        }
    }

    private static class SidConfiguration {
        private final String sidPrefix;
        private final Class<?> receiverClass;
        private final Class<? extends UniversalSid> sidClass;

        private SidConfiguration(String sidPrefix, Class<?> receiverClass, Class<? extends UniversalSid> sidClass) {
            this.sidPrefix = sidPrefix;
            this.receiverClass = receiverClass;
            this.sidClass = sidClass;
        }

        public String getSidPrefix() {
            return sidPrefix;
        }

        public Class<?> getReceiverClass() {
            return receiverClass;
        }

        public Class<? extends UniversalSid> getSidClass() {
            return sidClass;
        }
    }
}
