/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

import org.ehony.pojoghost.core.*;

public interface Setter<O, T> {

    void set(Entity<O> entity, Entity<T> value);
    
    Bound<T> getArgumentBound(Class<? extends O> type);
}
