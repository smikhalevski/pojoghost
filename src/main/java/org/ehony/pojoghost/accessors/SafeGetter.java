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
 * Decorator for entity <code>emptiness</code>-safe getter.
 * {@inheritDoc}
 */
public class SafeGetter<From, Type> implements Getter<From, Type>
{

    private Getter<From, Type> getter;

    public SafeGetter(Getter<From, Type> getter) {
        notNull(getter, "Getter expected.");
        this.getter = getter;
    }

    @Override
    public Entity<Type> get(Entity<From> from) {
        if (from.isEmpty()) {
            return null;
        }
        return getter.get(from);
    }

    @Override
    public Bound<Type> getReturnBound(Class<? extends From> type) {
        return getter.getReturnBound(type);
    }
}
