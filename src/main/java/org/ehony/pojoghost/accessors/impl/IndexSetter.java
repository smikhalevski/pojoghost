/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors.impl;

import org.ehony.pojoghost.core.impl.BasicBound;
import org.ehony.pojoghost.core.Entity;
import org.ehony.pojoghost.accessors.Setter;
import java.lang.reflect.*;
import java.util.List;
import org.ehony.pojoghost.core.*;

public class IndexSetter<O, T> implements Setter<O, T> {

    private int index;

    public IndexSetter(int index) {
        this.index = index;
    }

    public void set(Entity<O> entity, Entity<T> value) {
        O o = entity.getObject();
        T t = value.getObject();
        if (o.getClass().isArray()) {
            Array.set(o, index, t);
            return;
        }
        if (o instanceof List) {
            ((List)o).set(index, t);
            return;
        }
        throw new UnsupportedOperationException();
    }

    public Bound<T> getArgumentBound(Class<? extends O> type) {
        Bound tree = BasicBound.inspect(type);
        if (type.isArray()) {
            return (Bound)tree.getParameterBounds().get(0);
        }
        if (List.class.isAssignableFrom(type)) {
            return (Bound)tree.findImplemetedBoundOfType(List.class).getParameterBounds().get(0);
        }
        throw new IllegalArgumentException();
    }
}