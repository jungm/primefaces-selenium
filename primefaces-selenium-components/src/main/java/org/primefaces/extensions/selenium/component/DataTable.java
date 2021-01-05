/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.selenium.component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractPageableData;
import org.primefaces.extensions.selenium.component.base.ComponentUtils;
import org.primefaces.extensions.selenium.component.model.datatable.Cell;
import org.primefaces.extensions.selenium.component.model.datatable.Header;
import org.primefaces.extensions.selenium.component.model.datatable.HeaderCell;
import org.primefaces.extensions.selenium.component.model.datatable.Row;

public abstract class DataTable extends AbstractPageableData {

    @Override
    public List<WebElement> getRowsWebElement() {
        return findElement(By.tagName("table")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
    }

    public List<Row> getRows() {
        return getRowsWebElement().stream().map(rowElt -> {
            List<Cell> cells = rowElt.findElements(By.tagName("td")).stream().map(cellElt -> new Cell(cellElt)).collect(Collectors.toList());
            return new Row(rowElt, cells);
        }).collect(Collectors.toList());
    }

    public Row getRow(int index) {
        return getRows().get(index);
    }

    /**
     * Gets the Cell at the row/column coordinates.
     *
     * @param rowIndex the index of the row
     * @param colIndex the index of the column in the row
     * @return the {@link Cell} representing these coordinates
     * @throws IndexOutOfBoundsException if either row or column not found
     */
    public Cell getCell(int rowIndex, int colIndex) throws IndexOutOfBoundsException {
        Row row = getRow(rowIndex);
        if (row == null) {
            throw new IndexOutOfBoundsException("Row " + rowIndex + " was not found in table");
        }
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            throw new IndexOutOfBoundsException("Column " + colIndex + " was not found in Row " + rowIndex + ".");
        }
        return cell;
    }

    public WebElement getHeaderWebElement() {
        return findElement(By.tagName("table")).findElement(By.tagName("thead"));
    }

    public Header getHeader() {
        List<HeaderCell> cells = getHeaderWebElement().findElements(By.tagName("th")).stream()
                    .map(cellElt -> new HeaderCell(cellElt))
                    .collect(Collectors.toList());
        return new Header(getHeaderWebElement(), cells);
    }

    public void sort(String headerText) {
        Optional<HeaderCell> cell = getHeader().getCell(headerText);
        if (cell.isPresent()) {
            PrimeSelenium.guardAjax(cell.get().getWebElement().findElement(By.className("ui-sortable-column-icon"))).click();
        }
    }

    public void filter(int cellIndex, String filterValue) {
        getHeader().getCell(cellIndex).setFilterValue(filterValue, false);
    }

    public void filter(String headerText, String filterValue) {
        Optional<HeaderCell> cell = getHeader().getCell(headerText);
        if (cell.isPresent()) {
            cell.get().setFilterValue(filterValue, false);
        }
    }

    public void removeFilter(int cellIndex) {
        filter(cellIndex, null);
    }

    public void removeFilter(String headerText) {
        filter(headerText, null);
    }

    /**
     * If using multiple checkbox mode this toggles the Select All checkbox in the header.
     */
    public void toggleSelectAllCheckBox() {
        WebElement checkboxAll = getSelectAllCheckBox();
        if (ComponentUtils.hasBehavior(this, "rowSelect") || ComponentUtils.hasBehavior(this, "rowUnselect")) {
            PrimeSelenium.guardAjax(checkboxAll).click();
        }
        else {
            checkboxAll.click();
        }
    }

    /**
     * Gets the Select All checkbox in the header of the table.
     *
     * @return the WebElement representing the checkbox
     */
    public WebElement getSelectAllCheckBox() {
        return getHeader().getCell(0).getWebElement();
    }
}
