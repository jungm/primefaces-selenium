package org.primefaces.extensions.selenium.component.model.data;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class Paginator {

    private WebElement webElement;
    private List<Page> pages;

    public Paginator(WebElement webElement) {
        this.webElement = webElement;
    }

    public WebElement getWebElement() {
        return webElement;
    }

    public void setWebElement(WebElement webElement) {
        this.webElement = webElement;
    }

    public List<Page> getPages() {
        if (pages == null) {
            pages = webElement.findElements(By.className("ui-paginator-page")).stream().map(pageElt -> {
                int number = Integer.parseInt(pageElt.getText());
                return new Page(number, pageElt);
            }).collect(Collectors.toList());
        }
        return pages;
    }

    public Page getPage(int index) {
        return getPages().get(index);
    }
}
