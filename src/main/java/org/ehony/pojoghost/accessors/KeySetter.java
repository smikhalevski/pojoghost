/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.api.Setter;
import org.ehony.pojoghost.core.*;
import org.ehony.pojoghost.core.impl.BasicBound;

import java.util.Map;

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
    public Bound<Type> getArgumentBound(Class<? extends To> type) {
        return (Bound<Type>) BasicBound.inspect(type).findImplemetedBoundOfType(Map.class).getParameterBounds().get(1);
    }
}
