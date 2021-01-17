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
package org.primefaces.extensions.selenium.component.base;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;

public abstract class AbstractInputComponent extends AbstractComponent {

    public WebElement getInput() {
        return getRoot();
    }

    public boolean isOnchangeAjaxified() {
        return isAjaxified(getInput(), "onchange") || ComponentUtils.hasAjaxBehavior(getRoot(), "change");
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
