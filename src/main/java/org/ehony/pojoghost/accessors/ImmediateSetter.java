/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.ReflectionBound1;
import org.ehony.pojoghost.api.*;

import java.lang.reflect.Field;

/**
 * Set value by field name, ex. <code>object.<b>name</b> = value</code>.
 * {@inheritDoc}
 */
public class ImmediateSetter<To, Type> implements Setter<To, Type>
{

    private String name;

    public ImmediateSetter(String name) {
        this.name = name;
    }

    private Field getField(Class type) {
        try {
            Field f = type.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            throw new AccessException("Field not found " + type.getName() + "#" + name, e);
        }
    }

    public void set(Entity<To> to, Entity<Type> value) {
        To o = to.getObject();
        Class type = o.getClass();
        try {
            getField(type).set(o, value.getObject());
        } catch (IllegalAccessException e) {
            throw new AccessException("Cannot access " + type.getName() + "#" + name, e);
        }
    }

    @SuppressWarnings("unchecked")
    public Bound getArgumentBound(Class<? extends To> type) {
        return new ReflectionBound1(getField(type).getGenericType());
    }
}
