/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.api.*;
import org.ehony.pojoghost.BasicEntity;

import java.util.List;

import static org.apache.commons.lang3.Validate.notEmpty;

public class StackedGetter<From, Type> implements Getter<From, Type>
{

    private List<Getter> getters;

    public StackedGetter(List<Getter> getters) {
        notEmpty(getters, "Getters expected.");
        this.getters = getters;
    }

    @SuppressWarnings("unchecked")
    public Entity<Type> get(final Entity<From> from) {
        Throwable cause = null;
        for (Getter getter : getters) {
            try {
                return new BasicEntity<Type>((Type) getter.get(from).getObject());
            } catch (Throwable t) {
                if (cause == null) {
                    cause = t;
                }
            }
        }
        throw new AccessException(cause);
    }

    @SuppressWarnings("unchecked")
    public Bound<Type> getReturnBound(Class<? extends From> type) {
        Throwable cause = null;
        for (Getter getter : getters) {
            try {
                return getter.getReturnBound(type);
            } catch (Throwable t) {
                if (cause == null) {
                    cause = t;
                }
            }
        }
        throw new IllegalArgumentException(cause);
    }
}
