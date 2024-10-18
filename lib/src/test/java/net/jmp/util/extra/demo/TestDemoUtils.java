package net.jmp.util.extra.demo;

/*
 * (#)TestDemoUtils.java    1.4.0   10/18/2024
 *
 * MIT License
 *
 * Copyright (c) 2024 Jonathan M. Parker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.junit.Test;

import static org.junit.Assert.*;

/// The test class for WrappedObject.
///
/// @version    1.4.0
/// @since      1.4.0
public final class TestDemoUtils {
    @Test
    public void testGetDemoClassVersionFromAnnotatedClass() throws DemoUtilException {
        final double version = DemoUtils.getDemoClassVersion("net.jmp.util.extra.demo.AnnotatedDemoClass");

        assertEquals(1.4, version, 0.01);
    }

    @Test
    public void testGetDemoClassVersionFromUnannotatedClass() throws DemoUtilException {
        final double version = DemoUtils.getDemoClassVersion("net.jmp.util.extra.demo.UnannotatedDemoClass");

        assertEquals(0, version, 0.01);
    }

    @Test(expected = DemoUtilException.class)
    public void testGetDemoClassVersionClassNotFound() throws DemoUtilException {
        DemoUtils.getDemoClassVersion("net.jmp.util.extra.demo.ClassNotFound");
    }

    @Test
    public void testRunDemoClassVoidMethod() throws DemoUtilException {
        DemoUtils.runDemoClassMethod("net.jmp.util.extra.demo.AnnotatedDemoClass", "someVoidMethod", Void.class);
    }

    @Test
    public void testRunDemoClassStringMethod() throws DemoUtilException {
        final String value = DemoUtils.runDemoClassMethod("net.jmp.util.extra.demo.AnnotatedDemoClass", "someStringMethod", String.class);

        assertEquals("Some string", value);
    }

    @Test(expected = DemoUtilException.class)
    public void testRunDemoClassMethodClassNotFound() throws DemoUtilException {
        DemoUtils.runDemoClassMethod("net.jmp.util.extra.demo.ClassNotFound", "someVoidMethod", Void.class);
    }

    @Test(expected = DemoUtilException.class)
    public void testRunDemoClassMethodMethodNotFound() throws DemoUtilException {
        DemoUtils.runDemoClassMethod("net.jmp.util.extra.demo.AnnotatedDemoClass", "methodNotFound", Void.class);
    }
}
