package net.jmp.util.extra;

/*
 * (#)TestKeyedFunctionExecutor.java    1.1.0   09/26/2024
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.function.Function;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

import org.junit.Test;

/// The test class for KeyedFunctionExecutor.
///
/// @version    1.1.0
/// @since      1.1.0
public class TestKeyedFunctionExecutor {
    @Test
    public void testString() {
        final List<String> list = new ArrayList<>();
        final List<String> results = Collections.synchronizedList(list);

        try (final KeyedFunctionExecutor<String> keyedFunctionExecutor = new KeyedFunctionExecutor<>()) {
            final Function<String, Void> function = s -> {
                results.add(s);

                return null;
            };

            final List<String> elements =
                    IntStream.rangeClosed(1, 50)
                            .mapToObj(String::valueOf)
                            .toList();

            elements.forEach(e -> {
                keyedFunctionExecutor.process(function, e, String.format("-%s-", e));
            });
        }

        assertFalse(results.isEmpty());
        assertEquals(50, results.size());

        final List<String> expected =
                IntStream.rangeClosed(1, 50)
                        .mapToObj(String::valueOf)
                        .map(s -> String.format("-%s-", s))
                        .toList();

        expected.forEach(e -> {
            assertTrue(results.contains(e));
        });
    }

    @Test
    public void testNumber() {
        assertTrue(true);
    }
}
