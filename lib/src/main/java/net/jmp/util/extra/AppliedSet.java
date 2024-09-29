package net.jmp.util.extra;

/*
 * (#)AppliedSet.java   1.2.0   09/29/2024
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

import java.util.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static net.jmp.util.logging.LoggerUtils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/// An applied set.
///
/// @param  <T> The type of element
/// @version    1.2.0
/// @since      1.2.0
public class AppliedSet<T> extends AppliedBaseCollection<T> implements Set<T>, AutoCloseable {
    /// The logger.
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /// The set.
    private final Set<T> set;

    /// The default constructor.
    public AppliedSet() {
        super();

        this.set = new HashSet<>();
    }

    /// A constructor that takes
    /// the number of threads to use.
    ///
    /// @param  numThreads  int
    public AppliedSet(final int numThreads) {
        super(numThreads);

        this.set = new HashSet<>();
    }

    /// A constructor that takes a list
    /// and creates an unmodifiable object.
    ///
    /// @param  set java.util.Set<? extends T>
    private AppliedSet(final Set<? extends T> set) {
        super();

        this.set = Collections.unmodifiableSet(set);
    }

    /// Create an empty applied set.
    ///
    /// @param  <T> The type of element
    /// @return     net.jmp.demo.java22.util.AppliedSet<T>
    public static <T> AppliedSet<T> of() {
        return new AppliedSet<>(new HashSet<>());
    }

    /// Create an applied set with one element.
    ///
    /// @param  <T> The type of element
    /// @param  t   T
    /// @return     net.jmp.demo.java22.util.AppliedSet<T>
    public static <T> AppliedSet<T> of(final T t) {
        final AppliedSet<T> set = new AppliedSet<>();

        set.add(Objects.requireNonNull(t));

        return new AppliedSet<>(set);
    }

    /// Create an applied set with two elements.
    ///
    /// @param  <T> The type of element
    /// @param  t1  T
    /// @param  t2  T
    /// @return     net.jmp.demo.java22.util.AppliedSet<T>
    public static <T> AppliedSet<T> of(final T t1, final T t2) {
        final AppliedSet<T> set = new AppliedSet<>();

        set.add(Objects.requireNonNull(t1));
        set.add(Objects.requireNonNull(t2));

        return new AppliedSet<>(set);
    }

    /// Create an applied set with three elements.
    ///
    /// @param  <T> The type of element
    /// @param  t1  T
    /// @param  t2  T
    /// @param  t3  T
    /// @return     net.jmp.demo.java22.util.AppliedSet<T>
    public static <T> AppliedSet<T> of(final T t1, final T t2, final T t3) {
        final AppliedSet<T> set = new AppliedSet<>();

        set.add(Objects.requireNonNull(t1));
        set.add(Objects.requireNonNull(t2));
        set.add(Objects.requireNonNull(t3));

        return new AppliedSet<>(set);
    }

    /// Close any resources.
    @Override
    public void close() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        super.close();

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Inserts the element into the set if the
    /// applied predicate function evaluates to true
    /// and the element is not already present.
    ///
    /// @param  t       T
    /// @param  filter  java.util.function.Predicate<? super T>
    /// @return         boolean
    public boolean addIf(final T t, final Predicate<? super T> filter) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, filter));
        }

        final boolean result = super.addIf(t, this.set, filter);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Inserts the element into the set after applying
    /// the mapper function if the applied predicate
    /// function evaluates to true and the element is
    /// not already present.
    ///
    /// @param  t       T
    /// @param  mapper  java.util.function.Function<? super T,? extends T>
    /// @param  filter  java.util.function.Predicate<? super T>
    /// @return         boolean
    public boolean applyAndAddIf(final T t, final Function<? super T, ? extends T> mapper, final Predicate<? super T> filter) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, mapper, filter));
        }

        final boolean result = super.applyAndAddIf(t, this.set, mapper, filter);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Inserts the element into the set after applying the mapper
    /// function if the element is not already present.
    ///
    /// @param  t       T
    /// @param  mapper  java.util.function.Function<? super T, ? extends T>
    /// @return         boolean
    public boolean applyAndAdd(final T t, final Function<? super T, ? extends T> mapper) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, mapper));
        }

        final boolean result = super.applyAndAdd(t, this.set, mapper);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Adds all the elements in the specified collection to this set.
    /// Apply the mapper function to each element before adding it.
    ///
    /// @param  c       java.util.Collection<? extends T>
    /// @param  mapper  java.util.function.Function<? super T, ? extends T>
    /// @return         boolean
    public boolean applyAndAddAll(final Collection<? extends T> c, final Function<? super T, ? extends T> mapper) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(c, mapper));
        }

        final boolean result = super.applyAndAddAll(this.set, c, mapper);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Apply the onElement to each element
    /// and then clear the list.
    ///
    /// @param  onElement   java.util.function.Consumer<? super T>
    /// @param  onEnd       java.lang.Runnable
    public void clearAndApply(final Consumer<? super T> onElement, final Runnable onEnd) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(onElement, onEnd));
        }

        super.clearAndApply(this.set, onElement, onEnd);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Consume all the elements in the set.
    ///
    /// @param  onElement   java.util.function.Consumer<? super T>
    /// @param  onEnd       java.lang.Runnable
    public void consume(final Consumer<? super T> onElement, final Runnable onEnd) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(onElement, onEnd));
        }

        super.consume(this.set, onElement, onEnd);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Removes the occurrence of this element from the set if one exists.
    /// Apply the consumer to the removed element if it is not null.
    ///
    /// @param  object      T
    /// @param  consumer    java.util.function.Consumer<? super T>
    /// @return             boolean
    public boolean removeAndApply(final T object, final Consumer<? super T> consumer) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(object, consumer));
        }

        final boolean result = this.set.remove(object);

        if (object != null) {
            super.runTask(() -> consumer.accept(object));
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Removes all of this collection's elements that are also contained in the specified
    /// collection (optional operation). After this call returns, this collection will contain
    /// no elements in common with the specified collection.
    /// Apply the onElement consumer to each removed element.
    ///
    /// @param  c           java.util.Collection<? extends T>
    /// @param  onElement   java.util.function.Consumer<? super T>
    /// @return             boolean
    public boolean removeAllAndApply(final Collection<? extends T> c,
                                     final Consumer<? super T> onElement,
                                     final Runnable onEnd) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(c, onElement, onEnd));
        }

        final boolean result = super.removeAllAndApply(this.set, c, onElement, onEnd);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Removes the element into the set if the
    /// applied predicate function evaluates to true.
    ///
    /// @param  t       T
    /// @param  filter  java.util.function.Predicate<? super T>
    /// @return         boolean
    public boolean removeIf(final T t, final Predicate<? super T> filter) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, filter));
        }

        final boolean result = super.removeIf(t, this.set, filter);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Removes the occurrence of this element from the set if one exists
    /// and the applied predicate function evaluates to true.
    /// Apply the consumer to the removed element if it is not null.
    ///
    /// @param  object      T
    /// @param  matcher     java.util.function.Predicate<? super T>
    /// @param  consumer    java.util.function.Consumer<? super T>
    /// @return             boolean
    public boolean removeIfAndApply(final T object,
                                    final Predicate<? super T> matcher,
                                    final Consumer<? super T> consumer) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(object, matcher, consumer));
        }

        boolean result = false;

        if (matcher.test(object)) {
            result = this.set.remove(object);

            if (object != null) {
                super.runTask(() -> consumer.accept(object));
            }
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Retains only the elements in this set that are contained
    /// in the specified collection (optional operation). In other
    /// words, removes from this set all of its elements that are
    /// not contained in the specified collection. After this call
    /// returns, this collection will contain only elements in common
    /// with the specified collection.
    /// Apply the onElement consumer to each retained element.
    ///
    /// @param  c           java.util.Collection<? extends T>
    /// @param  onElement   java.util.function.Consumer<? super T>
    /// @return             boolean
    public boolean retainAllAndApply(final Collection<? extends T> c,
                                     final Consumer<? super T> onElement,
                                     final Runnable onEnd) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(c, onElement, onEnd));
        }

        final boolean result = super.retainAllAndApply(this.set, c, onElement, onEnd);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /* Set and Collection method overrides */

    @Override
    public int size() {
        return this.set.size();
    }

    @Override
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.set.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.set.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.set.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return this.set.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return this.set.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return this.set.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return this.set.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.set.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.set.removeAll(c);
    }

    @Override
    public void clear() {
        this.set.clear();
    }
}
