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
     */
    Type getType();

    /**
     * Get bound that represents extended superclass.
     */
    Bound getParent();

    /**
     * Get bounds of generic parameters specified for this bound.
     * Empty list must be returned if bound is not parameterized.
     */
    List<Bound> getParameters();

    /**
     * Get bounds directly implemented or extended by this bound.
     */
    List<Bound> getInterfaces();

    /**
     * Get bound for interface or superclass implemented or extended by this bound.
     * @param type class to look for in bounds hierarchy.
     * @return Bound that represents given type.
     */
    Bound findImplementedBoundOfType(Class<?> type);
}
