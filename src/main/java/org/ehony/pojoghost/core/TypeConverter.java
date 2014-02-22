/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core;

public interface TypeConverter<A, B> extends Converter<A, B>, ConverterRegistryAware {

    void map(Field<A, ?> from, Field<B, ?> to, Converter converter);
}
