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

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractInputComponent;
import org.primefaces.extensions.selenium.component.base.ComponentUtils;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;

/**
 * Component wrapper for the PrimeFaces {@code p:calendar}.
 */
public abstract class Calendar extends AbstractInputComponent {

    @FindByParentPartialId("_input")
    private WebElement input;

    public LocalDateTime getValue() {
        Object date = PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".getDate()");

        if (date == null) {
            return null;
        }

        long timeZoneOffset = PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".getDate().getTimezoneOffset()");
        String utcTimeString = PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".getDate().toUTCString();");

        // Parse time string and subtract the timezone offset
        return LocalDateTime.parse(utcTimeString, DateTimeFormatter.RFC_1123_DATE_TIME).minusMinutes(timeZoneOffset);
    }

    public LocalDate getValueAsLocalDate() {
        LocalDateTime value = getValue();
        return value != null ? value.toLocalDate() : null;
    }

    public void setValue(LocalDate localDate) {
        setValue(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    public void setValue(LocalDateTime dateTime) {
        int timezoneOffset = (int) getTimezoneOffset();
        int timezoneOffsetHours = timezoneOffset / 60;
        int timezoneOffsetMinutes = timezoneOffset % 60;

        ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(timezoneOffsetHours, timezoneOffsetMinutes);

        long millis = dateTime.atOffset(zoneOffset).toInstant().toEpochMilli();

        setValue(millis);
    }

    public void setValue(long millis) {
        String formattedDate = millisAsFormattedDate(millis);

        // Emulate user input instead of using js, calendar.setDate() can't go beyond mindate/maxdate
        getInput().sendKeys(Keys.chord(Keys.CONTROL, "a")); // select everything
        getInput().sendKeys(formattedDate); // overwrite value

        if (ComponentUtils.hasAjaxBehavior(getRoot(), "dateSelect")) {
            PrimeSelenium.guardAjax(getInput()).sendKeys(Keys.TAB);
        }
        else {
            getInput().sendKeys(Keys.TAB);
        }
    }

    public String millisAsFormattedDate(long millis) {
        return PrimeSelenium.executeScript(
                    "return $.datepicker.formatDate(" + getWidgetByIdScript() + ".cfg.dateFormat, new Date(" + millis + "));");
    }

    public long getTimezoneOffset() {
        return (Long) PrimeSelenium.executeScript("return new Date().getTimezoneOffset();");
    }

    @Override
    public WebElement getInput() {
        return input;
    }
}
