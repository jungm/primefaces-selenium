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
package org.primefaces.extensions.selenium.internal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class OnloadScriptsEventListener extends AbstractWebDriverEventListener {

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        OnloadScripts.execute();
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        OnloadScripts.execute();
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        OnloadScripts.execute();
    }

    @Override
    public void afterNavigateRefresh(WebDriver driver) {
        OnloadScripts.execute();
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        OnloadScripts.execute();
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        OnloadScripts.execute();
    }
}
