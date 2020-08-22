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
package org.primefaces.extensions.selenium.component.model.datatable;

import org.openqa.selenium.WebElement;

import java.util.List;

public class Header {

    private WebElement webElement;
    private List<HeaderCell> cells;

    public Header(WebElement webElement, List<HeaderCell> cells) {
        this.webElement = webElement;
        this.cells = cells;
    }

    public WebElement getWebElement() {
        return webElement;
    }

    public void setWebElement(WebElement webElement) {
        this.webElement = webElement;
    }

    public List<HeaderCell> getCells() {
        return cells;
    }

    public void setCells(List<HeaderCell> cells) {
        this.cells = cells;
    }

    public HeaderCell getCell(int index) {
        return getCells().get(index);
    }
}
