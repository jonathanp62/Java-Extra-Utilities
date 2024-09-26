package net.jmp.demo.util.extra;

/*
 * (#)TestMain.java 1.0.0   09/26/2024
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

import static net.jmp.util.testing.testutil.TestUtils.*;

import static org.junit.Assert.*;

import org.junit.Test;

/// The test class for StreamGatherersDemo.
///
/// @version    1.0.0
/// @since      1.0.0
public final class TestMain {
    @Test
    public void testWrappedObject() throws Exception {
        final var demo = new Main(new String[] {});
        final var method = Main.class.getDeclaredMethod("wrappedObject");

        method.setAccessible(true);

        final Object o = method.invoke(demo);
        final String result = castToType(String.class, o);

        assertNotNull(result);
        assertEquals("Jonathan's wrapped object", result);
    }
}
