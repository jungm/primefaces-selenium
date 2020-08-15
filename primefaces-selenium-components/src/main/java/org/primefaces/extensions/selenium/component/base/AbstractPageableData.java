package org.primefaces.extensions.selenium.component.base;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public abstract class AbstractPageableData extends AbstractComponent {

    @FindBy(className = "ui-paginator")
    private WebElement paginator;

    public abstract List<WebElement> getRowsWebElement();

    public WebElement getPaginatorWebElement() {
        return paginator;
    }
}
