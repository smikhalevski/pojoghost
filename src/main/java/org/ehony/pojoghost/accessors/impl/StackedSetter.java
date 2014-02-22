/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors.impl;

import org.ehony.pojoghost.accessors.SetterException;
import org.ehony.pojoghost.core.Entity;
import org.ehony.pojoghost.accessors.Setter;
import java.util.List;
import org.ehony.pojoghost.core.*;

public class StackedSetter<O, T> implements Setter<O, T> {

    private List<Setter> setters;

    public StackedSetter(List<Setter> setters) {
        this.setters = setters;
    }

    public void set(Entity<O> entity, Entity<T> value) {
        Throwable reason = null;
        for (Setter setter : setters) {
            try {
                setter.set(entity, value);
            } catch (Throwable t) {
                reason = t;
            }
        }
        throw new SetterException(reason);
    }

    public Bound<T> getArgumentBound(Class<? extends O> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
