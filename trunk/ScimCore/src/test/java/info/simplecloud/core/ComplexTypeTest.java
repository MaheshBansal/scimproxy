package info.simplecloud.core;

import java.util.ArrayList;
import java.util.List;

import info.simplecloud.core.coding.handlers.StringHandler;
import info.simplecloud.core.execeptions.FailedToGetValue;
import info.simplecloud.core.execeptions.UnknownAttribute;
import info.simplecloud.core.types.ComplexType;
import info.simplecloud.core.types.PluralType;

import org.junit.Assert;
import org.junit.Test;

public class ComplexTypeTest {

    private class ComplexTestType extends ComplexType {
        @Attribute(schemaName="simpleAttribute", codingHandler=StringHandler.class)
        public void getSimpleAttribute() {
        
        }
        
        @Attribute(schemaName="complexAttribute", codingHandler=StringHandler.class)
        public void getComplexAttribute(){
            
        }
        
        @Attribute(schemaName="pluralAttribute", codingHandler=StringHandler.class)
        public void getPluralAttribute(){
            
        }
    }

    @Test
    public void getAttribute() throws UnknownAttribute {
        Object test = new Object();
        ComplexType ct = new ComplexTestType();
        ct.setAttribute("test1", "Hello")
                .setAttribute("test2", test)
                .setAttribute("test3", test)
                .setAttribute(
                        "parent",
                        new ComplexTestType().setAttribute("child", new ComplexTestType().setAttribute("grandchild1", "World")
                                .setAttribute("grandchild2", "G2")));

        Assert.assertNotNull(ct.getAttribute("parent"));
        Assert.assertNotNull(ct.getAttribute("parent.child"));
        Assert.assertEquals("World", ct.getAttribute("parent.child.grandchild1"));
        Assert.assertEquals("G2", ct.getAttribute("parent.child.grandchild2"));
        Assert.assertEquals("Hello", ct.getAttribute("test1"));
        Assert.assertNull(ct.getAttribute("unknown"));
        Assert.assertNull(ct.getAttribute("parent.child.unknown"));
    }

    @Test
    public void equals() {
        ComplexType ct = new ComplexTestType().setAttribute("testid1", "Test value 1");
        ComplexType ctEquals1 = new ComplexTestType().setAttribute("testid1", "Test value 1");
        ComplexType ctEquals2 = ct;
        ComplexType ctNotEquals1 = new ComplexTestType().setAttribute("testid1", "Test value not equals");
        ComplexType ctNotEquals2 = new ComplexTestType().setAttribute("notFound", "Test value 1");
        ComplexType ctNotEquals3 = new ComplexTestType().setAttribute("testid1", "Test value 1").setAttribute("testid2", "Test value 2");
        ComplexType ctNotEquals4 = new ComplexTestType().setAttribute("testid1", new Object());

        Assert.assertTrue(ct.equals(ctEquals1));
        Assert.assertTrue(ct.equals(ctEquals2));
        Assert.assertTrue(ct.equals(ctEquals1));
        Assert.assertTrue(ct.equals(ctEquals1));
        Assert.assertTrue(ct.equals(ctEquals1));

        Assert.assertFalse(ct.equals(new Object()));
        Assert.assertFalse(ct.equals(ctNotEquals1));
        Assert.assertFalse(ct.equals(ctNotEquals2));
        Assert.assertFalse(ct.equals(ctNotEquals3));
        Assert.assertFalse(ct.equals(ctNotEquals4));
    }

    @Test
    public void merge() throws FailedToGetValue {

        ComplexType from = new ComplexTestType().setAttribute("simpleAttribute", "Test value from");
        ComplexType to = new ComplexTestType().setAttribute("to", "Test value to");

        to.merge(from);
        Assert.assertEquals("Test value to", to.getAttribute("to"));
        Assert.assertEquals("Test value from", to.getAttribute("simpleAttribute"));

        from = new ComplexTestType().setAttribute("simpleAttribute", "Hello").setAttribute("complexAttribute",
                new ComplexTestType().setAttribute("simpleAttribute", "World"));

        to.merge(from);

        Assert.assertNotNull(to.getAttribute("complexAttribute"));
        Assert.assertEquals(((ComplexType) to.getAttribute("complexAttribute")).getAttribute("simpleAttribute"), "World");

        List<PluralType<String>> pluralList = new ArrayList<PluralType<String>>();
        pluralList.add(new PluralType<String>("nisse@kalle.com", "home", true));
        
        
        from = new ComplexTestType().setAttribute("pluralAttribute", pluralList);
        List<PluralType<String>> pluralListExpected = new ArrayList<PluralType<String>>();
        pluralListExpected.add(new PluralType<String>("nisse@kalle.com", "home", true));

        to.merge(from);
        Assert.assertEquals(to.getAttribute("pluralAttribute"), pluralListExpected);
    }
}
