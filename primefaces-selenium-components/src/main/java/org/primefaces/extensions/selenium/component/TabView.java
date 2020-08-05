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
import org.primefaces.extensions.selenium.component.model.Tab;

import java.util.ArrayList;
import java.util.List;

/**
 * Component wrapper for the PrimeFaces {@code p:tabView}.
 */
public abstract class TabView extends AbstractComponent {

    @FindBy(css = ".ui-tabs-header")
    private List<WebElement> headers;

    @FindBy(css = ".ui-tabs-panel")
    private List<WebElement> contents;

    private List<Tab> tabs = null;

    public List<Tab> getTabs() {
        //TODO: add getTabs to AccordionPanel

        if (tabs == null) {
            List<Tab> tabs = new ArrayList<>();

            headers.stream().forEach(headerElt -> {
                String title = headerElt.findElement(By.tagName("a")).getText();
                WebElement header = headerElt;
                int index = getIndexOfHeader(headerElt);
                WebElement content = contents.get(index);

                tabs.add(new Tab(title, index, header, content));
            });

            this.tabs = tabs;
        }

        return this.tabs;
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
     * Provides the active (selected) {@link TabView} tab.
     *
     * @return  the active (selected) tab
     */
    public Tab getActiveTab() {
        WebElement selectedTabHeader = this.findElement(new By.ByClassName("ui-tabs-selected"));
        int index = getIndexOfHeader(selectedTabHeader);

        return getTabs().get(index);
    }

    private Integer getIndexOfHeader(WebElement headerElt) {
        return Integer.parseInt(headerElt.getAttribute("data-index"));
    }
}
