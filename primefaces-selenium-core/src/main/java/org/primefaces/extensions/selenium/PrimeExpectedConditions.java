/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class PrimeExpectedConditions {

    private PrimeExpectedConditions() {
        super();
    }

    public static ExpectedCondition<Boolean> documentLoaded() {
        return driver -> (Boolean) ((JavascriptExecutor) driver).executeScript("return document.readyState === 'complete'");
    }

    public static ExpectedCondition<Boolean> jQueryNotActive() {
        return driver -> (Boolean) ((JavascriptExecutor) driver).executeScript("return (!window.jQuery || jQuery.active == 0);");
    }

    public static ExpectedCondition<Boolean> ajaxQueueEmpty() {
        return driver -> (Boolean) ((JavascriptExecutor) driver).executeScript("return (!window.PrimeFaces || PrimeFaces.ajax.Queue.isEmpty());");
    }

    public static ExpectedCondition<Boolean> elementToBeClickable(WebElement element) {
        return ExpectedConditions.and(ExpectedConditions.elementToBeClickable(element));
    }

    public static ExpectedCondition<Boolean> visibleAndAnimationComplete(WebElement element) {
        return ExpectedConditions.and(
                    documentLoaded(),
                    jQueryNotActive(),
                    ajaxQueueEmpty(),
                    steadinessOfElementLocated(element, true));
    }

    public static ExpectedCondition<Boolean> invisibleAndAnimationComplete(WebElement element) {
        return ExpectedConditions.and(
                    documentLoaded(),
                    jQueryNotActive(),
                    ajaxQueueEmpty(),
                    steadinessOfElementLocated(element, false));
    }

    public static ExpectedCondition<Boolean> visibleInViewport(WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return (Boolean) PrimeSelenium.executeScript(
                            "var elem = arguments[0],"
                                        + "    box = elem.getBoundingClientRect(),"
                                        + "    cx = box.left + box.width / 2,"
                                        + "    cy = box.top + box.height / 2,"
                                        + "    e = document.elementFromPoint(cx, cy);"
                                        + "for (; e; e = e.parentElement) {"
                                        + "    if (e === elem) { return true; }"
                                        + "}"
                                        + "return false;",
                            element);
            }

            @Override
            public String toString() {
                return "is " + element + " visible in viewport";
            }
        };
    }

    /**
     * For CSS animations keeps checking the location of an element.
     *
     * @param element the WebElement to check
     * @param isDisplayed true if final state should be displayed false if hidden
     * @return true if element is now steady false if not found
     * @see <a href="https://stackoverflow.com/questions/39245064/wait-for-animated-button-to-stop/39247375#39247375">Stack Overflow</a>
     */
    public static ExpectedCondition<WebElement> steadinessOfElementLocated(final WebElement element, boolean isDisplayed) {
        return new ExpectedCondition<WebElement>() {

            private WebElement _element = null;
            private Point _location = null;

            @Override
            public WebElement apply(WebDriver driver) {
                if (_element == null) {
                    try {
                        if (element.getAttribute("id") != null) {
                            _element = driver.findElement(By.id(element.getAttribute("id")));
                        }
                        else if (element.getAttribute("name") != null) {
                            _element = driver.findElement(By.name(element.getAttribute("name")));
                        }
                        else {
                            _element = driver.findElement(By.className(element.getAttribute("class")));
                        }
                    }
                    catch (NoSuchElementException e) {
                        return null;
                    }
                }

                try {
                    if (_element.isDisplayed() == isDisplayed) {
                        Point location = _element.getLocation();
                        if (location.equals(_location)) {
                            return _element;
                        }
                        _location = location;
                    }
                }
                catch (StaleElementReferenceException e) {
                    _element = null;
                }

                return null;
            }

            @Override
            public String toString() {
                return "steadiness of element located by " + element;
            }
        };
    }

    public static ExpectedCondition<Boolean> isOnTop(WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return (Boolean) PrimeSelenium.executeScript(
                            "var elm = arguments[0];" +
                                        "var doc = elm.ownerDocument || document;" +
                                        "var rect = elm.getBoundingClientRect();" +
                                        "return elm === doc.elementFromPoint(rect.left + (rect.width / 2), rect.top + (rect.height / 2));",
                            element);
            }

            @Override
            public String toString() {
                return "is " + element + " on top";
            }
        };
    }
}
