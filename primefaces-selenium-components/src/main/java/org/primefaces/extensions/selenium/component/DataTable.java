package org.primefaces.extensions.selenium.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractPageableData;
import org.primefaces.extensions.selenium.component.model.data.Page;
import org.primefaces.extensions.selenium.component.model.data.Paginator;
import org.primefaces.extensions.selenium.component.model.datatable.Cell;
import org.primefaces.extensions.selenium.component.model.datatable.Header;
import org.primefaces.extensions.selenium.component.model.datatable.Row;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DataTable extends AbstractPageableData {

    private List<Row> rows;
    private Paginator paginator;
    private Header header;

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

    public Row getRow(int index) {
        return getRows().get(index);
    }

    public WebElement getHeaderWebElement() {
        return findElement(By.tagName("table")).findElement(By.tagName("thead"));
    }

    @Override
    public Paginator getPaginator() {
        if (paginator == null) {
            paginator = new Paginator(getPaginatorWebElement());
        }

        return paginator;
    }

    public Header getHeader() {
        if (header == null) {
            List<Cell> cells = getHeaderWebElement().findElements(By.tagName("th")).stream().map(cellElt -> new Cell(cellElt)).collect(Collectors.toList());
            header = new Header(getHeaderWebElement(), cells);
        }

        return header;
    }

    public void selectPage(Page page) {
        PrimeSelenium.guardAjax(page.getWebElement()).click();
        this.rows = null;
    }

    public void selectPage(int number) {
        for (Page page : getPaginator().getPages()) {
            if (page.getNumber() == number) {
                selectPage(page);
            }
        }
    }

}
