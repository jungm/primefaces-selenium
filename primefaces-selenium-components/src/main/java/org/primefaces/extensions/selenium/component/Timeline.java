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
package org.primefaces.extensions.selenium.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeExpectedConditions;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractComponent;

/**
 * Component wrapper for the PrimeFaces {@code p:timeline}.
 */
public abstract class Timeline extends AbstractComponent {

    /**
     * Selects either an event or any other element in the timeline by its CSS class.
     *
     * @param cssClass the CSS class to select
     */
    public void select(String cssClass) {
        PrimeSelenium.waitGui().until(PrimeExpectedConditions.visibleAndAnimationComplete(this));
        WebElement element = findElement(By.className(cssClass));
        PrimeSelenium.guardAjax(element).click();
    }

    /**
     * Force render the timeline component.
     */
    public void update() {
        PrimeSelenium.waitGui().until(PrimeExpectedConditions.visibleAndAnimationComplete(this));
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
