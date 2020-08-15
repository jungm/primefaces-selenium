package org.primefaces.extensions.selenium.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.component.base.AbstractPageableData;
import org.primefaces.extensions.selenium.component.model.datatable.Cell;
import org.primefaces.extensions.selenium.component.model.datatable.Row;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DataTable extends AbstractPageableData {

    private List<Row> rows;

    @Override
    public List<WebElement> getRowsWebElement() {
        return findElement(By.tagName("table")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
    }

    public List<Row> getRows() {
        if (rows == null) {
            rows = getRowsWebElement().stream().map(rowElt -> {
                List<Cell> cells = rowElt.findElements(By.tagName("td")).stream().map(cellElt -> new Cell(cellElt)).collect(Collectors.toList());
                return new Row(rowElt, cells);
            }).collect(Collectors.toList());
        }
        return rows;
    }

    public WebElement getHeaderWebElement() {
        return findElement(By.tagName("table")).findElement(By.tagName("thead"));
    }
}
