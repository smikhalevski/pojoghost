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

import java.lang.reflect.Field;

/**
 * Get value by field name, ex. <code>object.<b>name</b></code>.
 * {@inheritDoc}
 */
public class ImmediateGetter<From, Type> implements Getter<From, Type>
{

    private String name;

    public ImmediateGetter(String name) {
        this.name = name;
    }

    private Field getField(Class<?> type) {
        try {
            Field f = type.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            throw new AccessException("Field not found " + type.getName() + "#" + name, e);
        }
    }

    @SuppressWarnings("unchecked")
    public Entity<Type> get(Entity<From> from) {
        From o = from.getObject();
        Class type = o.getClass();
        try {
            return new BasicEntity(getField(type).get(o));
        } catch (IllegalAccessException e) {
            throw new AccessException("Cannot access " + type.getName() + "#" + name, e);
        }
    }

    @SuppressWarnings("unchecked")
    public Bound getReturnBound(Class<? extends From> type) {
        return new ReflectionBound1(getField(type).getGenericType());
    }
}
