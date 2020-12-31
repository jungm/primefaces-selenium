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

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractInputComponent;
import org.primefaces.extensions.selenium.component.base.ComponentUtils;

public abstract class SelectOneButton extends AbstractInputComponent {

    @FindBy(css = ".ui-button")
    private List<WebElement> options;

    @FindBy(css = ".ui-button.ui-state-active")
    private WebElement activeOption;

    public List<WebElement> getOptions() {
        return options;
    }

    public WebElement getActiveOption() {
        return activeOption;
    }

    public List<String> getOptionLabels() {
        List<String> result = new ArrayList<>();
        getOptions().forEach((element) -> result.add(element.getText()));

        return result;
    }

    public String getSelectedLabel() {
        return getActiveOption().getText();
    }

    public boolean isSelected(String label) {
        return getSelectedLabel().equalsIgnoreCase(label);
    }

    public boolean isSelected(int index) {
        return index == getOptions().indexOf(getActiveOption());
    }

    public void selectNext() {
        int activeIndex = getOptions().indexOf(getActiveOption());
        int nextIndex = activeIndex + 1;

        if (nextIndex >= getOptions().size()) {
            nextIndex = 0;
        }

        select(nextIndex);
    }

    public void select(String label) {
        if (isSelected(label)) {
            return;
        }

        for (WebElement element : getOptions()) {
            if (element.getText().equalsIgnoreCase(label)) {
                click(element);
                return;
            }
        }
    }

    public void select(int index) {
        if (index > getOptions().size()) {
            throw new IndexOutOfBoundsException("Index " + index + ", Size " + getOptions().size());
        }

        if (isSelected(index)) {
            return;
        }

        click(getOptions().get(index));
    }

    public void selectFirst() {
        select(0);
    }

    public void selectLast() {
        select(getOptions().size() - 1);
    }

    public void deselect(String label) {
        deselect(label, false);
    }

    public void deselect(String label, boolean ignoreDeselectable) {
        if (!ignoreDeselectable && !isUnselectable()) {
            return;
        }

        if (!isSelected(label)) {
            return;
        }

        for (WebElement element : getOptions()) {
            if (element.getText().equalsIgnoreCase(label)) {
                click(element);
                return;
            }
        }
    }

    public void deselect(int index) {
        deselect(index, false);
    }

    public void deselect(int index, boolean ignoreDeselectable) {
        if (index > getOptions().size()) {
            throw new IndexOutOfBoundsException("Index " + index + ", Size " + getOptions().size());
        }

        if (!ignoreDeselectable && !isUnselectable()) {
            return;
        }

        if (!isSelected(index)) {
            return;
        }

        click(getOptions().get(index));
    }

    public boolean isUnselectable() {
        return "true".equals(PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".cfg.unselectable"));
    }

    protected void click(WebElement element) {
        if (ComponentUtils.hasAjaxBehavior(getRoot(), "change") || ComponentUtils.hasAjaxBehavior(getRoot(), "onchange")) {
            PrimeSelenium.guardAjax(element).click();
        }
        else {
            element.click();
        }
    }
}
