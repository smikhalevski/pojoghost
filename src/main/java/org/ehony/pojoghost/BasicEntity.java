/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost;

import org.ehony.pojoghost.api.Entity;

/**
 * Constant object {@link Entity} implementation.
 * @param <O> type of object stored by this entity.
 */
public class BasicEntity<O> implements Entity<O> {

    private O object;

    /**
     * Creates entity holding provided object.
     * @param object object to store.
     */
    public BasicEntity(O object) {
        this.object = object;
    }

    public O getObject() {
        return object;
    }

    @Override
    public boolean isEmpty() {
        return object == null;
    }
}
