/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.api;

/**
 * Runtime exception indicating field access errors.
 */
public class AccessException extends RuntimeException
{

    public AccessException(Throwable t) {
        super(t);
    }
    
    public AccessException(String message, Throwable t) {
        super(message, t);
    }
}
