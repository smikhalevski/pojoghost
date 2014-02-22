/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core;

public interface Field<O, T> extends ParentAware<O> {

    boolean canGet();
    
    boolean canSet();
    
    Entity<T> get();
    
    void set(Entity<T> value);
    
    Bound<T> getReturnBound();
    
    Bound<T> getArgumentBound();
}
