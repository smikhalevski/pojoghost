/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.*;
import org.ehony.pojoghost.api.*;

import java.lang.reflect.Field;

import static java.lang.String.format;
import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.apache.commons.lang3.Validate.notBlank;

/**
 * Get value by field name, ex. <code>object.<b>name</b></code>.
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
            throw new AccessException(format("Expected %s.%s", type.getName(), name), e);
        }
    }

    @SuppressWarnings("unchecked")
    public Entity<Type> get(Entity<From> from) {
        From o = from.getObject();
        Class type = o.getClass();
        try {
            return new BasicEntity(getField(type).get(o));
        } catch (IllegalAccessException e) {
            throw new AccessException(format("Cannot access %s.%s", type.getName(), name), e);
        }
    }

    @SuppressWarnings("unchecked")
    public Bound<Type> getReturnBound(Class<? extends From> type) {
        return BasicBound.inspect(getField(type).getGenericType());
    }
}
