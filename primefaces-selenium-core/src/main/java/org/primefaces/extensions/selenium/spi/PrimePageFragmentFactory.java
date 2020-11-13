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
package org.primefaces.extensions.selenium.spi;

import java.lang.reflect.*;
import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.primefaces.extensions.selenium.AbstractPrimePage;
import org.primefaces.extensions.selenium.AbstractPrimePageFragment;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;
import org.primefaces.extensions.selenium.findby.FindByParentPartialIdElementLocator;
import org.primefaces.extensions.selenium.internal.proxy.ElementLocatorInterceptor;
import org.primefaces.extensions.selenium.internal.proxy.ElementsLocatorInterceptor;
import org.primefaces.extensions.selenium.internal.proxy.LazyElementLocator;
import org.primefaces.extensions.selenium.internal.proxy.ProxyUtils;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class PrimePageFragmentFactory {

    private PrimePageFragmentFactory() {
    }

    public static <T extends WebElement> T create(Class<T> fragment, WebElement element) {
        ElementLocator el;
        if (element instanceof AbstractPrimePageFragment) {
            el = ((AbstractPrimePageFragment) element).getElementLocator();
        }
        else {
            el = new ElementLocator() {
                @Override
                public WebElement findElement() {
                    return element;
                }

                @Override
                public List<WebElement> findElements() {
                    return null;
                }
            };
        }

        return create(fragment, element, el);
    }

    public static <T extends WebElement> T create(Class<T> fragment, WebElement element, ElementLocator el) {
        try {
            T proxy = proxy(fragment,
                        InvocationHandlerAdapter.of((Object p, Method method, Object[] args) -> method.invoke(el.findElement(), args)));

            WebDriver driver = WebDriverProvider.get();

            if (proxy instanceof AbstractPrimePage) {
                ((AbstractPrimePage) proxy).setWebDriver(driver);
            }
            if (proxy instanceof AbstractPrimePageFragment) {
                ((AbstractPrimePageFragment) proxy).setWebDriver(driver);
                ((AbstractPrimePageFragment) proxy).setElementLocator(el);
            }

            setMembers(driver, new DefaultElementLocatorFactory(proxy), proxy);

            return proxy;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void setMembers(WebDriver driver, ElementLocatorFactory elf, Object obj) {

        for (Field field : ProxyUtils.collectFields(obj)) {
            if (field.getAnnotation(FindBy.class) != null
                        || field.getAnnotation(FindAll.class) != null
                        || field.getAnnotation(FindBys.class) != null) {
                ElementLocator el = new LazyElementLocator(elf, field);

                setMember(driver, el, field, obj);
            }

            FindByParentPartialId findByParentPartialId = field.getAnnotation(FindByParentPartialId.class);
            if (findByParentPartialId != null) {
                ElementLocator parentEl = null;
                if (obj instanceof AbstractPrimePageFragment) {
                    parentEl = ((AbstractPrimePageFragment) obj).getElementLocator();
                }

                ElementLocator el = new FindByParentPartialIdElementLocator(driver, parentEl, findByParentPartialId);

                setMember(driver, el, field, obj);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void setMember(WebDriver driver, ElementLocator el, Field field, Object obj) {
        Object value = null;

        if (WebElement.class.isAssignableFrom(field.getType())) {
            value = proxy((Class<T>) field.getType(),
                        MethodDelegation.to(new ElementLocatorInterceptor(el)));

            if (value instanceof AbstractPrimePage) {
                ((AbstractPrimePage) value).setWebDriver(driver);
            }
            if (value instanceof AbstractPrimePageFragment) {
                ((AbstractPrimePageFragment) value).setWebDriver(driver);
                ((AbstractPrimePageFragment) value).setElementLocator(el);
            }

            DefaultElementLocatorFactory delf = new DefaultElementLocatorFactory((SearchContext) value);

            setMembers(driver, delf, value);
        }

        if (List.class.isAssignableFrom(field.getType())) {
            Class<? extends WebElement> genericClass = extractGenericListType(field);
            if (genericClass != null) {
                InvocationHandler handler = new ElementsLocatorInterceptor(el, genericClass);

                value = Proxy.newProxyInstance(
                            ProxyUtils.class.getClassLoader(), new Class[] {List.class}, handler);
            }
        }

        try {
            field.setAccessible(true);
            field.set(obj, value);
        }
        catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Can not set field in PageFragment!", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends WebElement> extractGenericListType(Field field) {
        // Attempt to discover the generic type of the list
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return null;
        }

        Type listGenericType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
        try {
            Class<? extends WebElement> listGenericClass = (Class<? extends WebElement>) Class.forName(listGenericType.getTypeName());
            if (WebElement.class.isAssignableFrom(listGenericClass)) {
                return listGenericClass;
            }
        }
        catch (ClassNotFoundException ex) {
            // do nothing
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> clazz, Implementation interceptor) {
        Class<T> proxyClass = (Class<T>) new ByteBuddy()
                    .subclass(clazz)
                    .implement(WrapsElement.class)
                    .method(ElementMatchers.isDeclaredBy(WebElement.class)
                                .or(ElementMatchers.isDeclaredBy(WrapsElement.class))
                                .or(ElementMatchers.named("hashCode"))
                                .or(ElementMatchers.named("equals")))
                    .intercept(interceptor)
                    .make()
                    .load(PrimeSelenium.class.getClassLoader())
                    .getLoaded();

        try {
            return proxyClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
