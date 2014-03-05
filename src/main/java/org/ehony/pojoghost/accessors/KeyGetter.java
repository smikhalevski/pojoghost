/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.*;
import org.ehony.pojoghost.api.*;

import java.util.Map;

/**
 * Get value from map by key, ex. <code>map[<b>key</b>]</code>.
 * {@inheritDoc}
 */
public class KeyGetter<From extends Map, Type> implements Getter<From, Type>
{

    private Object key;

    public KeyGetter(Object key) {
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    public Entity<Type> get(Entity<From> from) {
        return new BasicEntity(from.getObject().get(key));
    }

    @SuppressWarnings("unchecked")
    public Bound<Type> getReturnBound(Class<? extends From> type) {
        return (Bound<Type>) new ReflectionBound(type).findImplementedBoundOfType(Map.class).getParameters().get(1);
    }
}
