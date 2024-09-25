package net.jmp.util.extra;

/*
 * (#)TestWrappedObject.java    1.0.0   09/25/2024
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
/// @version    1.0.0
/// @since      1.0.0
public final class TestWrappedObject {
    @Test
    public void testEmptyConstructor() {
        final WrappedObject<String> wrappedString = new WrappedObject<>();

        assertNull(wrappedString.get());

        wrappedString.set("Testing 1 2 3");

        assertEquals("Testing 1 2 3", wrappedString.get());
    }

    @Test
    public void testConstructor() {
        final WrappedObject<Boolean> wrappedBoolean = new WrappedObject<>(false);

        assertFalse(wrappedBoolean.get());

        wrappedBoolean.set(true);

        assertTrue(wrappedBoolean.get());
    }

    @Test
    public void testOf() {
        final WrappedObject<Integer> wrappedInteger = WrappedObject.of(17);

        assertEquals(17, (long) wrappedInteger.get());

        wrappedInteger.set(71);

        assertEquals(71, (long) wrappedInteger.get());
    }
}
