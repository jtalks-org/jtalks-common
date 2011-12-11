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
package org.jtalks.common.util;

import java.nio.charset.Charset;
import java.security.SecureRandom;

/**
 * Salt generator that generates salt with specified length in bits using {@link SecureRandom}.
 *
 * @author Masich Ivan
 */
public class SimpleSaltGenerator implements SaltGenerator {
    private int bitLength;

    /**
     * Constructor with length of bits.
     *
     * @param bitLength Needed length in bits for result string.
     */
    public SimpleSaltGenerator(int bitLength) {
        this.bitLength = bitLength;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String generate() {
        SecureRandom generator = new SecureRandom();

        byte[] randomBytes = new byte[bitLength / 8];
        generator.nextBytes(randomBytes);

        return new String(randomBytes, Charset.forName("US-ASCII"));
    }
}
