/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core.impl;

import org.ehony.pojoghost.core.Injector;

public class DefaultInjector implements Injector {

    public <T> T newInstance(Class<T> type) {
        return newInstance(type, null);
    }

    public <T> T newInstance(Class<T> type, T instance) {
        if (String.class.isAssignableFrom(type)) {
            return (T)new String();
        }
        if (Integer.class.isAssignableFrom(type)) {
            return (T)new Integer(0);
        }
        if (Double.class.isAssignableFrom(type)) {
            return (T)new Double(0);
        }
        return null;
    }
}
