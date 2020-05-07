/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
package org.primefaces.extensions.selenium.internal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.primefaces.extensions.selenium.spi.PrimeSeleniumAdapter;

public class ConfigProvider {

    private static ConfigProvider configProvider = null;

    private int guiTimeout = 2;
    private int ajaxTimeout = 10;
    private int httpTimeout = 10;
    private int documentLoadTimeout = 15;

    private boolean disableJQueryAnimations = true;
    private PrimeSeleniumAdapter adapter;
    private List<String> onloadScripts;

    public ConfigProvider() {
        try {
            InputStream config = this.getClass().getResourceAsStream("/primefaces-selenium/config.properties");
            if (config != null) {
                Properties properties = new Properties();
                properties.load(config);

                String guiTimeout = properties.getProperty("guiTimeout");
                if (guiTimeout != null && !guiTimeout.trim().isEmpty()) {
                    this.guiTimeout = Integer.parseInt(guiTimeout);
                }

                String ajaxTimeout = properties.getProperty("ajaxTimeout");
                if (ajaxTimeout != null && !ajaxTimeout.trim().isEmpty()) {
                    this.ajaxTimeout = Integer.parseInt(ajaxTimeout);
                }

                String httpTimeout = properties.getProperty("httpTimeout");
                if (httpTimeout != null && !httpTimeout.trim().isEmpty()) {
                    this.httpTimeout = Integer.parseInt(httpTimeout);
                }

                String documentLoadTimeout = properties.getProperty("documentLoadTimeout");
                if (documentLoadTimeout != null && !documentLoadTimeout.trim().isEmpty()) {
                    this.documentLoadTimeout = Integer.parseInt(documentLoadTimeout);
                }

                String disableJQueryAnimations = properties.getProperty("disableJQueryAnimations");
                if (disableJQueryAnimations != null && !disableJQueryAnimations.trim().isEmpty()) {
                    this.disableJQueryAnimations = Boolean.parseBoolean(disableJQueryAnimations);
                }

                String adapter = properties.getProperty("adapter");
                if (adapter != null && !adapter.trim().isEmpty()) {
                    this.adapter = (PrimeSeleniumAdapter) Class.forName(adapter).newInstance();
                }
            }

            if (adapter == null) {
                throw new RuntimeException("No lifecycle set via config.properties!");
            }

            buildOnloadScripts();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void buildOnloadScripts() throws Exception {
        onloadScripts = new ArrayList<>();
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/primefaces-selenium/onload.js"),
                StandardCharsets.UTF_8))) {
            onloadScripts.add(buffer.lines().collect(Collectors.joining("\n")));
        }

        if (disableJQueryAnimations) {
            onloadScripts.add("if (window.$) { $(function() { $.fx.off = true; }); }");
        }

        adapter.registerOnloadScripts(onloadScripts);
    }

    public int getGuiTimeout() {
        return guiTimeout;
    }

    public int getAjaxTimeout() {
        return ajaxTimeout;
    }

    public int getHttpTimeout() {
        return httpTimeout;
    }

    public int getDocumentLoadTimeout() {
        return documentLoadTimeout;
    }

    public boolean isDisableJQueryAnimations() {
        return disableJQueryAnimations;
    }

    public PrimeSeleniumAdapter getAdapter() {
        return adapter;
    }

    public List<String> getOnloadScripts() {
        return onloadScripts;
    }

    public static synchronized ConfigProvider getInstance() {
        if (configProvider == null) {
            configProvider = new ConfigProvider();
        }

        return configProvider;
    }
}
