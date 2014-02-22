/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors.impl;

import java.lang.reflect.Method;
import org.ehony.pojoghost.accessors.*;
import org.ehony.pojoghost.core.*;
import org.ehony.pojoghost.core.impl.*;

public class MethodSetter<O, T> extends BasicParentAware<O> implements Setter<O, T> {

    private String name;
    private Class[] argTypes;
    private Object[] argDefaults;
    private int index;

    public MethodSetter(String name) {
        this.name = name;
    }

    public MethodSetter(String name, Class[] argTypes, Object[] argDefaults, int index) {
        this.name = name;
        this.argTypes = argTypes;
        this.argDefaults = argDefaults;
        this.index = index;
    }

    private Method getMethod() {
        O object = getParent().getObject();
        try {
            Class[] argTypes = this.argTypes;
            Object[] argDefaults = this.argDefaults;
            if (argTypes == null | argDefaults == null) {
                argTypes = new Class[]{};
                argDefaults = new Object[]{};
            }
            return object.getClass().getMethod(name, argTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void set(Entity<O> entity, Entity<T> value) {
        O object = entity.getObject();
        try {
            Class[] argTypes = this.argTypes;
            Object[] argDefaults = this.argDefaults;
            int index = this.index;
            if (argTypes == null | argDefaults == null) {
                argTypes = new Class[]{value.getClass()};
                argDefaults = new Object[]{null};
                index = 0;
            }
            Method method = object.getClass().getMethod(name, argTypes);
            method.setAccessible(true);
            argDefaults[index] = value.getObject();
            getMethod().invoke(object, argDefaults);
        } catch (Exception e) {
            throw new SetterException(e);
        }
    }

    public Bound<T> getArgumentBound(Class<? extends O> type) {
        return BasicBound.inspect(getMethod().getGenericReturnType());
    }
}
