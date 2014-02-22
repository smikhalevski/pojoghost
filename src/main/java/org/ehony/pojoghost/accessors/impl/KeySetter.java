/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors.impl;

import java.util.Map;
import org.ehony.pojoghost.accessors.Setter;
import org.ehony.pojoghost.core.*;
import org.ehony.pojoghost.core.impl.BasicBound;

public class KeySetter<O extends Map, T> implements Setter<O, T> {

    private Object key;

    public KeySetter(Object key) {
        this.key = key;
    }

    public void set(Entity<O> entity, Entity<T> value) {
        entity.getObject().put(key, value.getObject());
    }

    public Bound<T> getArgumentBound(Class<? extends O> type) {
        return (Bound)BasicBound.inspect(type).findImplemetedBoundOfType(Map.class).getParameterBounds().get(1);
    }
}
