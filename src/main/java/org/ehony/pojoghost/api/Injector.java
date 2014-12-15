/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

/**
 * Strategy for creating and possibly dependency injecting objects
 * which could be implemented using straight forward reflection.
 */
public interface Injector
{

    /**
     * Instantiate an instance of the given type possibly injecting values
     * into the object in the process.
     *
     * @param type type of object to create.
     * @return A newly created instance.
     */
    <Type> Type createInstance(Class<Type> type);

    /**
     * Instantiate an instance of the given object type possibly injecting
     * values into the object in the process.
     *
     * @param type type of object to create.
     * @param instance hinting reference instance.
     * @return A newly created instance.
     */
    <Type> Type createInstance(Class<Type> type, Type instance);
}
