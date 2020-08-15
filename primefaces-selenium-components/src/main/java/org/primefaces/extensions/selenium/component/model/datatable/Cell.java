package org.primefaces.extensions.selenium.component.model.datatable;

import org.openqa.selenium.WebElement;

public class Cell {

    private WebElement webElement;

    public Cell(WebElement webElement) {
        this.webElement = webElement;
    }

    public WebElement getWebElement() {
        return webElement;
    }

    public void setWebElement(WebElement webElement) {
        this.webElement = webElement;
    }

    public String getText() {
        if (webElement != null) {
            return webElement.getText();
        }
        return null;
    }
}
