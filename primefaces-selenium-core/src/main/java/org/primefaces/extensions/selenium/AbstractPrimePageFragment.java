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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.primefaces.extensions.selenium.internal.WebDriverAware;
import org.primefaces.extensions.selenium.internal.ElementLocatorAware;

public abstract class AbstractPrimePageFragment implements WebElement, WrapsElement, ElementLocatorAware, WebDriverAware {

    private ElementLocator elementLocator;
    private WebDriver webDriver;

    @Override
    public ElementLocator getElementLocator() {
        return elementLocator;
    }

    @Override
    public void setElementLocator(ElementLocator elementLocator) {
        this.elementLocator = elementLocator;
    }

    @Override
    public WebDriver getWebDriver() {
        return webDriver;
    }

    @Override
    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public WebElement getWrappedElement() {
        return getElementLocator().findElement();
    }

    public WebElement getRoot() {
        return elementLocator.findElement();
    }

    public String getId() {
        return getRoot().getAttribute("id");
    }

}
