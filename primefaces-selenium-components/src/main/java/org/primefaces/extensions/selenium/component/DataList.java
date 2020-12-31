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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.primefaces.extensions.selenium.component.base.AbstractPageableData;

public abstract class DataList extends AbstractPageableData {

    @FindBy(className = "ui-datalist-content")
    private WebElement content;

    @Override
    public List<WebElement> getRowsWebElement() {
        return content.findElements(By.className("ui-datalist-item"));
    }

    public WebElement getRowWebElement(int index) {
        return getRowsWebElement().get(index);
    }

}
