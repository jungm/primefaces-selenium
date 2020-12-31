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
import org.primefaces.extensions.selenium.PrimeSelenium;

public abstract class InputMask extends InputText {

    @Override
    public void setValue(Serializable value) {
        WebElement input = getInput();
        input.clear();
        setWidgetValue(value.toString());
    }

    /**
     * Client side widget method to set the current value.
     *
     * @param value the value to set the input to
     */
    public void setWidgetValue(Serializable value) {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".setValue('" + value + "');");
    }

    /**
     * Client side widget method to get the current value.
     *
     * @return the current value
     */
    public String getWidgetValue() {
        return PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".getValue();");
    }

    /**
     * Client side widget method to get the unmasked value.
     *
     * @return the unmasked value
     * @since 9.0
     */
    public String getWidgetValueUnmasked() {
        return PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".getValueUnmasked();");
    }
}
