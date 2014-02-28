/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost;

import java.util.*;

import org.ehony.pojoghost.api.*;

public class DefaultConverterRegistry implements ConverterRegistry
{

    private Injector injector;
    private Map<Object, Converter> converters = new HashMap<Object, Converter>();

    public <A, B> void register(Converter<? extends A, ? extends B> converter, Object id) {
        if (converter == null) {
            throw new NullPointerException("Converter expected");
        }
        converters.put(id, converter);
    }

    public <A, B> Converter<A, B> lookup(Class<A> from, Class<B> to, Object id) {
        
        
        
        return null;
        
        
        
//        
//        Object current = null;
//        
//        
//        
//        for (Object key : converters.keySet()) {
//            if ((key == null && id == null) || (key.getId() != null && key.getId().equals(id))) {
//                if (key.getFrom().isAssignableFrom(from) && to.isAssignableFrom(key.getTo())) {
//                    if (current != null) {
//                        if (key.getFrom().isAssignableFrom(current.getFrom()) || current.getTo().isAssignableFrom(key.getTo())) {
//                            continue;
//                        }
//                    }
//                    current = key;
//                }
//            }
//        }
//        return converters.get(current);
    }

    public Injector getInjector() {
        if (injector == null) {
            injector = new DefaultInjector();
        }
        return injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }
}