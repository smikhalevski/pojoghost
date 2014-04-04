package org.ehony.pojoghost;

import org.junit.Test;

import java.util.*;

public class ReflectionBound1Test
{

    @Test
    public void testName() throws Exception {
        
        Bound b = Bound.traverse(null, new ArrayList<String>(){}.getClass());
        
        System.out.println(b);
    }

    //    @Test
//    public void testComplexObject() throws Exception {
//        System.out.println(new ReflectionBound(A.class));
//    }

//    @Test
//    public void testGenericArrayType() throws Exception {
//        System.out.println(new ReflectionBound(int[].class));
//    }
}


class W<T extends C<?>> {}
class A<T extends D & C<Q>, Q extends C<E[]>, E> {}
class D<T>  {}
interface C<T> {}
