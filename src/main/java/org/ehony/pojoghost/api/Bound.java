/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

import java.util.List;

/**
 * Interface for reflection-based traversal.
 * @param <Type> type processed by this bound.
 */
public interface Bound<Type> {

    /**
     * Get actual class instance that this bound represents.
     * @return Instance of {@link Class} or {@code null} is bound is a generic type.
     */
    Class<Type> getType();

    /**
     * Get the common interface for all type this bound represents.
     * @return Raw type, parameterized type, array type, type variable
     *         or primitive type, never {@code null}.
     */
    java.lang.reflect.Type getRawType();

    /**
     * Get bounds of generic parameters specified for this bound.
     */
    List<Bound> getBoundsOfGenericParameters();

    /**
     * Get bounds directly implemented or extended by this bound.
     */
    List<Bound> getImplementedBounds();

    /**
     * Get bound for interface of super class implemented or extended by this bound.
     * @param type class to look for in hierarchy.
     * @return Bound that represents given type of {@code null} if 
     */
    <T> Bound<T> findImplementedBoundOfType(Class<T> type);
}
