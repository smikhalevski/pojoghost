/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.api.*;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Decorator for entity <code>emptiness</code>-safe setter.
 * {@inheritDoc}
 */
public class SafeSetter<To, Type> implements Setter<To, Type>
{

    private Setter<To, Type> setter;

    public SafeSetter(Setter<To, Type> setter) {
        this.setter = setter;
    }

    @Override
    public void set(Entity<To> to, Entity<Type> value) {
        if (to.isEmpty()) {
            return;
        }
        setter.set(to, value);
    }

    @Override
    public Bound getArgumentBound(Class<? extends To> type) {
        return setter.getArgumentBound(type);
    }
}
