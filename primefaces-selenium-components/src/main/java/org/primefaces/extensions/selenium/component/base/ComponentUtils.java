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
package org.primefaces.extensions.selenium.component.base;

import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;

public final class ComponentUtils {

    private ComponentUtils() {
        // prevent instantiation
    }

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
        return PrimeSelenium.executeScript("return JSON.stringify(" + getWidgetByIdScript(id) + ".cfg, function(key, value) {\n" +
                    "  if (typeof value === 'function') {\n" +
                    "    return value.toString();\n" +
                    "  } else {\n" +
                    "    return value;\n" +
                    "  }\n" +
                    "});");
    }

    public static String getWidgetByIdScript(String id) {
        return "PrimeFaces.getWidgetById('" + id + "')";
    }

    /**
     * When using Chrome what can happen is the keys are sent too fast and the Javascript of the input can't process it fast enough. This method sends the keys
     * 1 at a time using Chrome so the input can properly process each key.
     *
     * @param input the input component to send keys to
     * @param value the value to send to the input
     */
    public static void sendKeys(WebElement input, CharSequence value) {
        if (input == null || value == null) {
            return;
        }

        // using classname here to prevent classloading issues
        if (PrimeSelenium.isChrome()) {
            // focus the input
            input.click();

            // Chrome send keys 1 at a time
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                input.sendKeys(Character.toString(c));
            }
        }
        else {
            // Firefox handles it correctly
            input.sendKeys(value);
        }
    }

}
