/*
 * Copyright 2011-2020 PrimeFaces Extensions
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

public class ElementLocatorInterceptor {

    private final ElementLocator locator;

    public ElementLocatorInterceptor(ElementLocator locator) {
        this.locator = locator;
    }

    @RuntimeType
    public Object intercept(@Origin Method method, @AllArguments Object[] args) throws Throwable {
        WebElement located;
        try {
            located = locator.findElement();
        }
        catch (NoSuchElementException e) {
            throw e;
        }

        if (method.getName().equals("getWrappedElement")) {
            return located;
        }
        if (method.getName().equals("hashCode")) {
            return located.hashCode();
        }
        if (method.getName().equals("equals")) {
            return located.equals(args[0]);
        }

        try {
            return method.invoke(located, args);
        }
        catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
