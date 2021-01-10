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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractInputComponent;
import org.primefaces.extensions.selenium.component.base.ComponentUtils;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;

public abstract class DatePicker extends AbstractInputComponent {

    @FindByParentPartialId("_input")
    private WebElement input;

    @Override
    public WebElement getInput() {
        return input;
    }

    public WebElement getPanel() {
        return getWebDriver().findElement(By.id(getId() + "_panel"));
    }

    public LocalDateTime getValue() {
        Object date = PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".getDate();");

        if (date == null) {
            return null;
        }

        // TODO: take timeZone - attribute into account when set; currently we always use the default ZoneId.systemDefault()

        String utcTimeString = PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".getDate().toUTCString();");

        // Parse time string and move into server-timezone
        LocalDateTime dateTime = LocalDateTime.parse(utcTimeString, DateTimeFormatter.RFC_1123_DATE_TIME);
        dateTime = LocalDateTime.ofInstant(dateTime.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());

        return dateTime;
    }

    public LocalDate getValueAsLocalDate() {
        LocalDateTime value = getValue();
        return value != null ? value.toLocalDate() : null;
    }

    public void setValue(LocalDate localDate) {
        setValue(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    public void setValue(LocalDateTime dateTime) {
        long millis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        setValue(millis);
    }

    public void setValue(long millis) {
        String formattedDate = millisAsFormattedDate(millis);

        // Emulate user input instead of using js, calendar.setDate() can't go beyond mindate/maxdate
        if (PrimeSelenium.isSafari()) {
            setDate(millis);
        }
        else {
            WebElement input = getInput();
            if (PrimeSelenium.isMacOs()) {
                input.sendKeys(Keys.chord(Keys.COMMAND, "a")); // select everything
            }
            else {
                input.sendKeys(Keys.chord(Keys.CONTROL, "a")); // select everything
            }
            input.sendKeys(formattedDate); // overwrite value
        }

        if (ComponentUtils.hasAjaxBehavior(getRoot(), "dateSelect")) {
            PrimeSelenium.guardAjax(input).sendKeys(Keys.TAB);
        }
        else {
            input.sendKeys(Keys.TAB);
        }
    }

    public String millisAsFormattedDate(long millis) {
        return PrimeSelenium.executeScript(
                    "return " + getWidgetByIdScript() + ".jq.data().primeDatePicker.formatDateTime(new Date(" + millis + "));");
    }

    public String setDate(long millis) {
        return PrimeSelenium.executeScript(
                    "return " + getWidgetByIdScript() + ".setDate(new Date(" + millis + "));");
    }

    public String updateViewDate(long millis) {
        return PrimeSelenium.executeScript(
                    "return " + getWidgetByIdScript() + ".jq.data().primeDatePicker.updateViewDate(null, new Date(" + millis + "));");
    }

    public String showPanel() {
        return PrimeSelenium.executeScript(
                    "return " + getWidgetByIdScript() + ".jq.data().primeDatePicker.showOverlay();");
    }

    public long getTimezoneOffset() {
        return (Long) PrimeSelenium.executeScript("return new Date().getTimezoneOffset();");
    }

}
