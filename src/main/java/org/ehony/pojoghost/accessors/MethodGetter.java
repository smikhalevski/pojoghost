/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.ReflectionBound;
import org.ehony.pojoghost.api.*;
import org.ehony.pojoghost.ReflectionBound;

import java.lang.reflect.Method;

public class MethodGetter<O, T> implements Getter<O, T>
{

    private String name;
    private Class[] argTypes;
    private Object[] argDefaults;

    private Entity<O> entity;

    public MethodGetter(String name) {
        this.name = name;
    }

    public MethodGetter(String name, Class[] argTypes, Object[] argDefaults) {
        this.name = name;
        this.argTypes = argTypes;
        this.argDefaults = argDefaults;
    }

    private Method getMethod(Class<?> type) {
        try {
            Class[] argTypes = this.argTypes;
            Object[] argDefaults = this.argDefaults;
            if (argTypes == null | argDefaults == null) {
                argTypes = new Class[] {};
                argDefaults = new Object[] {};
            }
            return type.getMethod(name, argTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Entity<T> get(Entity<O> from) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SuppressWarnings("unchecked")
    public Bound getReturnBound(Class<? extends O> type) {
        return new ReflectionBound(getMethod(type).getGenericReturnType());
    }
}
