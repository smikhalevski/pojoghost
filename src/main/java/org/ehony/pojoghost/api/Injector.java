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
     * Instantiates a new instance of the given type possibly injecting values
     * into the object in the process.
     *
     * @param type the type of object to create
     * @return A newly created instance.
     */
    <T> T createInstance(Class<T> type);

    /**
     * Instantiates a new instance of the given object type possibly injecting
     * values into the object in the process.
     *
     * @param type     the type of object to create
     * @param instance an instance of the type to create
     * @return A newly created instance.
     */
    <T> T createInstance(Class<T> type, T instance);
}
