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
package org.primefaces.extensions.selenium.findby;

import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.ElementLocator;

public class FindByParentPartialIdElementLocator implements ElementLocator {

    private final WebDriver driver;
    private final ElementLocator parentLocator;
    private final FindByParentPartialId annotation;

    public FindByParentPartialIdElementLocator(WebDriver driver, ElementLocator parentLocator, FindByParentPartialId annotation) {
        this.driver = driver;
        this.parentLocator = parentLocator;
        this.annotation = annotation;
    }

    @Override
    public WebElement findElement() {
        List<WebElement> elements = findElements();
        if (elements == null || elements.isEmpty()) {
            throw new NoSuchElementException("Cannot locate element using: " + annotation.value());
        }
        return elements.get(0);
    }

    @Override
    public List<WebElement> findElements() {
        WebElement parent = parentLocator.findElement();

        String parentId = parent.getAttribute("id");
        if (parentId == null || parentId.trim().isEmpty()) {
            throw new WebDriverException("Id of parent element is null or empty!");
        }

        By by;
        if (annotation.name().length() > 0) {
            by = By.name(parentId + annotation.name());
        }
        else {
            by = By.id(parentId + annotation.value());
        }
        if (annotation.searchFromRoot()) {
            return driver.findElements(by);
        }

        return parent.findElements(by);
    }
}
