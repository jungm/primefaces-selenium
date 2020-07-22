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
