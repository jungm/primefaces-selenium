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
package org.primefaces.extensions.selenium.spi;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
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
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;
import org.primefaces.extensions.selenium.findby.FindByParentPartialIdElementLocator;
import org.primefaces.extensions.selenium.internal.proxy.ProxyUtils;
import org.primefaces.extensions.selenium.internal.WebDriverAware;
import org.primefaces.extensions.selenium.internal.proxy.ElementLocatorInterceptor;
import org.primefaces.extensions.selenium.internal.proxy.ListProxyInvocationHandler;
import org.primefaces.extensions.selenium.internal.ElementLocatorAware;
import org.primefaces.extensions.selenium.internal.proxy.LazyElementLocator;

public class PrimePageFragmentFactory {

    private PrimePageFragmentFactory() {
    }

    public static <T extends WebElement> T create(Class<T> fragment, WebElement element) {
        ElementLocator el = new ElementLocator() {
            @Override
            public WebElement findElement() {
                return element;
            }

            @Override
            public List<WebElement> findElements() {
                return null;
            }
        };

        return create(fragment, element, el);
    }

    public static <T extends WebElement> T create(Class<T> fragment, WebElement element, ElementLocator el) {
        Class<T> proxyClass = (Class<T>) new ByteBuddy()
                .subclass(fragment)
                .implement(WrapsElement.class)
                .method(ElementMatchers.isDeclaredBy(WebElement.class)
                        .or(ElementMatchers.isDeclaredBy(WrapsElement.class))
                        .or(ElementMatchers.named("hashCode"))
                        .or(ElementMatchers.named("equals")))
                .intercept(InvocationHandlerAdapter.of((Object proxy, Method method, Object[] args) -> method.invoke(el.findElement(), args)))
                .make()
                .load(PrimeSelenium.class.getClassLoader())
                .getLoaded();

        try {
            T proxy = proxyClass.newInstance();

            WebDriver driver = WebDriverProvider.get();

            if (proxy instanceof WebDriverAware) {
                ((WebDriverAware) proxy).setWebDriver(driver);
            }

            if (proxy instanceof ElementLocatorAware) {
                ((ElementLocatorAware) proxy).setElementLocator(el);
            }

            fillMembers(driver, new DefaultElementLocatorFactory(proxy), proxy);

            return proxy;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static List<Field> collectFields(Object instance) {
        Class<?> clazz = ProxyUtils.getUnproxiedClass(instance.getClass());

        ArrayList<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        Class<?> superClazz = clazz.getSuperclass();
        while (superClazz != null && superClazz != Object.class) {
            fields.addAll(Arrays.asList(superClazz.getDeclaredFields()));

            superClazz = superClazz.getSuperclass();
        }

        return fields;
    }

    static void fillMembers(WebDriver driver, ElementLocatorFactory elf, Object obj)
            throws IllegalAccessException, InstantiationException {

        for (Field field : collectFields(obj)) {
            if (field.getAnnotation(FindBy.class) != null
                    || field.getAnnotation(FindAll.class) != null
                    || field.getAnnotation(FindBys.class) != null) {
                ElementLocator el = new LazyElementLocator(elf, field);

                Object proxy = createProxy(driver, el, field);

                field.setAccessible(true);
                field.set(obj, proxy);
            }

            FindByParentPartialId findByParentPartialId = field.getAnnotation(FindByParentPartialId.class);
            if (findByParentPartialId != null) {
                ElementLocator parentEl = null;
                if (obj instanceof ElementLocatorAware) {
                    parentEl = ((ElementLocatorAware) obj).getElementLocator();
                }

                ElementLocator el = new FindByParentPartialIdElementLocator(driver, parentEl, findByParentPartialId);

                Object proxy = createProxy(driver, el, field);

                field.setAccessible(true);
                field.set(obj, proxy);
            }
        }
    }

    static <T> T createProxy(WebDriver driver, ElementLocator el, Field field)
            throws IllegalAccessException, InstantiationException {

        if (WebElement.class.isAssignableFrom(field.getType())) {
            Class<T> proxyClass = (Class<T>) new ByteBuddy()
                    .subclass(field.getType())
                    .implement(WrapsElement.class)
                    .method(ElementMatchers.isDeclaredBy(WebElement.class)
                            .or(ElementMatchers.isDeclaredBy(WrapsElement.class))
                            .or(ElementMatchers.named("hashCode"))
                            .or(ElementMatchers.named("equals")))
                    .intercept(MethodDelegation.to(new ElementLocatorInterceptor(el)))
                    .make()
                    .load(PrimeSelenium.class.getClassLoader())
                    .getLoaded();

            T proxy = proxyClass.newInstance();

            if (proxy instanceof ElementLocatorAware) {
                ((ElementLocatorAware) proxy).setElementLocator(el);
            }

            if (proxy instanceof WebDriverAware) {
                ((WebDriverAware) proxy).setWebDriver(driver);
            }

            DefaultElementLocatorFactory delf = new DefaultElementLocatorFactory((SearchContext) proxy);

            fillMembers(driver, delf, proxy);

            return proxy;
        }

        Class<? extends WebElement> genericClass = extractGenericListType(field);
        if (genericClass != null) {
            InvocationHandler handler = new ListProxyInvocationHandler(el, genericClass);

            List<? extends WebElement> proxy;
            proxy = (List<? extends WebElement>) Proxy.newProxyInstance(
                    ProxyUtils.class.getClassLoader(), new Class[]{List.class}, handler);
            return (T) proxy;
        }

        return null;
    }

    private static Class<? extends WebElement> extractGenericListType(Field field) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return null;
        }

        if (field.getAnnotation(FindBy.class) == null
                && field.getAnnotation(FindBys.class) == null
                && field.getAnnotation(FindAll.class) == null) {
            return null;
        }

        // Type erasure in Java isn't complete. Attempt to discover the generic
        // type of the list.
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

        }

        return null;
    }
}
