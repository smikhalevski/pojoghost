/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors.impl;

import java.lang.reflect.Field;
import org.ehony.pojoghost.accessors.*;
import org.ehony.pojoghost.core.*;
import org.ehony.pojoghost.core.impl.BasicBound;

public class BasicSetter<O, T> implements Setter<O, T> {

    private String name;

    public BasicSetter(String name) {
        this.name = name;
    }

    public Field getField(Class type) {
        try {
            Field f = type.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void set(Entity<O> entity, Entity<T> value) {
        try {
            O o = entity.getObject();
            getField(o.getClass()).set(o, value.getObject());
        } catch (Exception e) {
            throw new GetterException(e);
        }
    }

    public Bound<T> getArgumentBound(Class<? extends O> type) {
        return BasicBound.inspect(getField(type).getGenericType());
    }
}