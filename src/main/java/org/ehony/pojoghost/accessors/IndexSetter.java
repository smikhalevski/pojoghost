/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.api.*;
import org.ehony.pojoghost.BasicBound;

import java.lang.reflect.Array;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.isTrue;

/**
 * Set value from iterable by index, ex. <code>iterable[<b>index</b>] = value</code>.
 * {@inheritDoc}
 */
public class IndexSetter<To, Type> implements Setter<To, Type>
{

    private int index;

    public IndexSetter(int index) {
        isTrue(index >= 0, "Non-negative index expected.");
        this.index = index;
    }

    @SuppressWarnings("unchecked")
    public void set(Entity<To> to, Entity<Type> value) {
        To o = to.getObject();
        Type t = value.getObject();
        Class type = o.getClass();
        if (type.isArray()) {
            Array.set(o, index, t);
            return;
        }
        if (o instanceof List) {
            ((List) o).set(index, t);
            return;
        }
        throw new UnsupportedOperationException(format("Index not supported %s[%d]", type.getName(), index));
    }

    @SuppressWarnings("unchecked")
    public Bound<Type> getArgumentBound(Class<? extends To> type) {
        Bound tree = BasicBound.inspect(type);
        if (type.isArray()) {
            return (Bound<Type>) tree.getParameterBounds().get(0);
        }
        if (List.class.isAssignableFrom(type)) {
            return (Bound<Type>) tree.findImplemetedBoundOfType(List.class).getParameterBounds().get(0);
        }
        throw new IllegalArgumentException(type.getName() + " must be iterable.");
    }
}