/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.accessors;

public class GetterException extends RuntimeException {

    public GetterException(Throwable t) {
        super(t);
    }
    
    public GetterException(String message, Throwable t) {
        super(message, t);
    }
}
