/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.ReflectionBound;
import org.ehony.pojoghost.api.*;

import java.util.Map;

/**
 * Set value from map by key, ex. <code>map[<b>key</b>] = value</code>.
 * {@inheritDoc}
 */
public class KeySetter<To extends Map, Type> implements Setter<To, Type>
{

    private Object key;

    public KeySetter(Object key) {
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    public void set(Entity<To> to, Entity<Type> value) {
        to.getObject().put(key, value.getObject());
    }

    @SuppressWarnings("unchecked")
    public Bound getArgumentBound(Class<? extends To> type) {
        return new ReflectionBound(type).findImplementedBoundOfType(Map.class).getParameters().get(1);
    }
}
