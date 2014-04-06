/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost;

import org.junit.Test;

public class BoundTest<T>
{

    @Test
    public void testName() throws Exception {

        B4<? extends Number> a = new B4<Number>();


        Bound b = Bound.traverse(null, a.getClass());
        System.out.println(b);
    }
}

class B4<Q> extends A4<Q> {}
class A4<T> implements D1<T[]> {}

class A3<T1 extends Number> extends B3<Number, T1>{}
class B3<T2, T3 extends T2> {}

class A2 extends B2<A2> {}
class B2<T extends B2<T>> {}

class A1<T1> extends B1<T1> implements D1<T1> {}
class B1<T2> implements C1<T2> {}
interface C1<T3> extends D1<T3> {}
interface D1<T> {}
