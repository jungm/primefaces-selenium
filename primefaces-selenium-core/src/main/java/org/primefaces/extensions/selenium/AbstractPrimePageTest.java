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
package org.primefaces.extensions.selenium;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.primefaces.extensions.selenium.internal.junit.BootstrapExtension;
import org.primefaces.extensions.selenium.internal.junit.PageInjectionExtension;
import org.primefaces.extensions.selenium.internal.junit.WebDriverExtension;
import org.primefaces.extensions.selenium.spi.WebDriverProvider;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(BootstrapExtension.class)
@ExtendWith(WebDriverExtension.class)
@ExtendWith(PageInjectionExtension.class)
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

    protected void assertIsAt(Class<? extends AbstractPrimePage> pageClass) {
        String location;
        try {
            location = PrimeSelenium.getUrl((AbstractPrimePage) pageClass.newInstance());
        }
        catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        assertIsAt(location);
    }

    /**
     * Checks the browse console and asserts there are no SEVERE level messages.
     */
    protected void assertNoJavascriptErrors() {
        LogEntries logEntries = getLogsForType(LogType.BROWSER);
        if (logEntries == null) {
            return;
        }
        List<LogEntry> severe = logEntries.getAll().stream()
                    .filter(l -> l.getLevel() == Level.SEVERE)
                    .collect(Collectors.toList());
        Assertions.assertTrue(severe.isEmpty(), "Javascript errors were detected in the browser console.\r\n" + severe.toString());
    }

    /**
     * Dumps to System.out or System.err any messages found in the browser console.
     */
    protected void printConsole() {
        LogEntries logEntries = getLogsForType(LogType.BROWSER);
        if (logEntries == null) {
            return;
        }
        for (LogEntry log : logEntries) {
            if (log.getLevel() == Level.SEVERE) {
                System.err.println(log.getMessage());
            }
            else {
                System.out.println(log.getMessage());
            }
        }
    }

    /**
     * Utility method for checking the browser console for a specific type of message.
     *
     * @param type the {@link LogType} you are searching for
     * @return either NULL if not available or the {@link LogEntries}
     */
    protected LogEntries getLogsForType(String type) {
        // Firefox does not support https://github.com/mozilla/geckodriver/issues/284
        if (PrimeSelenium.isFirefox()) {
            return null;
        }
        Logs logs = getWebDriver().manage().logs();
        if (logs == null) {
            return null;
        }
        Set<String> types = logs.getAvailableLogTypes();
        if (!types.contains(LogType.BROWSER)) {
            return null;
        }
        return logs.get(type);
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
