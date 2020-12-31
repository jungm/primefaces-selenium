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
package org.primefaces.extensions.selenium.component;

import java.io.Serializable;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;

/**
 * Component wrapper for the PrimeFaces {@code p:spinner}.
 */
public abstract class Spinner extends InputText {

    @FindByParentPartialId("_input")
    private WebElement input;

    @FindBy(css = ".ui-spinner-up")
    private WebElement buttonUp;

    @FindBy(css = ".ui-spinner-down")
    private WebElement buttonDown;

    @Override
    public WebElement getInput() {
        return input;
    }

    /**
     * Gets the Spinner's Up button.
     *
     * @return the {@link WebElement} representing the up button
     */
    public WebElement getButtonUp() {
        return buttonUp;
    }

    /**
     * Gets the Spinner's Down button.
     *
     * @return the {@link WebElement} representing the down button
     */
    public WebElement getButtonDown() {
        return buttonDown;
    }

    @Override
    public void setValue(Serializable value) {
        if (value == null) {
            value = "\"\"";
        }

        PrimeSelenium.executeScript(getWidgetByIdScript() + ".setValue(" + value.toString() + ")");
    }

    /**
     * Increments this spinner by one SpinnerCfg.step
     */
    public void increment() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".spin(1);");
    }

    /**
     * Decrements this spinner by one SpinnerCfg.step
     */
    public void decrement() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".spin(-1);");
    }
}
