package org.primefaces.extensions.selenium.component.model.data;

import org.openqa.selenium.WebElement;

public class Page {

    private WebElement webElement;
    private int number;

    public Page(int number, WebElement webElement) {
        this.number = number;
        this.webElement = webElement;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public WebElement getWebElement() {
        return webElement;
    }

    public void setWebElement(WebElement webElement) {
        this.webElement = webElement;
    }

}
