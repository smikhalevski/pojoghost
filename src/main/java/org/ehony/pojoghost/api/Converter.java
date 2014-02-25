/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

/**
 * Generic conversion interface.
 * <p>Implementations may convert any types including primitives.</p>
 * 
 * @param <From> source type.
 * @param <To> destination type.
 */
public interface Converter<From, To>
{

    To convert(From from);

    To convert(From from, To to);

    boolean canConvert(Class<? extends From> from, Class<? extends To> to);
}
