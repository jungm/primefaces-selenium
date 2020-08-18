package org.primefaces.extensions.selenium.component.model.datatable;

import org.openqa.selenium.WebElement;

import java.util.List;

public class Header {

    private WebElement webElement;
    private List<HeaderCell> cells;

    public Header(WebElement webElement, List<HeaderCell> cells) {
        this.webElement = webElement;
        this.cells = cells;
    }

    public WebElement getWebElement() {
        return webElement;
    }

    public void setWebElement(WebElement webElement) {
        this.webElement = webElement;
    }

    public List<HeaderCell> getCells() {
        return cells;
    }

    public void setCells(List<HeaderCell> cells) {
        this.cells = cells;
    }

    public HeaderCell getCell(int index) {
        return getCells().get(index);
    }
}
