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
import org.ehony.pojoghost.core.impl.*;

public class BasicGetter<O, T> implements Getter<O, T> {

    private String name;

    public BasicGetter(String name) {
        this.name = name;
    }

    public Field getField(Class<?> type) {
        try {
            Field f = type.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Entity<T> get(Entity<O> entity) {
        try {
            O o = entity.getObject();
            return new BasicEntity(getField(o.getClass()).get(o));
        } catch (Exception e) {
            throw new GetterException(e);
        }
    }

    public Bound<T> getReturnBound(Class<? extends O> type) {
        return BasicBound.inspect(getField(type).getGenericType());
    }
}