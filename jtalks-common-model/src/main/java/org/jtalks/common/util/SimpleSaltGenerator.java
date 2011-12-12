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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Salt generator that generates salt with specified length in bits using {@link SecureRandom}.
 *
 * @author Masich Ivan
 * @author Alexey Malev
 */
public class SimpleSaltGenerator implements SaltGenerator {

    private int bitLength;

    private Random generator;

    /**
     * This constructor creates generator that generates string with length equal to {@code bitLength / 8}.
     *
     * @param bitLength Required length in bits (single-byte encoding) for result string.
     */
    public SimpleSaltGenerator(int bitLength) {
        this.bitLength = bitLength;
        this.generator = new SecureRandom();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String generate() {
        int currentBitLength = bitLength;
        String randomString = new BigInteger(bitLength, generator).toString(36);
        while (randomString.length() < bitLength / 8) {
            currentBitLength = currentBitLength * 2;
            randomString = new BigInteger(bitLength, generator).toString(36);
        }
        return randomString.substring(0, bitLength / 8);
    }
}
