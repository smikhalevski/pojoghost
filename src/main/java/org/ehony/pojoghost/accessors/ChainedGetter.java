/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.api.Getter;
import org.ehony.pojoghost.core.*;
import org.ehony.pojoghost.core.impl.BasicEntity;

import java.util.*;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Sequentially execute multiple getters, ex. <code>object.a.getB().get(3)</code>.
 * {@inheritDoc}
 */
public class ChainedGetter<From, To> implements Getter<From, To>
{

    private Collection<Getter> getters;

    public ChainedGetter(Collection<Getter> getters) {
        notEmpty(getters, "Getters expected.");
        this.getters = getters;
    }

    @SuppressWarnings("unchecked")
    public Entity<To> get(Entity<From> from) {
        Entity bridge = from;
        for (Getter getter : getters) {
            bridge = getter.get(bridge);
        }
        return new BasicEntity(bridge.getObject());
    }

    /**
     * {@inheritDoc}
     * <p>List of backing getters is iterated and return bounds are sequentially extracted.</p>
     * 
     * @param type class to extract information from, must conform first getter in backing list.
     * @return Description of latest getter return type.
     */
    @SuppressWarnings("unchecked")
    public Bound<To> getReturnBound(Class<? extends From> type) {
        Bound bound = null;
        Class<?> bridge = type;
        for (Getter getter : getters) {
            bound = getter.getReturnBound(bridge);
            bridge = bound.getType();
        }
        return bound;
    }
}
