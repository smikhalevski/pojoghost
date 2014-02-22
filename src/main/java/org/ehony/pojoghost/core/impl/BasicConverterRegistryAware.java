/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core.impl;

import org.ehony.pojoghost.core.*;

public class BasicConverterRegistryAware implements ConverterRegistryAware {

    private ConverterRegistry registry;

    public ConverterRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(ConverterRegistry registry) {
        this.registry = registry;
    }
}
