/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.converters;

import org.ehony.pojoghost.api.Converter;

public class ObjectToStringConverter //implements Converter<Object, String>
{

//    @Override
    public String convert(Object o) {
        return String.valueOf(o);
    }

//    @Override
    public String convert(Object o, String s) {
        return convert(o);
    }

//    @Override
    public boolean canConvert(Class<?> from, Class<? extends String> to) {
        return true;
    }
}
