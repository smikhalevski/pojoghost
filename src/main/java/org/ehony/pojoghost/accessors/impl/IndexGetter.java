/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors.impl;

import org.ehony.pojoghost.core.impl.BasicBound;
import org.ehony.pojoghost.core.impl.BasicEntity;
import org.ehony.pojoghost.core.Entity;
import org.ehony.pojoghost.accessors.Getter;
import java.lang.reflect.*;
import java.util.List;
import org.ehony.pojoghost.core.*;

public class IndexGetter<O, T> implements Getter<O, T> {

    private int index;

    public IndexGetter(int index) {
        this.index = index;
    }
    
    public Entity<T> get(Entity<O> entity) {
        O o = entity.getObject();
        if (o.getClass().isArray()) {
            return new BasicEntity(Array.get(o, index));
        }
        if (o instanceof List) {
            return new BasicEntity(((List)o).get(index));
        }
        throw new UnsupportedOperationException();
    }

    public Bound<T> getReturnBound(Class<? extends O> type) {
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
