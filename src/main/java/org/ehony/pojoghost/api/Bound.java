/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

import java.lang.reflect.Type;
import java.util.List;

public interface Bound<A> {

    Class<A> getType();
    
    Type getRawType();

    List<Bound> getParameterBounds();

    List<Bound> getImplemetedBounds();
    
    <B> Bound<B> findImplemetedBoundOfType(Class<B> type);
}
