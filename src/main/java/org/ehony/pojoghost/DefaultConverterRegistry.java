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
    private Map<Key, Converter> converters = new HashMap<Key, Converter>();

    public <A, B> void register(Class<A> from, Class<B> to, Converter<? extends A, ? extends B> converter, Object id) {
        if (converter == null) {
            throw new NullPointerException("Converter expected");
        }
        converters.put(new Key(from, to, id), converter);
    }

    public <A, B> Converter<A, B> lookup(Class<A> from, Class<B> to, Object id) {
        Key current = null;
        for (Key key : converters.keySet()) {
            if ((key.getId() == null && id == null) || (key.getId() != null && key.getId().equals(id))) {
                if (key.getFrom().isAssignableFrom(from) && to.isAssignableFrom(key.getTo())) {
                    if (current != null) {
                        if (key.getFrom().isAssignableFrom(current.getFrom()) || current.getTo().isAssignableFrom(key.getTo())) {
                            continue;
                        }
                    }
                    current = key;
                }
            }
        }
        return converters.get(current);
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

    private class Key {

        private Class<?> from, to;
        private Object id;

        public Key(Class<?> from, Class<?> to, Object id) {
            if (from == null || to == null) {
                throw new NullPointerException("Both From and To classes expected");
            }
            this.from = from;
            this.to = to;
            this.id = id;
        }

        public Class<?> getFrom() {
            return from;
        }

        public Object getId() {
            return id;
        }

        public Class<?> getTo() {
            return to;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Key) {
                Key key = (Key)obj;
                return from.equals(key.getFrom())
                        && to.equals(key.getTo())
                        && ((key.getId() == null && id == null) || (key.getId() != null && key.getId().equals(id)));

            }
            return super.equals(obj);
        }
    }
}