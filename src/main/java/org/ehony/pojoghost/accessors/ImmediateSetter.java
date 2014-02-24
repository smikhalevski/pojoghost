/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.api.*;
import org.ehony.pojoghost.core.*;
import org.ehony.pojoghost.core.impl.BasicBound;

import java.lang.reflect.Field;

import static org.apache.commons.lang3.Validate.notBlank;

public class ImmediateSetter<To, Type> implements Setter<To, Type>
{

    private String name;

    public ImmediateSetter(String name) {
        notBlank(name, "Field name expected.");
        this.name = name;
    }

    public Field getField(Class type) {
        try {
            Field f = type.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            throw new AccessException("Expected field " + name + " at " + type, e);
        }
    }

    public void set(Entity<To> to, Entity<Type> value) {
        To o = to.getObject();
        try {
            getField(o.getClass()).set(o, value.getObject());
        } catch (IllegalAccessException e) {
            throw new AccessException("Cannot set field " + name + " to " + o, e);
        }
    }

    @SuppressWarnings("unchecked")
    public Bound<Type> getArgumentBound(Class<? extends To> type) {
        return BasicBound.inspect(getField(type).getGenericType());
    }
}
