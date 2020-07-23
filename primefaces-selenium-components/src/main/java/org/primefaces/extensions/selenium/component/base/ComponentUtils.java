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
package org.primefaces.extensions.selenium.component.base;

import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;

public class ComponentUtils {

    public static boolean hasAjaxBehavior(WebElement element, String behavior) {
        if (!hasBehavior(element, behavior)) {
            return false;
        }

        String id = element.getAttribute("id");

        String result = PrimeSelenium.executeScript("return " + getWidgetByIdScript(id) + ".getBehavior('" + behavior + "').toString();");
        return isAjaxScript(result);
    }

    public static boolean hasBehavior(WebElement element, String behavior) {
        if (!isWidget(element)) {
            return false;
        }

        String id = element.getAttribute("id");

        return PrimeSelenium.executeScript("return " + getWidgetByIdScript(id) + ".hasBehavior('" + behavior + "');");
    }

    public static boolean isWidget(WebElement element) {
        String id = element.getAttribute("id");
        if (id == null || id.isEmpty()) {
            return false;
        }

        return PrimeSelenium.executeScript("return " + getWidgetByIdScript(id) + " != null;");
    }

    public static boolean isAjaxScript(String script) {
        if (script == null || script.isEmpty()) {
            return false;
        }

        return script.contains("PrimeFaces.ab(") || script.contains("pf.ab(") || script.contains("mojarra.ab(") || script.contains("jsf.ajax.request");
    }

    public static String getWidgetConfiguration(WebElement element) {
        String id = element.getAttribute("id");
        return PrimeSelenium.executeScript("return JSON.stringify(" + getWidgetByIdScript(id) + ".cfg);");
    }

    public static String getWidgetByIdScript(String id) {
        return "PrimeFaces.getWidgetById('" + id + "')";
    }

}
