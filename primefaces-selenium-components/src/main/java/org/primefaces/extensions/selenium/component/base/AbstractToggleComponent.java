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
package org.primefaces.extensions.selenium.component.base;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.primefaces.extensions.selenium.PrimeExpectedConditions;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;

/**
 * Base class for boolean toggle components.
 */
public abstract class AbstractToggleComponent extends AbstractInputComponent {

    @FindByParentPartialId("_input")
    private WebElement input;

    @Override
    public WebElement getInput() {
        return input;
    }

    @Override
    public void click() {
        PrimeSelenium.waitGui().until(ExpectedConditions.elementToBeClickable(getRoot()));

        if (ComponentUtils.hasAjaxBehavior(getRoot(), "change")) {
            PrimeSelenium.guardAjax(getRoot()).click();
        }
        else {
            getRoot().click();
        }
    }

    public void setValue(boolean value) {
        if (getValue() != value) {
            click();
        }
    }

    public boolean getValue() {
        return getInput().getAttribute("checked") != null;
    }

    @Override
    public boolean isSelected() {
        return getValue();
    }

    /**
     * Turns this switch in case it is off, or turns of off in case it is on.
     */
    public void toggle() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".toggle();");
        PrimeSelenium.waitGui().until(PrimeExpectedConditions.documentLoaded());
        PrimeSelenium.waitGui().until(PrimeExpectedConditions.jQueryNotActive());
    }

    /**
     * Turns this switch on if it is not already turned on.
     */
    public void check() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".check();");
        PrimeSelenium.waitGui().until(PrimeExpectedConditions.documentLoaded());
        PrimeSelenium.waitGui().until(PrimeExpectedConditions.jQueryNotActive());
    }

    /**
     * Turns this switch off if it is not already turned of.
     */
    public void uncheck() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".uncheck();");
        PrimeSelenium.waitGui().until(PrimeExpectedConditions.documentLoaded());
        PrimeSelenium.waitGui().until(PrimeExpectedConditions.jQueryNotActive());
    }

}
