/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core.impl;

import java.util.*;
import org.ehony.pojoghost.core.*;

public class DefaultTypeConverter<A, B> implements TypeConverter<A, B> {

    private Class<A> aClass;
    private Class<B> bClass;
    private ConverterRegistry registry;
    private List<FieldMapping> mappings = new ArrayList<FieldMapping>();

    public DefaultTypeConverter(Class<A> aClass, Class<B> bClass) {
        this.aClass = aClass;
        this.bClass = bClass;
    }
    
    public void map(Field<A, ?> from, Field<B, ?> to, Converter converter) {
        mappings.add(new FieldMapping(from, to, converter));
    }

    public B convert(A from) {
        return convert(from, (B)getRegistry().getInjector().newInstance(bClass));
    }

    public B convert(A from, B to) {
        for (FieldMapping mapping : mappings) {
            mapping.getFrom().setParent(new BasicEntity(from));
            mapping.getTo().setParent(new BasicEntity(to));
            
            Object fromObject = mapping.getFrom().get().getObject();
            Object toObject;

            Converter converter = mapping.getConverter();
            if (converter == null) {
                converter = getRegistry().lookup(mapping.getFrom().getReturnBound().getType(), mapping.getTo().getArgumentBound().getType(), null);
            }
            if (converter == null) {
                if (mapping.getTo().getArgumentBound().getType().isAssignableFrom(mapping.getFrom().getReturnBound().getType())) {
                    return (B)from;
                } else {
                    throw new IllegalStateException("Unexpected conversion");
                }
            }
            if (mapping.getTo().canGet()) {
                toObject = converter.convert(fromObject, mapping.getTo().get().getObject());
            } else {
                toObject = converter.convert(fromObject);
            }
            mapping.getTo().set(new BasicEntity(toObject));
        }
        return to;
    }

    public ConverterRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(ConverterRegistry registry) {
        this.registry = registry;
    }

    private class FieldMapping {

        private Field from, to;
        private Converter converter;

        public FieldMapping(Field from, Field to, Converter converter) {
            this.from = from;
            this.to = to;
            this.converter = converter;
        }

        public Converter getConverter() {
            return converter;
        }

        public Field getFrom() {
            return from;
        }

        public Field getTo() {
            return to;
        }
    }
}
