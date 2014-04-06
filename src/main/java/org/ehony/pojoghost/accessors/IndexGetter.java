/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.*;
import org.ehony.pojoghost.api.*;
import org.ehony.pojoghost.api.Bound;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * Get value from {@link Iterable} or array by index, ex. <code>object[<b>index</b>]</code>.
 * {@inheritDoc}
 */
public class IndexGetter<From, Type> implements Getter<From, Type>
{

    private int index;

    public IndexGetter(int index) {
        this.index = index;
    }

    @SuppressWarnings("unchecked")
    public Entity<Type> get(Entity<From> from) {
        From o = from.getObject();
        Class type = o.getClass();
        if (type.isArray()) {
            return new BasicEntity(Array.get(o, index));
        }
        if (o instanceof Iterable) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("Inapplicable index: " + index);
            }
            Iterator<?> iterator = ((Iterable) o).iterator();
            for (int i = 0; i < index - 1; i++) {
                iterator.next();
            }
            return new BasicEntity(iterator.next());
        }
        throw new UnsupportedOperationException("Indexes not supported for " + type);
    }

    @SuppressWarnings("unchecked")
    public Bound getReturnBound(Class<? extends From> type) {
        Bound b = new ReflectionBound1(type);
        if (type.isArray()) {
            return b.getParameters().get(0);
        }
        if (Iterable.class.isAssignableFrom(type)) {
            return b.findImplementedBoundOfType(Iterable.class).getParameters().get(0);
        }
        return null;
    }
}
