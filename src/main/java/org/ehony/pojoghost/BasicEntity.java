/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost;

import org.ehony.pojoghost.api.Entity;

public class BasicEntity<O> implements Entity<O> {

    private O object;

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
