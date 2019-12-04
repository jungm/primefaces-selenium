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
package org.primefaces.extensions.selenium.internal.junit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;
import java.lang.reflect.Field;
import org.primefaces.extensions.selenium.AbstractPrimePage;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.spi.PrimePageFactory;
import org.primefaces.extensions.selenium.spi.WebDriverProvider;

public class PageInjectionExtension implements ParameterResolver, TestInstancePostProcessor {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        return AbstractPrimePage.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        WebDriver driver = WebDriverProvider.get(true);

        AbstractPrimePage page = PrimePageFactory.create((Class<? extends AbstractPrimePage>) parameterContext.getParameter().getType(), driver);

        PrimeSelenium.goTo(page);

        return page;
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if (field.getAnnotation(Inject.class) != null
                    && AbstractPrimePage.class.isAssignableFrom(field.getType())
                    && field.get(testInstance) == null) {

                WebDriver driver = WebDriverProvider.get(true);

                AbstractPrimePage page = PrimePageFactory.create((Class<? extends AbstractPrimePage>) field.getType(), driver);

                field.set(testInstance, page);
            }
        }
    }

}
