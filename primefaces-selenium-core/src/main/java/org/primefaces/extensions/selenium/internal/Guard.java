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
package org.primefaces.extensions.selenium.internal;

import java.lang.reflect.InvocationHandler;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.bytebuddy.matcher.ElementMatcher;
import org.openqa.selenium.TimeoutException;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.spi.WebDriverProvider;

public class Guard {

    private Guard() {

    }

    public static <T> T http(T target) {
        return proxy(target, (Object p, Method method, Object[] args) -> {
            try {
                Object result = method.invoke(target, args);

                PrimeSelenium.waitDocumentLoad();

                return result;
            }
            catch (TimeoutException e) {
                throw new TimeoutException("Timeout while waiting for document ready!", e);
            }
        });
    }

    public static <T> T ajax(T target) {
        OnloadScripts.execute();

        return proxy(target, (Object p, Method method, Object[] args) -> {
            try {
                PrimeSelenium.executeScript("pfselenium.xhr = 'somethingJustNotNull';");

                Object result = method.invoke(target, args);

                WebDriver driver = WebDriverProvider.get();

                WebDriverWait wait = new WebDriverWait(driver, ConfigProvider.getInstance().getAjaxTimeout(), 100);
                wait.until(d -> {
                    return (Boolean) ((JavascriptExecutor) driver)
                            .executeScript("return document.readyState === 'complete'"
                                    + " && (!window.jQuery || jQuery.active == 0)"
                                    + " && (!window.pfselenium || (pfselenium.xhr === null && pfselenium.navigating === false));");
                });

                return result;
            }
            catch (TimeoutException e) {
                throw new TimeoutException("Timeout while waiting for AJAX complete!", e);
            }
        });
    }

    private static <T> T proxy(T target, InvocationHandler handler) {
        Class<?> classToProxy = target.getClass();
        List<Class> interfacesToImplement = new ArrayList<>();
        ElementMatcher.Junction methods = ElementMatchers.isPublic();

        // class is not proxyable - lets try to implement interfaces
        if (Modifier.isPrivate(classToProxy.getModifiers()) || Modifier.isFinal(classToProxy.getModifiers())) {
            interfacesToImplement = Arrays.asList(classToProxy.getInterfaces());
            classToProxy = Object.class;
            methods = null;

            for (Class c : interfacesToImplement) {
                if (methods == null) {
                    methods = ElementMatchers.isDeclaredBy(c);
                }
                else {
                    methods = methods.or(ElementMatchers.isDeclaredBy(c));
                }
            }
        }

        Class<T> proxyClass = new ByteBuddy()
                .subclass(classToProxy)
                .implement(interfacesToImplement)
                .method(methods)
                .intercept(InvocationHandlerAdapter.of(handler))
                .make()
                .load(target.getClass().getClassLoader())
                .getLoaded();

        try {
            return proxyClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
