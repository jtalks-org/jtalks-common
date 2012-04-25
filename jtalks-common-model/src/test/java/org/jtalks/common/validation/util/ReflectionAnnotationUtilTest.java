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
package org.jtalks.common.validation.util;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.jtalks.common.validation.annotations.UniqueField;

import java.lang.reflect.Field;
import java.util.List;

public class ReflectionAnnotationUtilTest {

    private static List<String> toNames(List<AnnotatedField<UniqueField>> fields) {
        return Lists.transform(fields, fieldNamesRetriever);
    }

    private final static Function<AnnotatedField<?>, String> fieldNamesRetriever = 
            new Function<AnnotatedField<?>, String>() {
        public String apply(AnnotatedField<?> input) {
            return input.getFieldName();
        }
    };

    private static List<Field> toFields(List<AnnotatedField<UniqueField>> fields) {
        return Lists.transform(fields, fieldRetriever);
    }

    private final static Function<AnnotatedField<?>, Field> fieldRetriever = 
            new Function<AnnotatedField<?>, Field>() {
        public Field apply(AnnotatedField<?> input) {
            return input.getField();
        }
    };

}
