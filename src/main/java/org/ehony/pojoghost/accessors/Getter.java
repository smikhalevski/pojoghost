/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.core.*;

public interface Getter<O, T> {

    Entity<T> get(Entity<O> entity);
    
    Bound<T> getReturnBound(Class<? extends O> type);
}
