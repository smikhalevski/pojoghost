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

import java.lang.reflect.Array;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;

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
        if (o.getClass().isArray()) {
            Array.set(o, index, t);
            return;
        }
        if (o instanceof List) {
            ((List) o).set(index, t);
            return;
        }
        throw new UnsupportedOperationException("Cannot set item by index to " + o);
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
        throw new IllegalArgumentException("List expected: " + type);
    }
}