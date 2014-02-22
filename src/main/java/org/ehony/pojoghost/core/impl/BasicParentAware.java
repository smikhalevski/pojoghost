/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core.impl;

import org.ehony.pojoghost.core.*;

public class BasicParentAware<O> implements ParentAware<O> {

    private Entity<O> entity;
    
    public Entity<O> getParent() {
        return entity;
    }
    
    public void setParent(Entity<O> entity) {
        this.entity = entity;
    }
}
