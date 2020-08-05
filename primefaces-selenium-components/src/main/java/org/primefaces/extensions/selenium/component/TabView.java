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
import org.openqa.selenium.support.FindBy;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractComponent;
import org.primefaces.extensions.selenium.component.base.ComponentUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Component wrapper for the PrimeFaces {@code p:tabView}.
 */
public abstract class TabView extends AbstractComponent {

    @FindBy(css = ".ui-tabs-header")
    private List<WebElement> headers;

    @FindBy(css = ".ui-tabs-panel")
    private List<WebElement> contents;

    public List<WebElement> getHeaders() {
        return headers;
    }

    public List<Tab> getTabs() {
        //TODO: maybe do this only once instead of each time
        //TODO: deprecate/remove some of the old/existing methods
        //TODO: add getTabs to AccordionPanel

        List<Tab> tabs = new ArrayList<>();

        headers.stream().forEach(headerElt -> {
            String title = headerElt.findElement(By.tagName("a")).getText();
            WebElement header = headerElt;
            int index = Integer.parseInt(headerElt.getAttribute("data-index"));
            WebElement content = contents.get(index);

            tabs.add(new Tab(title, index, header, content));
        });

        return tabs;
    }

    /**
     * Toggle the tab denoted by the specified index.
     *
     * @param index the index of the tab to expand
     */
    public void toggleTab(int index) {
        if (ComponentUtils.hasAjaxBehavior(getRoot(), "tabChange")) {
            PrimeSelenium.guardAjax(headers.get(index)).click();
        }
        else {
            headers.get(index).click();
        }
    }

    /**
     * Provides the header of an {@link TabView} tab at the specified index.
     *
     * @param index the index
     * @return the header of the {@link TabView} tab
     */
    public String getTabHeader(int index) {
        return headers.get(index).getText();
    }

    /**
     * Provides the headers of the {@link TabView} tabs in their order.
     *
     * @return a copy of the headers in order
     */
    public List<String> getTabHeaders() {
        return headers.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
    }

    /**
     * Provides the header of the active (selected) {@link TabView} tab.
     *
     * @return  the header of the {@link TabView} active (selected) tab
     */
    public String getActiveTabHeader() {
        return this.findElement(new By.ByClassName("ui-tabs-selected")).getText();
    }
}
