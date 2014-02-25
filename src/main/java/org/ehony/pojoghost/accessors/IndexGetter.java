/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.*;
import org.ehony.pojoghost.api.*;

import java.lang.reflect.Array;
import java.util.Iterator;

import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.validState;

/**
 * Get value from iterable by index, ex. <code>iterable[<b>index</b>]</code>.
 * {@inheritDoc}
 */
public class IndexGetter<From, Type> implements Getter<From, Type>
{

    private int index;

    public IndexGetter(int index) {
        isTrue(index >= 0, "Non-negative index expected.");
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
            Iterator<?> iterator = ((Iterable) o).iterator();
            for (int i = 0; i < index - 1; i++) {
                iterator.next();
            }
            return new BasicEntity(iterator.next());
        }
        throw new UnsupportedOperationException(format("Index not supported %s[%d]", type.getName(), index));
    }

    @SuppressWarnings("unchecked")
    public Bound<Type> getReturnBound(Class<? extends From> type) {
        Bound tree = BasicBound.inspect(type);
        if (type.isArray()) {
            return (Bound<Type>) tree.getParameterBounds().get(0);
        }
        if (Iterable.class.isAssignableFrom(type)) {
            return (Bound<Type>) tree.findImplemetedBoundOfType(Iterable.class).getParameterBounds().get(0);
        }
        throw new IllegalArgumentException(type.getName() + " must be iterable.");
    }
}
