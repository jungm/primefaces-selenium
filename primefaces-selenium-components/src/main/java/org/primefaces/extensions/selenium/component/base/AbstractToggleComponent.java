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
package org.primefaces.extensions.selenium.component.base;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
        return input.getAttribute("checked") != null;
    }

    /**
     * Turns this switch in case it is off, or turns of off in case it is on.
     */
    public void toggle() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".toggle();");
    }

    /**
     * Turns this switch on if it is not already turned on.
     */
    public void check() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".check();");
    }

    /**
     * Turns this switch off if it is not already turned of.
     */
    public void uncheck() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".uncheck();");
    }

}
