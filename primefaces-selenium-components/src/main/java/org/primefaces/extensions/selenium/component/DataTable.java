package org.primefaces.extensions.selenium.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
        //TDDO: to cache or not to cache?
        if (paginator == null) {
            paginator = new Paginator(getPaginatorWebElement());
        }

        return paginator;
    }

    public Header getHeader() {
        //TDDO: to cache or not to cache?
        if (header == null) {
            List<Cell> cells = getHeaderWebElement().findElements(By.tagName("th")).stream().map(cellElt -> new Cell(cellElt)).collect(Collectors.toList());
            header = new Header(getHeaderWebElement(), cells);
        }

        return header;
    }

    private void resetCachedData() {
        this.rows = null;
        this.paginator = null;
    }

    public void selectPage(Page page) {
        //TODO: how to wait correct?
        //PrimeSelenium.guardAjax(page.getWebElement()).click();
        page.getWebElement().click();
        resetCachedData();
    }

    public void selectPage(int number) {
        for (Page page : getPaginator().getPages()) {
            if (page.getNumber() == number) {
                selectPage(page);
            }
        }
    }

    public void sort(String headerText) {
        //TODO: how to wait correct?
        for (Cell cell : getHeader().getCells()) {
            if (cell.getText().equals(headerText)) {
                cell.getWebElement().click();
            }
        }
    }
}
