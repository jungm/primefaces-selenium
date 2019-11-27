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
package org.primefaces.extensions.selenium;

import org.openqa.selenium.WebElement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.primefaces.extensions.selenium.internal.junit.AdapterExtension;
import org.primefaces.extensions.selenium.internal.junit.PageExtension;
import org.primefaces.extensions.selenium.internal.junit.WebDriverExtension;
import org.primefaces.extensions.selenium.spi.WebDriverProvider;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(AdapterExtension.class)
@ExtendWith(WebDriverExtension.class)
@ExtendWith(PageExtension.class)
public abstract class AbstractPrimePageTest {

    protected void assertPresent(WebElement element) {
        if (!PrimeSelenium.isElementPresent(element)) {
            Assertions.fail("Element should be present!");
        }
    }

    protected void assertPresent(By by) {
        if (!PrimeSelenium.isElementPresent(by)) {
            Assertions.fail("Element should be present!");
        }
    }

    protected void assertNotPresent(WebElement element) {
        if (PrimeSelenium.isElementPresent(element)) {
            Assertions.fail("Element should not be present!");
        }
    }

    protected void assertNotPresent(By by) {
        if (PrimeSelenium.isElementPresent(by)) {
            Assertions.fail("Element should not be present!");
        }
    }

    protected void assertDisplayed(WebElement element) {
        if (!PrimeSelenium.isElementDisplayed(element)) {
            Assertions.fail("Element should be displayed!");
        }
    }

    protected void assertDisplayed(By by) {
        if (!PrimeSelenium.isElementDisplayed(by)) {
            Assertions.fail("Element should be displayed!");
        }
    }

    protected void assertNotDisplayed(WebElement element) {
        if (PrimeSelenium.isElementDisplayed(element)) {
            Assertions.fail("Element should not be displayed!");
        }
    }

    protected void assertNotDisplayed(By by) {
        if (PrimeSelenium.isElementDisplayed(by)) {
            Assertions.fail("Element should not be displayed!");
        }
    }

    protected void assertEnabled(WebElement element) {
        if (!PrimeSelenium.isElementEnabled(element)) {
            Assertions.fail("Element should be enabled!");
        }
    }

    protected void assertEnabled(By by) {
        if (!PrimeSelenium.isElementEnabled(by)) {
            Assertions.fail("Element should be enabled!");
        }
    }

    protected void assertNotEnabled(WebElement element) {
        if (PrimeSelenium.isElementEnabled(element)) {
            Assertions.fail("Element should not be enabled!");
        }
    }

    protected void assertNotEnabled(By by) {
        if (PrimeSelenium.isElementEnabled(by)) {
            Assertions.fail("Element should not be enabled!");
        }
    }

    protected void assertDisabled(WebElement element) {
        if (PrimeSelenium.isElementEnabled(element)) {
            Assertions.fail("Element should be disabled!");
        }
    }

    protected void assertDisabled(By by) {
        if (PrimeSelenium.isElementEnabled(by)) {
            Assertions.fail("Element should be disabled!");
        }
    }

    protected void assertNotDisabled(WebElement element) {
        if (!PrimeSelenium.isElementEnabled(element)) {
            Assertions.fail("Element should not be disabled!");
        }
    }

    protected void assertNotDisabled(By by) {
        if (!PrimeSelenium.isElementEnabled(by)) {
            Assertions.fail("Element should not be disabled!");
        }
    }

    protected void assertIsAt(AbstractPrimePage page) {
        assertIsAt(page.getLocation());
    }

    protected void assertIsAt(Class<?> pageClass) {
        String location;
        try {
            location = PrimeSelenium.getUrl((AbstractPrimePage) pageClass.newInstance());
        }
        catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        assertIsAt(location);
    }

    protected void assertIsAt(String relativePath) {
        Assertions.assertTrue(getWebDriver().getCurrentUrl().contains(relativePath));
    }

    protected <T extends AbstractPrimePage> T goTo(Class<T> pageClass) {
        return PrimeSelenium.goTo(pageClass);
    }

    protected WebDriver getWebDriver() {
        return WebDriverProvider.get();
    }
}
