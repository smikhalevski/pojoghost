/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost;

import org.ehony.pojoghost.api.*;
import org.junit.Test;
import org.ehony.pojoghost.accessors.ImmediateGetter;
import org.ehony.pojoghost.accessors.ImmediateSetter;
import org.ehony.pojoghost.core.Converter;
import org.ehony.pojoghost.core.Field;
import org.ehony.pojoghost.core.ConverterRegistry;
import org.ehony.pojoghost.core.TypeConverter;
import org.ehony.pojoghost.core.impl.*;

public class GhostTest {
    
    public static class Foo {

        public String a = "100";
    }

    public static class Bar {

        public Integer b;
    }

    public class StringToIntegerConverter implements Converter<String, Integer> {

        public Integer convert(String from) {
            return convert(from, 0);
        }

        public Integer convert(String from, Integer to) {
            return Integer.parseInt(from)/2;
        }
    }

    @Test
    public void method() throws Exception {
        ConverterRegistry registry = new DefaultConverterRegistry();

        Getter<Foo, String> aGetter = new ImmediateGetter<Foo, String>("a");
        Setter<Foo, String> bSetter = new ImmediateSetter<Foo, String>("b");
        Field a = new BasicField<Foo, String>(aGetter, null);
        Field b = new BasicField<Foo, String>(null, bSetter);

        TypeConverter<Foo, Bar> fooToBarConverter = new DefaultTypeConverter<Foo, Bar>(Foo.class, Bar.class);
        fooToBarConverter.map(a, b, new StringToIntegerConverter());
        fooToBarConverter.setRegistry(registry);

        registry.register(String.class, Integer.class, new StringToIntegerConverter(), null);
        registry.register(Foo.class, Bar.class, fooToBarConverter, null);

        System.out.println("Bar#b = " + registry.lookup(Foo.class, Bar.class, null).convert(new Foo(), new Bar()).b);
    }
}
