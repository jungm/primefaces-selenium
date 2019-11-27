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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.primefaces.extensions.selenium.AbstractPrimePage;

public class PrimePageFactory {

    private PrimePageFactory() {
    }

    public static <T extends AbstractPrimePage> T create(Class<T> page, WebDriver driver) {
        try {
            T instance = page.newInstance();

            PrimePageFragmentFactory.fillMembers(driver, new DefaultElementLocatorFactory(driver), instance);

            instance.setWebDriver(driver);

            return instance;
        }
        catch (Throwable t) {
            throw new RuntimeException("Could not create Page: " + page.getName(), t);
        }
    }
}
