package org.primefaces.extensions.selenium.component.base;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.primefaces.extensions.selenium.PrimeExpectedConditions;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.model.data.Page;
import org.primefaces.extensions.selenium.component.model.data.Paginator;

import java.util.List;

public abstract class AbstractPageableData extends AbstractComponent {

    @FindBy(className = "ui-paginator")
    private WebElement paginator;

    public abstract List<WebElement> getRowsWebElement();

    public WebElement getPaginatorWebElement() {
        return paginator;
    }

    public Paginator getPaginator() {
        return new Paginator(getPaginatorWebElement());
    }

    public void selectPage(Page page) {
        page.getWebElement().click();
        PrimeSelenium.waitGui().until(PrimeExpectedConditions.jQueryNotActive());
    }

    public void selectPage(int number) {
        for (Page page : getPaginator().getPages()) {
            if (page.getNumber() == number) {
                selectPage(page);
            }
        }
    }
}
