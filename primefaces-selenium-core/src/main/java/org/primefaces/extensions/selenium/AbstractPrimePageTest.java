/**
 * Copyright 2011-2019 PrimeFaces Extensions
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.selenium;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.primefaces.extensions.selenium.internal.junit.BootstrapExtension;
import org.primefaces.extensions.selenium.internal.junit.PageInjectionExtension;
import org.primefaces.extensions.selenium.internal.junit.WebDriverExtension;
import org.primefaces.extensions.selenium.spi.WebDriverProvider;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(BootstrapExtension.class)
@ExtendWith(WebDriverExtension.class)
@ExtendWith(PageInjectionExtension.class)
public abstract class AbstractPrimePageTest {

    protected void assertPresent(final WebElement element) {
        if (!PrimeSelenium.isElementPresent(element)) {
            Assertions.fail("Element should be present!");
        }
    }

    protected void assertPresent(final By by) {
        if (!PrimeSelenium.isElementPresent(by)) {
            Assertions.fail("Element should be present!");
        }
    }

    protected void assertNotPresent(final WebElement element) {
        if (PrimeSelenium.isElementPresent(element)) {
            Assertions.fail("Element should not be present!");
        }
    }

    protected void assertNotPresent(final By by) {
        if (PrimeSelenium.isElementPresent(by)) {
            Assertions.fail("Element should not be present!");
        }
    }

    protected void assertDisplayed(final WebElement element) {
        if (!PrimeSelenium.isElementDisplayed(element)) {
            Assertions.fail("Element should be displayed!");
        }
    }

    protected void assertDisplayed(final By by) {
        if (!PrimeSelenium.isElementDisplayed(by)) {
            Assertions.fail("Element should be displayed!");
        }
    }

    protected void assertNotDisplayed(final WebElement element) {
        if (PrimeSelenium.isElementDisplayed(element)) {
            Assertions.fail("Element should not be displayed!");
        }
    }

    protected void assertNotDisplayed(final By by) {
        if (PrimeSelenium.isElementDisplayed(by)) {
            Assertions.fail("Element should not be displayed!");
        }
    }

    protected void assertEnabled(final WebElement element) {
        if (!PrimeSelenium.isElementEnabled(element)) {
            Assertions.fail("Element should be enabled!");
        }
    }

    protected void assertEnabled(final By by) {
        if (!PrimeSelenium.isElementEnabled(by)) {
            Assertions.fail("Element should be enabled!");
        }
    }

    protected void assertNotEnabled(final WebElement element) {
        if (PrimeSelenium.isElementEnabled(element)) {
            Assertions.fail("Element should not be enabled!");
        }
    }

    protected void assertNotEnabled(final By by) {
        if (PrimeSelenium.isElementEnabled(by)) {
            Assertions.fail("Element should not be enabled!");
        }
    }

    protected void assertDisabled(final WebElement element) {
        if (PrimeSelenium.isElementEnabled(element)) {
            Assertions.fail("Element should be disabled!");
        }
    }

    protected void assertDisabled(final By by) {
        if (PrimeSelenium.isElementEnabled(by)) {
            Assertions.fail("Element should be disabled!");
        }
    }

    protected void assertNotDisabled(final WebElement element) {
        if (!PrimeSelenium.isElementEnabled(element)) {
            Assertions.fail("Element should not be disabled!");
        }
    }

    protected void assertNotDisabled(final By by) {
        if (!PrimeSelenium.isElementEnabled(by)) {
            Assertions.fail("Element should not be disabled!");
        }
    }

    protected void assertIsAt(final AbstractPrimePage page) {
        assertIsAt(page.getLocation());
    }

    protected void assertIsAt(final Class<? extends AbstractPrimePage> pageClass) {
        final String location;
        try {
            location = PrimeSelenium.getUrl((AbstractPrimePage) pageClass.newInstance());
        }
        catch (final InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        assertIsAt(location);
    }

    protected void assertNoJavascriptErrors() {
        final LogEntries logEntries = getWebDriver().manage().logs().get(LogType.BROWSER);
        final List<LogEntry> severe = logEntries.getAll().stream()
                    .filter(l -> l.getLevel() == Level.SEVERE)
                    .collect(Collectors.toList());
        Assertions.assertTrue(severe.isEmpty());
    }

    protected void printConsole() {
        final LogEntries logEntries = getWebDriver().manage().logs().get(LogType.BROWSER);
        for (final LogEntry log : logEntries) {
            if (log.getLevel() == Level.SEVERE) {
                System.err.println(log.getMessage());
            }
            else {
                System.out.println(log.getMessage());
            }
        }
    }

    protected void assertIsAt(final String relativePath) {
        Assertions.assertTrue(getWebDriver().getCurrentUrl().contains(relativePath));
    }

    protected <T extends AbstractPrimePage> T goTo(final Class<T> pageClass) {
        return PrimeSelenium.goTo(pageClass);
    }

    protected WebDriver getWebDriver() {
        return WebDriverProvider.get();
    }
}
