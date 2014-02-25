/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

import java.io.Serializable;

/**
 * Implicit object access proxy.
 * <p>Analogue of option pattern in Scala.</p>
 * @param <Type> type of object kept (accessed) by proxy.
 */
public interface Entity<Type> extends Serializable
{

    /**
     * Get content of this entity.
     * @return Instance of {@link Type}.
     */
    Type getObject();
    
    boolean isEmpty();
}