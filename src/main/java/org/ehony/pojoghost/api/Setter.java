/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

/**
 * Abstraction of field value assign mechanism.
 * @param <To> type of object keeping field accessed by this setter.
 * @param <Type> type of object accepted by set mechanism.
 */
public interface Setter<To, Type>
{

    /**
     * Set field value.
     * @param to object keeping field.
     * @param value value to set to this field.
     */
    void set(Entity<To> to, Entity<Type> value);

    /**
     * Detect type of argument that setter mechanism accepts.
     * <p>Argument <code>value</code> accepted by {@link #set(Entity, Entity) set(to, value)}
     * is type-compliant with bound returned by this method.</p>
     * <p><b>Implementation considerations:</b> omit throwing exceptions if bound cannot be resolved
     * due to <a href="http://docs.oracle.com/javase/tutorial/java/generics/erasure.html">type erasure</a>
     * and return {@code null} instead.</p>
     *
     * @param type class object to extract information from.
     * @return Description of value type accepted by set mechanism.
     */
    Bound getArgumentBound(Class<? extends To> type);
}
