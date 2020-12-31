/*
 * Copyright 2011-2021 PrimeFaces Extensions
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

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        return driver -> (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0;");
    }

    public static ExpectedCondition<Boolean> visibleAndAnimationComplete(WebElement element) {
        return ExpectedConditions.and(
                    jQueryNotActive(),
                    ExpectedConditions.visibilityOf(element));
    }

    public static ExpectedCondition<Boolean> invisibleAndAnimationComplete(WebElement element) {
        return ExpectedConditions.and(
                    jQueryNotActive(),
                    ExpectedConditions.invisibilityOf(element));
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
}
