/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core;

public interface ConverterRegistryAware {
    
    ConverterRegistry getRegistry();
    
    void setRegistry(ConverterRegistry registry);
}
