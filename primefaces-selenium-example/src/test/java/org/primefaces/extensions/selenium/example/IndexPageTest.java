/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
package org.primefaces.extensions.selenium.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.primefaces.extensions.selenium.AbstractPrimePageTest;

public class IndexPageTest extends AbstractPrimePageTest {

    @Test
    public void myFirstTest(IndexPage index) throws InterruptedException {
        // right page?
        Assertions.assertTrue(index.isAt());
        assertNotDisplayed(index.getCar());
        
        // just to follow the browser with a human eye for the showcase :D - not need in your real tests
        Thread.sleep(2000);
        
        // select manufacturer
        assertDisplayed(index.getManufacturer());
        index.getManufacturer().select("BMW");
        Assertions.assertTrue(index.getManufacturer().isSelected("BMW"));
        
        // just to follow the browser with a human eye for the showcase :D - not need in your real tests
        Thread.sleep(2000);
        
        // type car
        assertDisplayed(index.getCar());
        index.getCar().setValue("E30 M3");
        
        // just to follow the browser with a human eye for the showcase :D - not need in your real tests
        Thread.sleep(2000);
    }
}
