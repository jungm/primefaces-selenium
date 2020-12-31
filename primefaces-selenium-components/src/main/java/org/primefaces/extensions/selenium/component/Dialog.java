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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractComponent;

public abstract class Dialog extends AbstractComponent {

    @FindBy(className = "ui-dialog-content")
    private WebElement content;

    @FindBy(className = "ui-dialog-title")
    private WebElement title;

    public void show() {
        PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".show();");
    }

    public void hide() {
        PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".hide();");
    }

    public WebElement getContent() {
        return content;
    }

    public String getTitle() {
        return title.getText();
    }
}
