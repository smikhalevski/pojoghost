package org.ehony.pojoghost.converters;

import org.ehony.pojoghost.api.Converter;

public class StringToNumberConverter //implements Converter<String, Number>
{

//    @Override
    public Number convert(String s) {
        
        
        return null;
    }

//    @Override
    public Number convert(String s, Number number) {
        return convert(s);
    }

//    @Override
    public boolean canConvert(Class<? extends String> from, Class<? extends Number> to) {
        return true;
    }
}
