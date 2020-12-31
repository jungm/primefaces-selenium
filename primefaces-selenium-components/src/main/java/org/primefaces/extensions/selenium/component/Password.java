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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;

public abstract class Password extends InputText {

    /**
     * Gets the Feedback panel showing password strength.
     *
     * @return the feedback panel
     */
    public WebElement getFeedbackPanel() {
        return getWebDriver().findElement(By.id(getId() + "_panel"));
    }

    /**
     * Brings up the panel with the password strength indicator.
     */
    public void showFeedback() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".show();");
    }

    /**
     * Hides the panel with the password strength indicator.
     */
    public void hideFeedback() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".hide();");
    }

}
