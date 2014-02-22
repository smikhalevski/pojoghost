/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

public class SetterException extends RuntimeException {

    public SetterException(Throwable t) {
        super(t);
    }

    public SetterException(String message, Throwable t) {
        super(message, t);
    }
}
