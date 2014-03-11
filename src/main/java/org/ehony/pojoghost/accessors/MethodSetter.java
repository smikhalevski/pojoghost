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

public class MethodSetter<O, T> implements Setter<O, T>
{

    private String name;
    private Class[] argTypes;
    private Object[] argDefaults;
    private int index;

    public MethodSetter(String name) {
        this.name = name;
        argDefaults = new Object[]{null};
    }
    
    public MethodSetter(String name, Class[] argTypes, Object[] argDefaults, int index) {
        this.name = name;
        this.argTypes = argTypes;
        this.argDefaults = argDefaults;
        this.index = index;
    }

    private Method getMethod(Class<?> type) {
        try {
            Class[] argTypes = this.argTypes;
            Object[] argDefaults = this.argDefaults;
            if (argTypes == null | argDefaults == null) {
                argTypes = new Class[]{};
                argDefaults = new Object[]{};
            }
            return type.getMethod(name, argTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void set(Entity<O> to, Entity<T> value) {
        O object = to.getObject();
        try {
            Class[] argTypes = this.argTypes;
            if (argTypes == null) {
                argTypes = new Class[]{value.getClass()};
            }
            Method method = object.getClass().getMethod(name, argTypes);
            method.setAccessible(true);
            argDefaults[index] = value.getObject();
            getMethod(object.getClass()).invoke(object, argDefaults);
        } catch (Exception e) {
            throw new AccessException(e);
        }
    }

    public Bound getArgumentBound(Class<? extends O> type) {
        return new ReflectionBound(getMethod(type).getGenericReturnType());
    }
}
