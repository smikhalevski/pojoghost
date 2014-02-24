/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.api.*;
import org.ehony.pojoghost.core.*;

import java.util.List;

import static org.apache.commons.lang3.Validate.notEmpty;

public class StackedSetter<To, Type> implements Setter<To, Type>
{

    private List<Setter> setters;

    public StackedSetter(List<Setter> setters) {
        notEmpty(setters, "Setters expected.");
        this.setters = setters;
    }

    @SuppressWarnings("unchecked")
    public void set(Entity<To> to, Entity<Type> value) {
        Throwable reason = null;
        for (Setter setter : setters) {
            try {
                setter.set(to, value);
            } catch (Throwable t) {
                reason = t;
            }
        }
        throw new AccessException(reason);
    }

    @SuppressWarnings("unchecked")
    public Bound<Type> getArgumentBound(Class<? extends To> type) {
        Throwable cause = null;
        for (Setter setter : setters) {
            try {
                return setter.getArgumentBound(type);
            } catch (Throwable t) {
                if (cause == null) {
                    cause = t;
                }
            }
        }
        throw new IllegalArgumentException(cause);
    }
}
