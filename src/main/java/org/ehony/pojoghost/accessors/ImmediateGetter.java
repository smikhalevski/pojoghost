/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.api.*;
import org.ehony.pojoghost.core.*;
import org.ehony.pojoghost.core.impl.*;

import java.lang.reflect.Field;

import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.apache.commons.lang3.Validate.notBlank;

/**
 * Get value by field name.
 * {@inheritDoc}
 */
public class ImmediateGetter<From, Type> implements Getter<From, Type>
{

    private String name;

    public ImmediateGetter(String name) {
        notBlank(name, "Field name expected.");
        this.name = name;
    }

    private Field getField(Class<?> type) {
        try {
            Field f = type.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            throw new AccessException("Expected field " + name + " at " + type, e);
        }
    }

    @SuppressWarnings("unchecked")
    public Entity<Type> get(Entity<From> from) {
        From o = from.getObject();
        try {
            return new BasicEntity<Type>((Type) getField(o.getClass()).get(o));
        } catch (IllegalAccessException e) {
            throw new AccessException("Cannot get field " + name + " from " + o, e);
        }
    }

    @SuppressWarnings("unchecked")
    public Bound<Type> getReturnBound(Class<? extends From> type) {
        return BasicBound.inspect(getField(type).getGenericType());
    }
}
