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

import java.util.List;

import org.primefaces.extensions.selenium.PrimeSelenium;

public class OnloadScripts {

    private OnloadScripts() {

    }

    public static void execute() {
        if (isInstalled()) {
            return;
        }

        List<String> onloadScripts = ConfigProvider.getInstance().getOnloadScripts();
        PrimeSelenium.executeScript("(function () { " + String.join(";", onloadScripts) + " })();");
    }

    private static boolean isInstalled() {
        PrimeSelenium.waitDocumentLoad();

        return PrimeSelenium.executeScript("return window.pfselenium != null;");
    }
}
