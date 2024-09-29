package net.jmp.util.extra;

/*
 * (#)AppliedQueue.java 1.2.0   09/28/2024
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

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.jmp.util.logging.LoggerUtils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/// An applied queue.
///
/// @param  <T> The type of element
/// @version    1.2.0
/// @since      1.2.0
public class AppliedQueue<T> extends AppliedBaseCollection<T> implements Queue<T>, AutoCloseable {
    /// The logger.
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /// The queue.
    private final Queue<T> queue;

    /// The default constructor.
    public AppliedQueue() {
        super();

        this.queue = new ConcurrentLinkedQueue<>();
    }

    /// A constructor that takes
    /// the number of threads to use.
    ///
    /// @param  numThreads  int
    public AppliedQueue(final int numThreads) {
        super(numThreads);

        this.queue = new ConcurrentLinkedQueue<>();
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

    /// Inserts the element into the queue if the
    /// applied predicate function evaluates to true.
    ///
    /// @param  t           T
    /// @param  matcher     java.util.function.Predicate<? super T>
    /// @param  function    java.util.function.Function<? super T, java.lang.Boolean>
    /// @return             boolean
    private boolean addOrOfferIf(final T t,
                                 final Predicate<? super T> matcher,
                                 final Function<? super T, Boolean> function) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, matcher, function));
        }

        boolean result = true;  // Return true if the element did not match

        if (matcher.test(t)) {
            result = function.apply(t);
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Inserts the element into the queue after applying
    /// the mapper function if the applied predicate
    /// function evaluates to true.
    ///
    /// @param  t           T
    /// @param  mapper      java.util.function.Function<? super T,? extends T>
    /// @param  matcher     java.util.function.Predicate<? super T>
    /// @param  function    java.util.function.Function<? super T, java.lang.Boolean>
    /// @return             boolean
    private boolean applyAndAddOrOfferIf(final T t,
                                         final Function<? super T, ? extends T> mapper,
                                         final Predicate<? super T> matcher,
                                         final Function<? super T, Boolean> function) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, mapper, matcher, function));
        }

        boolean result = true;  // Return true if the element did not match

        if (matcher.test(t)) {
            final T mappedValue = mapper.apply(t);

            result = function.apply(mappedValue);
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Peeks, polls, or removes the head of this queue, or returns null if
    /// this queue is empty.
    /// Apply the consumer to the retrieved element if it is not null.
    ///
    /// @param  consumer    java.util.function.Consumer<? super T>
    /// @param  supplier    java.util.function.Supplier<? extends T>
    /// @return             T
    private T peekOrPollOrRemoveAndApply(final Consumer<? super T> consumer,
                                         final Supplier<? extends T> supplier) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(consumer, supplier));
        }

        final T element = supplier.get();

        if (element != null) {
            super.runTask(() -> consumer.accept(element));
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(element));
        }

        return element;
    }

    /// Inserts the element into the queue after applying the mapper function.
    ///
    /// @param  t           T
    /// @param  mapper      java.util.function.Function<? super T, ? extends T>
    /// @param  function    java.util.function.Function<? super T, java.lang.Boolean>
    /// @return              boolean
    private boolean applyAndAddOrOffer(final T t,
                                       final Function<? super T, ? extends T> mapper,
                                       final Function<? super T, Boolean> function) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, mapper, function));
        }

        final T mappedValue = mapper.apply(t);
        final boolean result = function.apply(mappedValue);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Inserts the element into the queue if the
    /// applied predicate function evaluates to true.
    ///
    /// @param  t       T
    /// @param  matcher java.util.function.Predicate<? super T>
    /// @return         boolean
    public boolean addIf(final T t, final Predicate<? super T> matcher) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, matcher));
        }

        final boolean result = this.addOrOfferIf(t, matcher, this.queue::add);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Inserts the element into the queue after applying
    /// the mapper function if the applied predicate
    /// function evaluates to true.
    ///
    /// @param  t       T
    /// @param  mapper  java.util.function.Function<? super T,? extends T>
    /// @param  matcher java.util.function.Predicate<? super T>
    /// @return         boolean
    public boolean applyAndAddIf(final T t,
                                 final Function<? super T, ? extends T> mapper,
                                 final Predicate<? super T> matcher) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, mapper, matcher));
        }

        final boolean result = this.applyAndAddOrOfferIf(t, mapper, matcher, this.queue::add);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Inserts the element into the queue if the
    /// applied predicate function evaluates to true.
    ///
    /// @param  t       T
    /// @param  matcher java.util.function.Predicate<? super T>
    /// @return         boolean
    public boolean offerIf(final T t, final Predicate<? super T> matcher) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, matcher));
        }

        final boolean result = this.addOrOfferIf(t, matcher, this.queue::offer);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Inserts the element into the queue after applying
    /// the mapper function if the applied predicate
    /// function evaluates to true.
    ///
    /// @param  t       T
    /// @param  mapper  java.util.function.Function<? super T,? extends T>
    /// @param  matcher java.util.function.Predicate<? super T>
    /// @return         boolean
    public boolean applyAndOfferIf(final T t,
                                   final Function<? super T, ? extends T> mapper,
                                   final Predicate<? super T> matcher) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, mapper, matcher));
        }

        final boolean result = this.applyAndAddOrOfferIf(t, mapper, matcher, this.queue::offer);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Inserts the element into the queue after applying the mapper function.
    ///
    /// @param  t       T
    /// @param  mapper  java.util.function.Function<? super T,? extends T>
    /// @return         boolean
    public boolean applyAndOffer(final T t, final Function<? super T, ? extends T> mapper) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, mapper));
        }

        final boolean result = this.applyAndAddOrOffer(t, mapper, this.queue::offer);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Inserts the element into the queue after applying the mapper function.
    ///
    /// @param  t       T
    /// @param  mapper  java.util.function.Function<? super T, ? extends T>
    /// @return         boolean
    public boolean applyAndAdd(final T t, final Function<? super T, ? extends T> mapper) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, mapper));
        }

        final boolean result = this.applyAndAddOrOffer(t, mapper, this.queue::add);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Adds all the elements in the specified collection to this queue.
    /// Apply the mapper function to each element before adding it.
    ///
    /// @param  c       java.util.Collection<? extends T>
    /// @param  mapper  java.util.function.Function<? super T, ? extends T>
    /// @return         boolean
    public boolean applyAndAddAll(final Collection<? extends T> c,
                                  final Function<? super T, ? extends T> mapper) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(c, mapper));
        }

        final boolean result = super.applyAndAddAll(this.queue, c, mapper);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Apply the consumer to each element
    /// and then clear the queue.
    ///
    /// @param  onElement   java.util.function.Consumer<? super T>
    /// @param  onEnd       java.lang.Runnable
    public void clearAndApply(final Consumer<? super T> onElement, final Runnable onEnd) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(onElement, onEnd));
        }

        super.clearAndApply(this.queue, onElement, onEnd);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }

    /// Retrieves, but does not remove, the head of this queue. This method differs
    /// from peekAndApply only in that it throws an exception if this queue is empty.
    /// Apply the consumer to the retrieved element.
    ///
    /// @param  consumer    java.util.function.Consumer<? super T>
    /// @return             T
    public T elementAndApply(final Consumer<? super T> consumer) throws NoSuchElementException {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(consumer));
        }

        if (this.queue.isEmpty()) {
            throw new NoSuchElementException();
        }

        final T element = this.peekOrPollOrRemoveAndApply(consumer, this.queue::element);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(element));
        }

        return element;
    }

    /// Retrieves, but does not remove, the head of this queue, or returns null if
    /// this queue is empty.
    /// Apply the consumer to the retrieved element if it is not null.
    ///
    /// @param  consumer    java.util.function.Consumer<? super T>
    /// @return             T
    public T peekAndApply(final Consumer<? super T> consumer) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(consumer));
        }

        final T element = this.peekOrPollOrRemoveAndApply(consumer, this.queue::peek);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(element));
        }

        return element;
    }

    /// Retrieves and removes the head of this queue, or returns null if this queue is empty.
    /// Apply the consumer to the retrieved element if it is not null.
    ///
    /// @param  consumer    java.util.function.Consumer<? super T>
    /// @return             T
    public T pollAndApply(final Consumer<? super T> consumer) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(consumer));
        }

        final T element = this.peekOrPollOrRemoveAndApply(consumer, this.queue::poll);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(element));
        }

        return element;
    }

    /// Retrieves and removes the head of this queue, or throw an exception if this queue is empty.
    /// Apply the consumer to the retrieved element if it is not null.
    ///
    /// @param  consumer    java.util.function.Consumer<? super T>
    /// @return             T
    public T removeAndApply(final Consumer<? super T> consumer) throws NoSuchElementException {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(consumer));
        }

        if (this.queue.isEmpty()) {
            throw new NoSuchElementException();
        }

        final T element = this.peekOrPollOrRemoveAndApply(consumer, this.queue::remove);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(element));
        }

        return element;
    }

    /// Removes all of this collection's elements that are also contained in the specified
    /// collection (optional operation). After this call returns, this collection will contain
    /// no elements in common with the specified collection.
    /// Apply the onElement consumer to each removed element.
    ///
    /// @param  c           java.util.Collection<? extends T>
    /// @param  onElement   java.util.function.Consumer<? super T>
    /// @param  onEnd       java.lang.Runnable
    /// @return             boolean
    public boolean removeAllAndApply(final Collection<? extends T> c,
                                     final Consumer<? super T> onElement,
                                     final Runnable onEnd) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(c, onElement, onEnd));
        }

        final boolean result = super.removeAllAndApply(this.queue, c, onElement, onEnd);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Removes the element into the queue if the
    /// applied predicate function evaluates to true.
    ///
    /// @param  t       T
    /// @param  filter  java.util.function.Predicate<? super T>
    /// @return         boolean
    public boolean removeIf(final T t, final Predicate<? super T> filter) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(t, filter));
        }

        final boolean result = super.removeIf(t, this.queue, filter);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /// Removes all the elements of this collection that satisfy the given predicate.
    ///
    /// @param  matcher     java.util.function.Predicate<? super T>
    /// @param  consumer    java.util.function.Consumer<? super T>
    /// @return             boolean
    public boolean removeIfAndApply(final Predicate<? super T> matcher,
                                    final Consumer<? super T> consumer) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(matcher, consumer));
        }

        final WrappedObject<Boolean> result = WrappedObject.of(false);

        if (!this.queue.isEmpty()) {
            this.queue.forEach(e -> {
                if (this.queue.removeIf(matcher)) {
                    super.runTask(() -> consumer.accept(e));
                    result.set(true);
                }
            });
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result.get()));
        }

        return result.get();
    }

    /// Retains only the elements in this queue that are contained
    /// in the specified collection (optional operation). In other
    /// words, removes from this queue all of its elements that are
    /// not contained in the specified collection. After this call
    /// returns, this collection will contain only elements in common
    /// with the specified collection.
    /// Apply the onElement consumer to each retained element.
    ///
    /// @param  c           java.util.Collection<? extends T>
    /// @param  onElement   java.util.function.Consumer<? super T>
    /// @param  onEnd       java.lang.Runnable
    /// @return             boolean
    public boolean retainAllAndApply(final Collection<? extends T> c,
                                     final Consumer<? super T> onElement,
                                     final Runnable onEnd) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entryWith(c, onElement, onEnd));
        }

        final boolean result = super.retainAllAndApply(this.queue, c, onElement, onEnd);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exitWith(result));
        }

        return result;
    }

    /* Queue method overrides */

    /// Returns the number of elements in this queue.
    ///
    /// @return int
    @Override
    public int size() {
        return this.queue.size();
    }

    /// Returns true if this queue contains no elements.
    ///
    /// @return boolean
    @Override
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    /// Returns true if this queue contains the specified element.
    ///
    /// @param  o   java.lang.Object
    /// @return     boolean
    @Override
    public boolean contains(Object o) {
        return this.queue.contains(o);
    }

    /// Returns an iterator over the elements in this queue in proper sequence.
    ///
    /// @return java.util.Iterator<T>
    @Override
    public Iterator<T> iterator() {
        return this.queue.iterator();
    }

    /// Returns an array containing all the elements in this queue
    /// in proper sequence (from first to last element).
    ///
    /// @return java.lang.Object[]
    @Override
    public Object[] toArray() {
        return this.queue.toArray();
    }

    /// Returns an array containing all the elements in this queue in
    /// proper sequence (from first to last element); the runtime type
    /// of the returned array is that of the specified array.
    ///
    /// @param  <U> The component type of the array to contain the collection
    /// @param  a   U[]
    /// @return     U[]
    @Override
    public <U> U[] toArray(U[] a) {
        return this.queue.toArray(a);
    }

    /// Inserts the specified element into this queue if it is possible to do so
    /// immediately without violating capacity restrictions, returning true upon
    /// success and throwing an IllegalStateException if no space is currently
    /// available.
    ///
    /// @param  t   T
    /// @return     boolean
    @Override
    public boolean add(T t) {
        return this.queue.add(t);
    }

    /// Removes the first occurrence of the specified element from this queue,
    /// if it is present (optional operation).
    ///
    /// @param  o   java.lang.Object
    /// @return     boolean
    @Override
    public boolean remove(Object o) {
        return this.queue.remove(o);
    }

    /// Returns true if this queue contains all of the elements of the specified collection.
    ///
    /// @param  c   java.util.Collection<?>
    /// @return     boolean
    @Override
    public boolean containsAll(Collection<?> c) {
        return this.queue.containsAll(c);
    }

    /// Appends all the elements in the specified collection to the end of this queue,
    /// in the order that they are returned by the specified collection's iterator
    /// (optional operation).
    ///
    /// @param  c   java.util.Collection<? extends T>
    /// @return     boolean
    @Override
    public boolean addAll(Collection<? extends T> c) {
        return this.queue.addAll(c);
    }

    /// Removes from this queue all of its elements that are contained in
    /// the specified collection (optional operation).
    ///
    /// @param  c   java.util.Collection<?>
    /// @return     boolean
    @Override
    public boolean removeAll(Collection<?> c) {
        return this.queue.removeAll(c);
    }

    /// Retains only the elements in this queue that are contained in the
    /// specified collection (optional operation).
    ///
    /// @param  c   java.util.Collection<?>
    /// @return     boolean
    @Override
    public boolean retainAll(Collection<?> c) {
        return this.queue.retainAll(c);
    }

    /// Removes all the elements from this queue (optional operation).
    @Override
    public void clear() {
        this.queue.clear();
    }

    /// Inserts the specified element into this queue if it is possible
    /// to do so immediately without violating capacity restrictions.
    ///
    /// @param  t   T
    /// @return     boolean
    @Override
    public boolean offer(T t) {
        return this.queue.offer(t);
    }

    /// Retrieves and removes the head of this queue.
    ///
    /// @return T
    @Override
    public T remove() {
        return this.queue.remove();
    }

    /// Retrieves and removes the head of this queue, or returns
    /// null if this queue is empty.
    ///
    /// @return T
    @Override
    public T poll() {
        return this.queue.poll();
    }

    /// Retrieves, but does not remove, the head of this queue.
    ///
    /// @return T
    @Override
    public T element() {
        return this.queue.element();
    }

    /// Retrieves, but does not remove, the head of this queue,
    /// or returns null if this queue is empty.
    ///
    /// @return T
    @Override
    public T peek() {
        return this.queue.peek();
    }
}
