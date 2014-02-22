/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core;

public interface Converter<A, B> {

    B convert(A from);

    B convert(A from, B to);
}
