/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.ReflectionBound;
import org.ehony.pojoghost.api.*;

import java.lang.reflect.Array;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.isTrue;

/**
 * Get value from {@link List} or array by index, ex. <code>object[<b>index</b>]</code>.
 * {@inheritDoc}
 */
public class IndexSetter<To, Type> implements Setter<To, Type>
{

    private int index;

    public IndexSetter(int index) {
        this.index = index;
    }

    @SuppressWarnings("unchecked")
    public void set(Entity<To> to, Entity<Type> value) {
        To o = to.getObject();
        Type v = value.getObject();
        Class type = o.getClass();
        if (type.isArray()) {
            Array.set(o, index, v);
            return;
        }
        if (o instanceof List) {
            ((List) o).set(index, v);
            return;
        }
        throw new UnsupportedOperationException("Indexes not supported for " + type);
    }

    @SuppressWarnings("unchecked")
    public Bound<Type> getArgumentBound(Class<? extends To> type) {
        Bound b = new ReflectionBound<Type>(type);
        if (type.isArray()) {
            return (Bound<Type>) b.getParameters().get(0);
        }
        if (List.class.isAssignableFrom(type)) {
            return (Bound<Type>) b.findImplementedBoundOfType(List.class).getParameters().get(0);
        }
        return null;
    }
}