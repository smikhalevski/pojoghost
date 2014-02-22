/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors.impl;

import java.util.Map;
import org.ehony.pojoghost.accessors.Getter;
import org.ehony.pojoghost.core.*;
import org.ehony.pojoghost.core.impl.*;

public class KeyGetter<O extends Map, T> implements Getter<O, T> {

    private Object key;

    public KeyGetter(Object key) {
        this.key = key;
    }

    public Entity<T> get(final Entity<O> entity) {
        return new BasicEntity(entity.getObject().get(key));
    }

    public Bound<T> getReturnBound(Class<? extends O> type) {
        return (Bound)BasicBound.inspect(type).findImplemetedBoundOfType(Map.class).getParameterBounds().get(1);
    }
}
