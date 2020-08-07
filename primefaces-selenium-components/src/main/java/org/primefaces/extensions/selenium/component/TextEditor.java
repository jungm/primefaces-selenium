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
package org.primefaces.extensions.selenium.component;

import java.io.Serializable;

import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;

public abstract class TextEditor extends InputText {

    @FindByParentPartialId(value = "_input", name = "_input")
    private WebElement input;

    @FindByParentPartialId("_editor")
    private WebElement editor;

    @FindByParentPartialId("_toolbar")
    private WebElement toolbar;

    @Override
    public WebElement getInput() {
        return input;
    }

    public WebElement getEditor() {
        return editor;
    }

    public WebElement getToolbar() {
        return toolbar;
    }

    /**
     * Finds an returns the current contents of the editor.
     *
     * @return The current contents of the editor, as an HTML string.
     */
    public String getEditorValue() {
        return PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".getEditorValue();");
    }

    @Override
    public void setValue(Serializable value) {
        if (value == null) {
            value = "\"\"";
        }

        PrimeSelenium.executeScript(getWidgetByIdScript() + ".editor.setText('" + value.toString() + "');");
    }

    /**
     * Clears the entire text of the editor.
     */
    @Override
    public void clear() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".clear();");
    }

    /**
     * Enables this text editor so that text can be entered.
     */
    public void enable() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".enable();");
    }

    /**
     * Disables this text editor so that no text can be entered or removed.
     */
    public void disable() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".disable();");
    }
}
