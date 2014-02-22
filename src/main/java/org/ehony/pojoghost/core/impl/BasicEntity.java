/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core.impl;

import org.ehony.pojoghost.core.Entity;

public class BasicEntity<O> implements Entity<O> {

    private O object;

    public BasicEntity(O object) {
        this.object = object;
    }

    public O getObject() {
        return object;
    }
}
