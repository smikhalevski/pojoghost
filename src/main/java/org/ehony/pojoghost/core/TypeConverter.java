/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core;

public interface TypeConverter<A, B> extends Converter<A, B> {

    void map(Field<A, ?> from, Field<B, ?> to, Converter converter);
    
    ConverterRegistry getRegistry();
    
    void setRegistry(ConverterRegistry registry);
}
