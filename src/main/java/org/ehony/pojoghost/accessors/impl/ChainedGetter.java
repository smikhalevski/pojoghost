/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors.impl;

import org.ehony.pojoghost.core.Entity;
import org.ehony.pojoghost.accessors.Getter;
import java.util.List;
import org.ehony.pojoghost.core.*;
import org.ehony.pojoghost.core.impl.BasicEntity;

public class ChainedGetter<O, T> implements Getter<O, T> {

    private List<Getter> getters;

    public ChainedGetter(List<Getter> getters) {
        this.getters = getters;
    }

    public Entity<T> get(final Entity<O> entity) {
        Entity bridge = entity;
        for (Getter getter : getters) {
            bridge = getter.get(bridge);
        }
        return new BasicEntity(bridge.getObject());
    }

    public Bound<T> getReturnBound(Class<? extends O> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
