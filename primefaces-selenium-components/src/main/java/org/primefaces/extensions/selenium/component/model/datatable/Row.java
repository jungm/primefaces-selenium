package org.primefaces.extensions.selenium.component.model.datatable;

import org.openqa.selenium.WebElement;

import java.util.List;

public class Row {

    private WebElement webElement;
    private List<Cell> cells;

    public Row(WebElement webElement, List<Cell> cells) {
        this.webElement = webElement;
        this.cells = cells;
    }

    public WebElement getWebElement() {
        return webElement;
    }

    public void setWebElement(WebElement webElement) {
        this.webElement = webElement;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public Cell getCell(int index) {
        return cells.get(index);
    }
}
