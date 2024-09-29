package net.jmp.util.extra;

/*
 * (#)TestAppliedSet.java   1.2.0   09/29/2024
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
import java.util.List;
import java.util.Objects;

import java.util.concurrent.TimeUnit;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

import static org.awaitility.Awaitility.await;

import static org.junit.Assert.*;

import org.junit.Test;

/// A test class for AppliedSet.
///
/// @version    1.2.0
/// @since      1.2.0
public final class TestAppliedSet {
    public static final int AWAIT_TIME = 500;

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithZeroThreads() {
        try (final var _ = new AppliedSet<Integer>(0)) {
            assertTrue(true);
        }
    }

    @Test
    public void testOfEmpty() {
        try (final AppliedSet<Integer> set = AppliedSet.of()) {
            assertTrue(set.isEmpty());
            assertThrows(UnsupportedOperationException.class, () -> set.add(0));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testOfNull() {
        final AppliedSet<Integer> _ = AppliedSet.of(null);
    }

    @Test
    public void testOfOne() {
        try (final AppliedSet<Integer> set = AppliedSet.of(1)) {
            assertFalse(set.isEmpty());
            assertEquals(1, set.size());
            assertTrue(set.contains(1));
            assertThrows(UnsupportedOperationException.class, () -> set.add(0));
        }
    }

    @Test
    public void testOfTwo() {
        try (final AppliedSet<Integer> set = AppliedSet.of(1, 2)) {
            assertFalse(set.isEmpty());
            assertEquals(2, set.size());
            assertTrue(set.contains(1));
            assertTrue(set.contains(2));
            assertThrows(UnsupportedOperationException.class, () -> set.add(0));
        }
    }

    @Test
    public void testOfThree() {
        try (final AppliedSet<Integer> set = AppliedSet.of(1, 2, 3)) {
            assertFalse(set.isEmpty());
            assertEquals(3, set.size());
            assertTrue(set.contains(1));
            assertTrue(set.contains(2));
            assertTrue(set.contains(3));
            assertThrows(UnsupportedOperationException.class, () -> set.add(0));
        }
    }

    @Test
    public void testAddIf() {
        try (final AppliedSet<Integer> set = new AppliedSet<>()) {
            final Predicate<Integer> isEven = i -> i % 2 == 0;

            IntStream.rangeClosed(1, 6)
                    .forEach(i -> {
                        if (!set.addIf(i, isEven))
                            fail(String.format("Failed to add element %d", i));
                    });

            assertFalse(set.addIf(4, isEven));
            assertTrue(set.addIf(8, isEven));

            assertFalse(set.isEmpty());
            assertEquals(4, set.size());
            assertTrue(set.contains(2));
            assertTrue(set.contains(4));
            assertTrue(set.contains(6));
            assertTrue(set.contains(8));
        }
    }

    @Test
    public void testApplyAndAddIf() {
        try (final AppliedSet<Integer> set = new AppliedSet<>()) {
            final Function<Integer, Integer> timesTwo = x -> x * 2;
            final Predicate<Integer> isOdd = i -> i % 2 != 0;

            IntStream.rangeClosed(1, 6)
                    .forEach(i -> {
                        if (!set.applyAndAddIf(i, timesTwo, isOdd))
                            fail(String.format("Failed to add element %d", i));
                    });

            assertFalse(set.applyAndAddIf(3, timesTwo, isOdd));
            assertTrue(set.applyAndAddIf(7, timesTwo, isOdd));

            assertFalse(set.isEmpty());
            assertEquals(4, set.size());
            assertTrue(set.contains(2));
            assertTrue(set.contains(6));
            assertTrue(set.contains(10));
            assertTrue(set.contains(14));
        }
    }

    @Test
    public void testApplyAndAdd() {
        try (final AppliedSet<Integer> set = new AppliedSet<>()) {
            final Function<Integer, Integer> function = x -> x + 1;

            assertTrue(set.applyAndAdd(1, function));
            assertTrue(set.applyAndAdd(2, function));
            assertTrue(set.applyAndAdd(3, function));

            assertFalse(set.applyAndAdd(3, function));
            assertTrue(set.applyAndAdd(4, function));

            assertEquals(4, set.size());

            assertTrue(set.contains(2));
            assertTrue(set.contains(3));
            assertTrue(set.contains(4));
            assertTrue(set.contains(5));
        }
    }

    @Test
    public void testApplyAndAddAll() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final List<String> values = List.of("value 1", "value 2", "value 3");

            final boolean result = set.applyAndAddAll(values, String::toUpperCase);

            assertTrue(result);
            assertEquals(3, set.size());
            assertTrue(set.contains("VALUE 1"));
            assertTrue(set.contains("VALUE 2"));
            assertTrue(set.contains("VALUE 3"));

            assertFalse(set.applyAndAddAll(values, String::toUpperCase));
        }
    }

    @Test
    public void testApplyAndAddAllOnEmptyCollection() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final boolean result = set.applyAndAddAll(new ArrayList<>(), x -> x);

            assertFalse(result);
            assertEquals(0, set.size());
        }
    }

    @Test
    public void testClearAndApply() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final WrappedObject<Boolean> consumed = WrappedObject.of(false);
            final List<String> values = List.of("value 1", "value 2", "value 3");

            set.addAll(values);

            assertEquals(3, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains("value 2"));
            assertTrue(set.contains("value 3"));

            set.clearAndApply(e -> System.out.format("Cleared: %s%n", e), () -> consumed.set(true));

            set.waitForConsumers();

            assertTrue(consumed.get());
            assertTrue(set.isEmpty());
        }
    }

    @Test
    public void testConsume() {
        final List<String> results = new ArrayList<>();

        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final List<String> values = List.of("value 1", "value 2", "value 3");

            set.addAll(values);

            assertEquals(3, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains("value 2"));
            assertTrue(set.contains("value 3"));

            set.consume(results::add, () -> {});
        }

        assertFalse(results.isEmpty());
        assertEquals(3, results.size());
        assertTrue(results.contains("value 1"));
        assertTrue(results.contains("value 2"));
        assertTrue(results.contains("value 3"));
    }

    @Test
    public void testRemoveAndApplyByObjectFound() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final WrappedObject<Boolean> consumed = WrappedObject.of(false);
            final WrappedObject<String> removedElement = new WrappedObject<>();

            final List<String> values = List.of("value 1", "value 2", "value 3");

            set.addAll(values);

            final Consumer<String> consumer = e -> {
                removedElement.set(e.toUpperCase());
                consumed.set(true);
            };

            final boolean result = set.removeAndApply("value 2", consumer);

            assertTrue(result);
            assertEquals(2, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains("value 3"));

            await().atMost(AWAIT_TIME, TimeUnit.MILLISECONDS)
                    .untilAsserted(
                            () -> assertThat(consumed.get())
                                    .isTrue()
                    );

            assertEquals("VALUE 2", removedElement.get());
        }
    }

    @Test
    public void testRemoveAndApplyByObjectNotFound() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final List<String> values = List.of("value 1", "value 2", "value 3");

            set.addAll(values);

            final boolean result = set.removeAndApply("value 4", System.out::println);

            assertFalse(result);
            assertEquals(3, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains("value 2"));
            assertTrue(set.contains("value 3"));
        }
    }

    @Test
    public void testRemoveAndApplyByNullObject() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            set.add("value 1");
            set.add(null);
            set.add("value 3");

            final boolean result = set.removeAndApply(null, System.out::println);

            assertTrue(result);
            assertEquals(2, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains("value 3"));
        }
    }

    @Test
    public void testRemoveAllAndApply() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final WrappedObject<Boolean> consumed = new WrappedObject<>(false);
            final List<String> values = List.of("value 1", "value 2", "value 3");

            set.addAll(values);

            final boolean result = set.removeAllAndApply(
                    values,
                    e -> System.out.format("testRemoveAllAndApply: %s%n", e),
                    () -> consumed.set(true)
            );

            await().atMost(AWAIT_TIME, TimeUnit.MILLISECONDS)
                    .untilAsserted(
                            () -> assertThat(consumed.get())
                                    .isTrue()
                    );

            assertTrue(result);
            assertEquals(0, set.size());
        }
    }

    @Test
    public void testRemoveAllAndApplyOnEmptyCollection() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final List<String> values = List.of("value 1", "value 2", "value 3");

            values.forEach(set::add);

            final boolean result = set.removeAllAndApply(new ArrayList<>(), e -> {
                System.out.format("testRemoveAllAndApply: %s%n", e);
            }, () -> {});

            assertFalse(result);
            assertEquals(3, set.size());
        }
    }

    @Test
    public void testRemoveAllAndApplyOnNonMatchingCollection() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final List<String> values = List.of("value 1", "value 2", "value 3");
            final List<String> removals = List.of("value 4", "value 5");

            values.forEach(set::add);

            final boolean result = set.removeAllAndApply(
                    removals,
                    e -> System.out.format("testRemoveAllAndApply: %s%n", e),
                    () -> {}
            );

            assertFalse(result);
            assertEquals(3, set.size());
        }
    }

    @Test
    public void testRemoveIf() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final List<String> values = List.of("value 1", "value 2", "value 3");

            set.addAll(values);

            final boolean result = set.removeIf("value 2", s -> s.startsWith("value"));

            assertTrue(result);
            assertEquals(2, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains("value 3"));
        }
    }

    @Test
    public void testRemoveIfFalsePredicate() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final List<String> values = List.of("value 1", "value 2", "value 3");

            set.addAll(values);

            final boolean result = set.removeIf("value 2", s -> s.isEmpty());

            assertFalse(result);
            assertEquals(3, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains("value 2"));
            assertTrue(set.contains("value 3"));
        }
    }

    @Test
    public void testRemoveIfNotInSet() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final List<String> values = List.of("value 1", "value 2", "value 3");

            set.addAll(values);

            final boolean result = set.removeIf("value 4", s -> s.startsWith("value"));

            assertFalse(result);
            assertEquals(3, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains("value 2"));
            assertTrue(set.contains("value 3"));
        }
    }

    @Test
    public void testRemoveIfAndApplyByObjectFound() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final WrappedObject<Boolean> consumed = WrappedObject.of(false);
            final WrappedObject<String> removedElement = new WrappedObject<>();

            final List<String> values = List.of("value 1", "value 2", "value 3");

            set.addAll(values);

            final Consumer<String> consumer = e -> {
                removedElement.set(e.toUpperCase());
                consumed.set(true);
            };

            final boolean result = set.removeIfAndApply("value 2", s -> s.startsWith("value"), consumer);

            assertTrue(result);
            assertEquals(2, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains("value 3"));

            await().atMost(AWAIT_TIME, TimeUnit.MILLISECONDS)
                    .untilAsserted(
                            () -> assertThat(consumed.get())
                                    .isTrue()
                    );

            assertEquals("VALUE 2", removedElement.get());
        }
    }

    @Test
    public void testRemoveIfAndApplyByObjectFoundNoMatch() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final List<String> values = List.of("value 1", "value 2", "value 3");

            set.addAll(values);

            final boolean result = set.removeIfAndApply("value 2", s -> s.isEmpty(), System.out::println);

            assertFalse(result);
            assertEquals(3, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains("value 2"));
            assertTrue(set.contains("value 3"));
        }
    }

    @Test
    public void testRemoveIfAndApplyByObjectNotFound() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final List<String> values = List.of("value 1", "value 2", "value 3");

            set.addAll(values);

            final boolean result = set.removeIfAndApply("value 4", s -> s.startsWith("value"), System.out::println);

            assertFalse(result);
            assertEquals(3, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains("value 2"));
            assertTrue(set.contains("value 3"));
        }
    }

    @Test
    public void testRemoveIfAndApplyByNullObject() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            set.add("value 1");
            set.add(null);
            set.add("value 3");

            final boolean result = set.removeIfAndApply(null, Objects::isNull, System.out::println);

            assertTrue(result);
            assertEquals(2, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains("value 3"));
        }
    }

    @Test
    public void testRemoveIfAndApplyByNullObjectNoMatch() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            set.add("value 1");
            set.add(null);
            set.add("value 3");

            final boolean result = set.removeIfAndApply(null, Objects::nonNull, System.out::println);

            assertFalse(result);
            assertEquals(3, set.size());
            assertTrue(set.contains("value 1"));
            assertTrue(set.contains(null));
            assertTrue(set.contains("value 3"));
        }
    }

    @Test
    public void testRetainAllAndApplyOnFullMatchingCollection() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final WrappedObject<Boolean> consumed = WrappedObject.of(false);
            final List<String> values = List.of("value 1", "value 2", "value 3");
            final List<String> results = new ArrayList<>();

            set.addAll(values);

            final boolean result = set.retainAllAndApply(values, results::add, () -> consumed.set(true));

            await().atMost(AWAIT_TIME, TimeUnit.MILLISECONDS)
                    .untilAsserted(
                            () -> assertThat(results.size())
                                    .isEqualTo(3)
                    );

            assertFalse(result);
            assertTrue(consumed.get());
            assertEquals(3, results.size());
            assertTrue(results.contains("value 1"));
            assertTrue(results.contains("value 2"));
            assertTrue(results.contains("value 3"));
        }
    }

    @Test
    public void testRetainAllAndApplyOnPartialMatchingCollection() {
        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final WrappedObject<Boolean> consumed = WrappedObject.of(false);
            final List<String> values = List.of("value 1", "value 2", "value 3");
            final List<String> retains = List.of("value 1", "value 3");
            final List<String> results = new ArrayList<>();

            set.addAll(values);

            final boolean result = set.retainAllAndApply(retains, results::add, () -> consumed.set(true));

            assertTrue(result);

            set.waitForConsumers();

            assertTrue(consumed.get());
            assertEquals(2, results.size());
            assertTrue(results.contains("value 1"));
            assertTrue(results.contains("value 3"));
        }
    }

    @Test
    public void testRetainAllAndApplyOnEmptyCollection() {
        final List<String> results = new ArrayList<>();

        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final List<String> values = List.of("value 1", "value 2", "value 3");

            set.addAll(values);

            final boolean result = set.retainAllAndApply(new ArrayList<>(), results::add, () -> {});

            assertTrue(result);
            assertEquals(0, set.size());
        }

        assertEquals(0, results.size());
    }

    @Test
    public void testRetainAllAndApplyOnNonMatchingCollection() {
        final List<String> results = new ArrayList<>();

        try (final AppliedSet<String> set = new AppliedSet<>()) {
            final List<String> values = List.of("value 1", "value 2", "value 3");
            final List<String> retains = List.of("value 4", "value 5");

            set.addAll(values);

            final boolean result = set.retainAllAndApply(retains, results::add, () -> {});

            assertTrue(result);
        }

        assertEquals(0, results.size());
    }
}
