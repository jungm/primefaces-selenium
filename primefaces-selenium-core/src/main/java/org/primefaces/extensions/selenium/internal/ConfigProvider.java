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

import java.util.Properties;
import org.primefaces.extensions.selenium.spi.PrimeSeleniumAdapter;

public class ConfigProvider {

    private static ConfigProvider configProvider = null;

    private int uiTimeout = 2;
    private int ajaxTimeout = 10;
    private boolean disableJQueryAnimations = true;
    private PrimeSeleniumAdapter adapter;

    public ConfigProvider() {
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/primefaces-selenium/config.properties"));

            String uiTimeout = properties.getProperty("uiTimeout");
            if (uiTimeout != null && !uiTimeout.trim().isEmpty()) {
                this.uiTimeout = Integer.parseInt(uiTimeout);
            }

            String ajaxTimeout = properties.getProperty("ajaxTimeout");
            if (ajaxTimeout != null && !ajaxTimeout.trim().isEmpty()) {
                this.ajaxTimeout = Integer.parseInt(ajaxTimeout);
            }

            String disableJQueryAnimations = properties.getProperty("disableJQueryAnimations");
            if (disableJQueryAnimations != null && !disableJQueryAnimations.trim().isEmpty()) {
                this.disableJQueryAnimations = Boolean.parseBoolean(disableJQueryAnimations);
            }

            String adapter = properties.getProperty("adapter");
            if (adapter != null && !adapter.trim().isEmpty()) {
                this.adapter = (PrimeSeleniumAdapter) Class.forName(adapter).newInstance();
            }
            else {
                throw new RuntimeException("No lifecycle set via config.properties!");
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getUiTimeout() {
        return uiTimeout;
    }

    public int getAjaxTimeout() {
        return ajaxTimeout;
    }

    public boolean isDisableJQueryAnimations() {
        return disableJQueryAnimations;
    }

    public PrimeSeleniumAdapter getAdapter() {
        return adapter;
    }

    public static synchronized ConfigProvider getInstance() {
        if (configProvider == null) {
            configProvider = new ConfigProvider();
        }

        return configProvider;
    }
}
