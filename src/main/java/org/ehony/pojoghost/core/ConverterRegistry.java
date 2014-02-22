/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core;

public interface ConverterRegistry {

    /**
     * Registers a new type converter
     *
     * @param from the type to convert from
     * @param to the type to convert to
     * @param converter the type converter to use
     */
    <A, B> void register(Class<A> from, Class<B> to, Converter<? extends A, ? extends B> converter, Object id);

    /**
     * Performs a lookup for a given type converter.
     *
     * @param from the type to convert from
     * @param to the type to convert to
     * @return the type converter or <tt>null</tt> if not found.
     */
    <A, B> Converter<A, B> lookup(Class<A> from, Class<B> to, Object id);

    /**
     * Sets the injector to be used for creating new instances during type
     * conversions.
     *
     * @param injector the injector
     */
    void setInjector(Injector injector);

    /**
     * Gets the injector
     *
     * @return the injector
     */
    Injector getInjector();
}
