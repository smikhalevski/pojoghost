/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Interface for reflection-based traversal.
 */
public interface Bound {

    /**
     * Get actual class instance that this bound represents.
     * @return Instance of {@link Class} or {@code null} is bound is a generic type.
     */
    Type getType();

    Bound getParent();

    /**
     * Get bounds of generic parameters specified for this bound.
     * 
     */
    List<Bound> getParameters();

    /**
     * Get bounds directly implemented or extended by this bound.
     */
    List<Bound> getInterfaces();

    /**
     * Get bound for interface of super class implemented or extended by this bound.
     * @param type class to look for in hierarchy.
     * @return Bound that represents given type of {@code null} if 
     */
    Bound findImplementedBoundOfType(Class<?> type);
}
