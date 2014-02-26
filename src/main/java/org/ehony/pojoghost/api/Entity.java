/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

/**
 * Implicit object access proxy.
 * <p>Analogue of option pattern in Scala.</p>
 *
 * @param <Type> type of object kept (accessed) by proxy.
 */
public interface Entity<Type>
{

    /**
     * Get actual content of this entity.
     * <p>Exception must be thrown if object cannot be successfully retrieved.</p>
     * 
     * @return Instance of {@link Type} or {@code null} if entity is empty.
     * @see #isEmpty()
     */
    Type getObject();

    /**
     * Returns <code>true</code> if object held by this entity is empty.
     * <p>Definition of &laquo;empty&raquo; may depend on entity content.</p>
     */
    boolean isEmpty();
}