/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors.impl;

import java.util.List;
import org.ehony.pojoghost.accessors.*;
import org.ehony.pojoghost.core.*;
import org.ehony.pojoghost.core.impl.BasicEntity;

public class StackedGetter<O, T> implements Getter<O, T> {

    private List<Getter> getters;

    public StackedGetter(List<Getter> getters) {
        this.getters = getters;
    }

    public Entity<T> get(final Entity<O> entity) {
        Throwable reason = null;
        for (Getter getter : getters) {
            try {
                return new BasicEntity(getter.get(entity).getObject());
            } catch (Throwable t) {
                reason = t;
            }
        }
        throw new GetterException(reason);
    }

    public Bound<T> getReturnBound(Class<? extends O> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
