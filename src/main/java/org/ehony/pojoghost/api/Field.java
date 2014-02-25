/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

public interface Field<O, T> {

    /**
     * Get parent of field
     * @return
     */
    Entity<O> getParent();
    
    void setParent(Entity<O> entity);
    
    boolean canGet();
    
    boolean canSet();
    
    Entity<T> get();
    
    void set(Entity<T> value);
    
    Bound<T> getReturnBound();
    
    Bound<T> getArgumentBound();
}
