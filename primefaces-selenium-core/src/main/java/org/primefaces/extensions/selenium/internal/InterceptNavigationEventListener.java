/**
 * Copyright 2011-2019 PrimeFaces Extensions
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.primefaces.extensions.selenium.PrimeSelenium;

public class InterceptNavigationEventListener extends AbstractWebDriverEventListener {

    private static final ThreadLocal<String> LAST_UID = new ThreadLocal<>();

    private static String ajaxScript = null;

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        executeOnloadScripts();
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        executeOnloadScripts();
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        executeOnloadScripts();
    }

    @Override
    public void afterNavigateRefresh(WebDriver driver) {
        executeOnloadScripts();
    }

    public void executeOnloadScripts() {
        if (ConfigProvider.getInstance().isDisableJQueryAnimations()) {
            PrimeSelenium.disableAnimations();
        }

        if (ajaxScript == null) {
            try {
                try (BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(this.getClass().getResourceAsStream("/primefaces-selenium/ajaxguard.js"), StandardCharsets.UTF_8))) {
                    ajaxScript = buffer.lines().collect(Collectors.joining("\n"));
                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        PrimeSelenium.executeScript("(function () { " + ajaxScript + " })();");
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        String uid = getUid();
        if (uid == null || uid.isEmpty()) {
            uid = newUid();
            setUid(uid);
        }
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        // navigation happened on button click?
        String uid = getUid();
        if (!Objects.equals(uid, LAST_UID.get())) {
            executeOnloadScripts();

            uid = newUid();
            setUid(uid);
        }
    }

    public String newUid() {
        return UUID.randomUUID().toString();
    }

    public void setUid(String uid) {
        PrimeSelenium.executeScript("window.primeselenium_uid = '" + uid + "';");
        LAST_UID.set(uid);
    }

    public String getUid() {
        return PrimeSelenium.executeScript("return window.primeselenium_uid;");
    }
}
