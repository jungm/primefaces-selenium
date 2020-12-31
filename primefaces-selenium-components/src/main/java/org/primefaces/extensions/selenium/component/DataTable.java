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
package org.primefaces.extensions.selenium.component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractPageableData;
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
}
