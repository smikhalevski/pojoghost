package org.ehony.pojoghost;

import org.junit.Test;

public class ReflectionBoundTest
{


    
//    @Test
//    public void testComplexObject() throws Exception {
//        System.out.println(new ReflectionBound<A>(A.class));
//    }
    
    
    
    @Test
    public void testGenericArrayType() throws Exception {
        System.out.println(new ReflectionBound(int[].class));
    }
    
    
}


class A<T extends B & C> {}
class B<T> implements C {}
interface C {}