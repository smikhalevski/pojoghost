/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

/**
 * Lookup capable dictionary of converters.
 */
public interface ConverterRegistry
{

    /**
     * Register a new type converter.
     * @param converter Type converter to use.
     * @param id optional converter identifier.
     */
    <From, To> void register(Converter<? extends From, ? extends To> converter, Object id);

    /**
     * Perform a lookup for a given type converter.
     * @param from type to convert from.
     * @param to type to convert to.
     * @return Type converter or {@code null} if not found.
     */
    <From, To> Converter<From, To> lookup(Class<From> from, Class<To> to, Object id);

    /**
     * Set the injector to be used for creating new instances during type conversions.
     * @param injector the injector.
     */
    void setInjector(Injector injector);

    /**
     * Get the injector.
     */
    Injector getInjector();
}
