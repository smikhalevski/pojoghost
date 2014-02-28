/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

/**
 * Abstraction for get accessor mechanism.
 * @param <From> object keeping field accessed by this getter.
 * @param <Type> type of returned object.
 */
public interface Getter<From, Type>
{

    /**
     * Get field proxy for provided object.
     * <p>Returned entity may be asynchronous, so exception may be raised later on,
     * during {@link Entity#getObject()} invocation.</p>
     *
     * @param from entity to apply getter to.
     * @return Proxy to actual field value described by this getter.
     */
    Entity<Type> get(Entity<From> from);

    /**
     * Detect type of value that getter mechanism returns.
     * <p>Proxy returned by {@link #get(Entity)} is type-compliant with bound returned by this method.</p>
     * <p><b>Implementation considerations:</b> omit throwing exceptions if bound cannot be resolved
     * due to <a href="http://docs.oracle.com/javase/tutorial/java/generics/erasure.html">type erasure</a>
     * and return {@code null} instead.</p>
     *
     * @param type class object to extract information from.
     * @return Description of value type returned by get mechanism.
     */
    Bound<Type> getReturnBound(Class<? extends From> type);
}
