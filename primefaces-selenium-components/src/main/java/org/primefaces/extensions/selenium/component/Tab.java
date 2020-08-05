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

import org.openqa.selenium.WebElement;

/**
 * Component wrapper for the PrimeFaces {@code p:tab.
 */
public class Tab {

    private String title;
    private Integer index;
    private WebElement header;
    private WebElement content;

    public Tab(String title, Integer index, WebElement header, WebElement content) {
        this.title = title;
        this.index = index;
        this.header = header;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public WebElement getHeader() {
        return header;
    }

    public void setHeader(WebElement header) {
        this.header = header;
    }

    public WebElement getContent() {
        return content;
    }

    public void setContent(WebElement content) {
        this.content = content;
    }
}
