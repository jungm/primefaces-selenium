package org.primefaces.extensions.selenium.component.model.datatable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HeaderCell extends Cell {

    public HeaderCell(WebElement webElement) {
        super(webElement);
    }

    public WebElement getColumnFilter() {
        if (getWebElement() != null) {
            return getWebElement().findElement(By.className("ui-column-filter"));
        }
        return null;
    }
}
