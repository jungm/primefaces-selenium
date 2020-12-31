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
import org.primefaces.extensions.selenium.component.base.AbstractComponent;

public abstract class Schedule extends AbstractComponent {

    /**
     * Selects either a date or event or any other element in the schedule by its CSS class.
     *
     * @param cssClass the CSS class to select
     */
    public void select(String cssClass) {
        WebElement element = findElement(By.className(cssClass));
        PrimeSelenium.guardAjax(element).click();
    }

    /**
     * Updates and refreshes the schedule view refetching all events
     */
    public void update() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".update();");
    }

    public WebElement getTodayButton() {
        return getButton("fc-today-button");
    }

    public WebElement getMonthButton() {
        return getButton("fc-dayGridMonth-button");
    }

    public WebElement getWeekButton() {
        return getButton("fc-timeGridWeek-button");
    }

    public WebElement getDayButton() {
        return getButton("fc-timeGridDay-button");
    }

    public WebElement getButton(String buttonClass) {
        return findElement(By.className(buttonClass));
    }

}
