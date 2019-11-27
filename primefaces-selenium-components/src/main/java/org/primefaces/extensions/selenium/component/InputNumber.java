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
package org.primefaces.extensions.selenium.component;

import java.io.Serializable;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.Script;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;

public abstract class InputNumber extends InputText {

    @FindByParentPartialId("_input")
    private WebElement input;

    @FindByParentPartialId("_hinput")
    private WebElement hiddenInput;

    @Override
    protected WebElement getInput() {
        return input;
    }

    protected WebElement getHiddenInput() {
        return hiddenInput;
    }

    @Override
    public void setValue(Serializable value) {
        if (value == null) {
            value = "\"\"";
        }

        PrimeSelenium.executeScript(getWidgetByIdScript() + ".setValue(" + value.toString() + ")");

        Script changeTriggerScript = () -> PrimeSelenium.executeScript(getWidgetByIdScript() + ".input.change()");
        changeTriggerScript = isOnchangeAjaxified() ? PrimeSelenium.guardAjax(changeTriggerScript) : changeTriggerScript;
        changeTriggerScript.execute();
    }

    public Double getValueToRender() {
        return Double.valueOf(PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".valueToRender;"));
    }
}
