package gspd.ispd.model.dependencies;

import static org.junit.Assert.*;

import org.junit.Test;

public class DependencyNodeTest {

    @Test
    public void testDepedency() {
        DependencyNode a = new DependencyNode();
        DependencyNode b = new DependencyNode();
        // a and b are independents
        assertFalse(a.isDependencyOf(b));
        b.addDependency(a);
        // now a is dependency of b
        assertTrue(a.isDependencyOf(b));
        // that is, b depends on a
        assertTrue(b.dependsOn(a));
        // a is free of dependencies
        assertTrue(a.isFree());
        // b is not free (once depends on a)
        assertFalse(b.isFree());
        // a is done, then disconects b
        a.disconectDependents();
        // now b is free
        assertTrue(b.isFree());
        // a and b are not interdependents anymore
        assertFalse(a.isDependencyOf(b));
        b.addDependent(a);
        // now a is dependent of b
        // NOTE: it is using addDependent and not addDependency
        assertTrue(a.dependsOn(b));
        assertFalse(a.isFree());
        assertTrue(b.isFree());
        b.disconectDependents();
        assertTrue(a.isFree());
        // reconnect a -> b, and remove the connection
        b.addDependency(a);
        assertTrue(a.isDependencyOf(b));
        b.removeDependency(a);
        assertFalse(a.isDependencyOf(b));

    }

    @Test
    public void testReferencing() {
        DependencyNode d1 = new DependencyNode("T1");
        DependencyNode d2 = new DependencyNode("T2");
        DependencyNode d3 = new DependencyNode("T3");

        assertTrue(d1.addDependency(d2));
        assertTrue(d2.addDependency(d3));
        assertTrue(d3.addDependency(d1));
        assertTrue(d1.addDependency(d1));

        assertEquals("T1", d1.getReference());
        assertEquals("T2", d2.getReference());
        assertEquals("T3", d3.getReference());

    }
}
