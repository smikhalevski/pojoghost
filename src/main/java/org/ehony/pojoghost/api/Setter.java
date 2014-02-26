/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

/**
 * Abstraction for set accessor mechanism.
 * @param <To> object keeping field accessed by this setter.
 * @param <Type> type of accepted object.
 */
public interface Setter<To, Type>
{

    /**
     * Immediately set field value.
     * @param to object keeping field.
     * @param value value to set to this field.
     */
    void set(Entity<To> to, Entity<Type> value);

    /**
     * Get type of argument that setter accepts.
     * <p>Value argument accepted by {@link #set(Entity, Entity)}
     * entity is type-compliant with bound returned by this method.</p>
     *
     * @param type class to extract information from.
     * @return Description of type accepted by set method.
     */
    Bound<Type> getArgumentBound(Class<? extends To> type);
}
