/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

/**
 * Object conversion interface.
 * @param <From> source type.
 * @param <To> destination type.
 */
public interface Converter<From, To>
{

    To convert(From from);

    To convert(From from, To to);

    /**
     * Perform hinted conversion of given object.
     * <p>Type hint is of use if converter accept interfaces as destination types
     * but a certain implementation may be required as a conversion output.</p>
     * 
     * @param from source object.
     * @param type class to convert source object to.
     * @param <Type> destination type.
     * @return Converted instance implementing <code>type</code>.
     */
    <Type extends To> Type convert(From from, Class<Type> type);
    
    /**
     * Detect weather converter is applicable for translation of given types.
     * @param from source type.
     * @param to destination type.
     * @return <code>true</code> when translation between provided types can
     *         be handled by this converter, <code>false</code> otherwise.
     */
    boolean canConvert(Class<? extends From> from, Class<? extends To> to);
}
