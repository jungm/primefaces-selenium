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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractComponent;

public abstract class Timeline extends AbstractComponent {

    /**
     * Selects either an event or any other element in the timeline by its CSS class.
     *
     * @param cssClass the CSS class to select
     */
    public void select(String cssClass) {
        WebElement element = findElement(By.className(cssClass));
        PrimeSelenium.guardAjax(element).click();
    }

    /**
     * Force render the timeline component.
     */
    public void update() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".renderTimeline();");
    }

    /**
     * Gets number of events (items in the timeline).
     *
     * @return The number of event in the timeline.
     */
    public long getNumberOfEvents() {
        return PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".getNumberOfEvents();");
    }
}
