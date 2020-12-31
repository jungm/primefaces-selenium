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
package org.primefaces.extensions.selenium.component.base;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;

public abstract class AbstractInputComponent extends AbstractComponent {

    public WebElement getInput() {
        return getRoot();
    }

    @Override
    public boolean isEnabled() {
        return getInput().isEnabled();
    }

    public WebElement getAssignedLabel() {
        return getWebDriver().findElement(By.cssSelector("label[for='" + getInput().getAttribute("id") + "']"));
    }

    public String getAssignedLabelText() {
        return getAssignedLabel().getText();
    }

    /**
     * Copy the current value in the Input to the clipboard.
     *
     * @return the value copied to the clipboard
     */
    public String copyToClipboard() {
        WebElement input = getInput();
        Keys command = PrimeSelenium.isMacOs() ? Keys.COMMAND : Keys.CONTROL;
        input.sendKeys(Keys.chord(command, "a")); // select everything
        input.sendKeys(Keys.chord(command, "c")); // copy
        return input.getAttribute("value");
    }

    /**
     * Paste the current value of the clipboard to the Input.
     *
     * @return the value pasted into the input
     */
    public String pasteFromClipboard() {
        WebElement input = getInput();
        Keys command = PrimeSelenium.isMacOs() ? Keys.COMMAND : Keys.CONTROL;
        input.sendKeys(Keys.chord(command, "a")); // select everything
        input.sendKeys(Keys.chord(command, "v")); // paste
        return input.getAttribute("value");
    }

}
