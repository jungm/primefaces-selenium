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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.primefaces.extensions.selenium.spi.PrimePageFragmentFactory;

public class LocatingElementListHandler implements InvocationHandler {

    private final ElementLocator locator;
    private final Class<? extends WebElement> genericClass;

    public LocatingElementListHandler(ElementLocator locator, Class<? extends WebElement> genericClass) {
        this.locator = locator;
        this.genericClass = genericClass;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
        List<WebElement> elements;
        if (genericClass == WebElement.class) {
            elements = locator.findElements();
        }
        else {
            elements = new ArrayList<>();
            for (WebElement element : locator.findElements()) {
                elements.add(PrimePageFragmentFactory.create(genericClass, element));
            }
        }

        try {
            return method.invoke(elements, objects);
        }
        catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            throw e.getCause();
        }
    }
}
