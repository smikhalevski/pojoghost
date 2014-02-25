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
     * Get content of this entity.
     * @return Instance of {@link Type}.
     */
    Type getObject();

    /**
     * Returns <code>true</code> if object held by this entity is empty.
     * <p>Definition of empty may vary depending on context and object.</p>
     */
    boolean isEmpty();
}