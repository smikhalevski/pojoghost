/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core.impl;

import org.ehony.pojoghost.accessors.*;
import org.ehony.pojoghost.core.*;

public class BasicField<O, T> extends BasicParentAware<O> implements Field<O, T> {

    private Getter<O, T> getter;
    private Setter<O, T> setter;

    public BasicField(Getter<O, T> getter, Setter<O, T> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public boolean canGet() {
        return getter instanceof Getter;
    }

    public boolean canSet() {
        return setter instanceof Setter;
    }

    public Entity<T> get() {
        if (canGet()) {
            return getter.get(getParent());
        }
        throw new UnsupportedOperationException();
    }

    public void set(Entity<T> value) {
        if (canSet()) {
            setter.set(getParent(), value);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public Bound<T> getArgumentBound() {
        if (canSet()) {
//            return setter.getArgumentBound(getParent().getObjectType());
        }
        throw new UnsupportedOperationException();
    }

    public Bound<T> getReturnBound() {
        if (canSet()) {
//            return getter.getReturnBound(getParent().getObjectType());
        }
        throw new UnsupportedOperationException();
    }
}
