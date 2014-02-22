/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors.impl;

import java.lang.reflect.Method;
import org.ehony.pojoghost.accessors.Getter;
import org.ehony.pojoghost.core.*;
import org.ehony.pojoghost.core.impl.*;

public class MethodGetter<O, T> extends BasicParentAware<O> implements Getter<O, T> {

    private String name;
    private Class[] argTypes;
    private Object[] argDefaults;

    public MethodGetter(String name) {
        this.name = name;
    }

    public MethodGetter(String name, Class[] argTypes, Object[] argDefaults) {
        this.name = name;
        this.argTypes = argTypes;
        this.argDefaults = argDefaults;
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

    public Entity<T> get(Entity<O> entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Bound<T> getReturnBound(Class<? extends O> type) {
        return BasicBound.inspect(getMethod().getGenericReturnType());
    }
}
