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
package org.primefaces.extensions.selenium.internal.junit;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.primefaces.extensions.selenium.internal.ConfigProvider;
import org.primefaces.extensions.selenium.spi.PrimeSeleniumAdapter;

public class BootstrapExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static final Object SYNCHRONIZER = new Object();

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        synchronized (SYNCHRONIZER) {
            if (!started) {
                PrimeSeleniumAdapter adapter = ConfigProvider.getInstance().getAdapter();
                adapter.startup();

                // The following line registers a callback hook when the root test context is shut down
                context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put(BootstrapExtension.class.getName(), this);

                started = true;
            }
        }
    }

    @Override
    public void close() throws Exception {
        PrimeSeleniumAdapter adapter = ConfigProvider.getInstance().getAdapter();
        adapter.shutdown();
    }

}
