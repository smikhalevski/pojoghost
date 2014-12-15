/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost;

import static junit.framework.Assert.*;

import org.junit.*;

import java.util.List;
import java.util.stream.Collectors;

public class BoundTest
{

    @Test
    public void testNull() throws Exception {
        assertNull(Bound.traverse(null));
    }

    class C1 {}
    @Test
    public void testClass() throws Exception {
        C1 c1 = new C1();
        Bound b = Bound.traverse(c1.getClass());

        assertEquals(b.getType(), C1.class);
        assertEquals(b.getDefinition(), C1.class);
        assertEquals(b.getInterfaces().size(), 0);
        assertEquals(b.getParameters().size(), 0);
        assertEquals(b.getParent(), null);
        assertEquals(b.getSuperclass().getType(), Object.class);
    }

    class C2 extends C1 {}
    @Test
    public void testExtends() throws Exception {
        C2 c2 = new C2();
        Bound b = Bound.traverse(c2.getClass());

        assertEquals(b.getType(), C2.class);
        assertEquals(b.getDefinition(), C2.class);
        assertEquals(b.getInterfaces().size(), 0);
        assertEquals(b.getParameters().size(), 0);
        assertEquals(b.getParent(), null);
        assertEquals(b.getSuperclass().getType(), C1.class);
        assertEquals(b.getSuperclass().getSuperclass().getType(), Object.class);
    }

    interface I1 {}
    interface I2 {}
    class C3 implements I1, I2 {}
    @Test
    public void testImplements() throws Exception {
        C3 c3 = new C3();
        Bound b = Bound.traverse(c3.getClass());

        assertEquals(b.getType(), C3.class);
        assertEquals(b.getDefinition(), C3.class);
        assertEquals(b.getInterfaces().size(), 2);

        List<Object> interfaces = b.getInterfaces().stream().map(Bound::getType).collect(Collectors.toList());
        assertTrue(interfaces.contains(I1.class));
        assertTrue(interfaces.contains(I2.class));

        assertEquals(b.getParameters().size(), 0);
        assertEquals(b.getParent(), null);
        assertEquals(b.getSuperclass().getType(), Object.class);
    }

    class C4<G1, G2> {}
    @Test
    public void testErasure() throws Exception {
        C4<String, Number> c4 = new C4<String, Number>();
        Bound b = Bound.traverse(c4.getClass());

        assertEquals(b.getType(), C4.class);
        assertEquals(b.getDefinition(), C4.class);
        assertEquals(b.getInterfaces().size(), 0);
        assertEquals(b.getParameters().size(), 2);
        assertEquals(b.getParameters().get(0).getType(), Object.class);
        assertEquals(b.getParameters().get(1).getType(), Object.class);
        assertEquals(b.getParent(), null);
        assertEquals(b.getSuperclass().getType(), Object.class);
    }

    class C5 extends C4<String, Number> {}
    @Test
    public void testGenerics() throws Exception {
        C5 c5 = new C5();
        Bound b = Bound.traverse(c5.getClass());

        assertEquals(b.getType(), C4.class);
        assertEquals(b.getDefinition(), C4.class);
        assertEquals(b.getInterfaces().size(), 0);
        assertEquals(b.getParameters().size(), 2);
        assertEquals(b.getParameters().get(0).getType(), Object.class);
        assertEquals(b.getParameters().get(1).getType(), Object.class);
        assertEquals(b.getParent(), null);
        assertEquals(b.getSuperclass().getType(), Object.class);
    }
}
