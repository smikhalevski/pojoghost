/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core;

/**
 * Implicit object access proxy.
 * <p>Analogue of option pattern in Scala.</p>
 * @param <T> type of object kept by proxy.
 */
public interface Entity<T> {

    /**
     * Get content of this entity.
     * @return Instance of {@link T}.
     */
    T getObject();
}