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

import java.io.File;
import java.util.Properties;
import java.util.Random;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.primefaces.extensions.selenium.spi.PrimeSeleniumAdapter;

import org.apache.tomee.embedded.Configuration;
import org.apache.tomee.embedded.Container;

public class ExampleAdapter implements PrimeSeleniumAdapter {

    private Container container;

    @Override
    public WebDriver createWebDriver() {
        System.setProperty("webdriver.gecko.driver", "D:/geckodriver.exe");

        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        return new FirefoxDriver(options);
    }

    @Override
    public void startup() throws Exception {
        Configuration config = new Configuration();
        config.setHttpPort(createRandomPort());
        config.setQuickSession(true);
        config.setStopPort(createRandomPort());
        config.setDir("target/tomee");

        Properties properties = new Properties();
        properties.put("org.apache.tomee.loader.TomEEJarScanner.scanClassPath", "false");
        properties.put("org.apache.tomee.loader.TomEEJarScanner.scanBootstrapClassPath", "false");
        config.setProperties(properties);

        File targetDir = new File("target/");
        String[] warFiles = targetDir.list((dir, name) -> name.endsWith(".war"));
        if (warFiles == null || warFiles.length == 0) {
            throw new RuntimeException("No WAR found in target; please build before ;)");
        }
        String warName = warFiles[0];
        File warFile = new File(targetDir, warName);

        container = new Container(config);
        container.deploy(
                "ROOT",
                warFile,
                true);
    }

    @Override
    public String getBaseUrl() {
        return "http://localhost:" + container.getConfiguration().getHttpPort() + "/";
    }

    @Override
    public void shutdown() throws Exception {
        container.close();
    }

    private int createRandomPort() {
        Random random = new Random();
        return random.nextInt((9000 - 8000) + 1) + 8000;
    }
}
