package org.lff;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestUtility {
    @Test
    public void test1() {
        String s = "abc";
        String r = Utility.removeLeading(s);
        Assertions.assertEquals(s, r);
    }
    @Test
    public void test2() {
        String s = "/abc";
        String r = Utility.removeLeading(s);
        Assertions.assertEquals("abc", r);
    }
    @Test
    public void test3() {
        String s = "/////abc";
        String r = Utility.removeLeading(s);
        Assertions.assertEquals("abc", r);
    }

    @Test
    public void test4() {
        String s = "abc///";
        String r = Utility.removeTailing(s);
        Assertions.assertEquals("abc", r);
    }
    @Test
    public void test5() {
        String s = "abc/";
        String r = Utility.removeTailing(s);
        Assertions.assertEquals("abc", r);
    }
    @Test
    public void test6() {
        String s = "abc";
        String r = Utility.removeTailing(s);
        Assertions.assertEquals("abc", r);
    }
}
