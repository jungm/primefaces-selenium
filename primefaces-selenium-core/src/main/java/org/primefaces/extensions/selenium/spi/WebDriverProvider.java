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
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.primefaces.extensions.selenium.internal.ConfigProvider;
import org.primefaces.extensions.selenium.internal.OnloadScriptsEventListener;

public class WebDriverProvider {

    private static final ThreadLocal<WebDriver> WEB_DRIVER = new ThreadLocal<>();

    public static void set(WebDriver driver) {
        WEB_DRIVER.set(driver);
    }

    public static WebDriver get() {
        return get(false);
    }

    public static WebDriver get(boolean create) {
        WebDriver driver = WEB_DRIVER.get();
        if (driver == null && create) {
            PrimeSeleniumAdapter adapter = ConfigProvider.getInstance().getAdapter();

            driver = adapter.createWebDriver();

            EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
            eventDriver.register(new OnloadScriptsEventListener());

            set(eventDriver);

            driver = eventDriver;
        }
        return driver;
    }
}
