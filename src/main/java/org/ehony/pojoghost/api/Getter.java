/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

import org.ehony.pojoghost.core.*;

/**
 * Abstraction for get accessor mechanism.
 * @param <From> object keeping field accessed by this getter.
 * @param <Type> type of returned object.
 */
public interface Getter<From, Type>
{

    /**
     * Get field proxy for provided object.
     * <p><b>Important:</b> returned entity may be asynchronous and can
     * raise exception on {@link Entity#getObject()} invokation.</p>
     * 
     * @param from entity to apply getter to.
     * @return Entity which can access field described by this getter.
     */
    Entity<Type> get(Entity<From> from);

    /**
     * Get return type bound of this getter.
     * <p>Object encapsulated by {@link #get(Entity) getter}-returned
     * entity is type-compliant with bound returned by this method.</p>
     *
     * @param type class to extract information from.
     * @return Description of getter return type.
     */
    Bound<Type> getReturnBound(Class<? extends From> type);
}
