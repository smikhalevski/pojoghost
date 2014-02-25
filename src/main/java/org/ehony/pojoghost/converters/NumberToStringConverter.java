package org.ehony.pojoghost.converters;

import org.ehony.pojoghost.api.Converter;

import java.text.DecimalFormat;

public class NumberToStringConverter implements Converter<Number, String>
{

    private DecimalFormat format;

    public NumberToStringConverter(DecimalFormat format) {
        this.format = format;
    }

    @Override
    public String convert(Number number) {
        if (format != null) {
            return format.format(number.doubleValue());
        } else {
            return String.valueOf(number);
        }
    }

    @Override
    public String convert(Number number, String s) {
        return convert(number);
    }

    @Override
    public boolean canConvert(Class<? extends Number> from, Class<? extends String> to) {
        return true;
    }
}
