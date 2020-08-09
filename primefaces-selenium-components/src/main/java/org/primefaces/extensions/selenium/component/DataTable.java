package org.primefaces.extensions.selenium.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.component.base.AbstractPageableData;

import java.util.List;

public abstract class DataTable extends AbstractPageableData {

    @Override
    public List<WebElement> getRows() {
        return findElement(By.tagName("table")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
    }

    public WebElement getHeader() {
        return findElement(By.tagName("table")).findElement(By.tagName("thead"));
    }
}
