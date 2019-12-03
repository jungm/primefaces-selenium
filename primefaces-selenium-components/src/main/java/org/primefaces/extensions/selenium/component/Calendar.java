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

import java.time.LocalDate;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractInputComponent;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.component.base.ComponentUtils;
import org.primefaces.extensions.selenium.findby.FindByParentPartialId;

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

        //Parse time string and subtract the timezone offset
        return LocalDateTime.parse(utcTimeString, DateTimeFormatter.RFC_1123_DATE_TIME).minusMinutes(timeZoneOffset);
    }

    public void setValue(LocalDate localDate) {
        setValue(localDate.atStartOfDay());
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
        getInput().sendKeys(formattedDate); //overwrite value

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
