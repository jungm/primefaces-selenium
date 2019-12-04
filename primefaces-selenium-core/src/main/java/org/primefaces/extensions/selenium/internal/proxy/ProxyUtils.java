/**
 * Copyright 2011-2019 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.selenium.internal.proxy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProxyUtils {

    private ProxyUtils() {
    }

    public static Class<?> getUnproxiedClass(Class<?> clazz) {
        if (clazz.getName().contains("$ByteBuddy")) {
            return clazz.getSuperclass();
        }
        return clazz;
    }

    public static List<Field> collectFields(Object instance) {
        Class<?> clazz = getUnproxiedClass(instance.getClass());

        ArrayList<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        Class<?> superClazz = clazz.getSuperclass();
        while (superClazz != null && superClazz != Object.class) {
            fields.addAll(Arrays.asList(superClazz.getDeclaredFields()));

            superClazz = superClazz.getSuperclass();
        }

        return fields;
    }
}
