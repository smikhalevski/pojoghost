/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core;

public interface ParentAware<O> {

    Entity<O> getParent();
    
    void setParent(Entity<O> entity);
}
