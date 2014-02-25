/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.api.*;
import org.ehony.pojoghost.BasicEntity;

import java.util.Collection;

import static org.apache.commons.lang3.Validate.*;

/**
 * Decorator for sequential execution of multiple getters,
 * ex. <code>object.a.getB().<b>get(3)</b></code>.
 * {@inheritDoc}
 */
public class ChainedGetter<From, To> implements Getter<From, To>
{

    private Collection<Getter> getters;

    /**
     * Build chain of getters.
     * @param getters backing list of getters. 
     */
    public ChainedGetter(Collection<Getter> getters) {
        notEmpty(getters, "Getters expected.");
        this.getters = getters;
    }

    @SuppressWarnings("unchecked")
    public Entity<To> get(Entity<From> from) {
        Entity f = from;
        for (Getter getter : getters) {
            f = getter.get(f);
        }
        return new BasicEntity(f.getObject());
    }

    /**
     * {@inheritDoc}
     * <p>List of backing getters is iterated and bounds are extracted sequentially.
     * Exception may occur in case generic type was not explicitly specified.</p>
     * 
     * @param type class to extract information from, must conform first getter in backing list.
     * @return Description of latest getter return type.
     */
    @SuppressWarnings("unchecked")
    public Bound<To> getReturnBound(Class<? extends From> type) {
        Class t = type;
        int i = getters.size();
        for (Getter getter : getters) {
            Bound bound = getter.getReturnBound(t);
            if (--i == 0) {
                return bound;
            }
            t = bound.getType();
        }
        throw new IllegalStateException("Getters expected.");
    }
}
