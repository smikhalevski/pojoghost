/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.api.Getter;
import org.ehony.pojoghost.core.*;
import org.ehony.pojoghost.core.impl.*;

import java.util.Map;

public class KeyGetter<From extends Map, Type> implements Getter<From, Type>
{

    private Object key;

    public KeyGetter(Object key) {
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    public Entity<Type> get(Entity<From> from) {
        return new BasicEntity<Type>((Type) from.getObject().get(key));
    }

    @SuppressWarnings("unchecked")
    public Bound<Type> getReturnBound(Class<? extends From> type) {
        return (Bound<Type>) BasicBound.inspect(type).findImplemetedBoundOfType(Map.class).getParameterBounds().get(1);
    }
}
