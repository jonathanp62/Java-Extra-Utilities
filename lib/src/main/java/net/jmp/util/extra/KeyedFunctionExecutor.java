package net.jmp.util.extra;

/*
 * (#)KeyedFunctionExecutor.java    1.1.0   09/26/2024
 *
 * @author   Jonathan Parker
 * @version  1.1.0
 * @since    1.1.0
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

import com.google.common.util.concurrent.Striped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import java.util.concurrent.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import java.util.function.Function;

/// The keyed function executor.
///
/// @param  <T> The type of value
public final class KeyedFunctionExecutor<T> implements AutoCloseable {
    /// The default number of threads.
    private static final int DEFAULT_NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();

    /// The map of keyed entries.
    private final Map<String, T> map = new ConcurrentHashMap<>();

    /// The executor service.
    private final ExecutorService executor;

    /// A list of runnable futures.
    private final List<Future<Void>> futures = new ArrayList<>();

    /// Control access to the map.
    private final Striped<ReadWriteLock> locks = Striped.readWriteLock(64);

    /// The default constructor.
    public KeyedFunctionExecutor() {
        super();

        this.executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);
    }

    /// A constructor that takes
    /// the number of threads to use.
    ///
    /// @param  numberOfThreads int
    public KeyedFunctionExecutor(final int numberOfThreads) {
        super();

        if (numberOfThreads <= 0) {
            throw new IllegalArgumentException("Number of threads must be greater than 0");
        }

        this.executor = Executors.newFixedThreadPool(numberOfThreads);
    }

    /// Close any resources.
    @Override
    public void close() {
        this.waitForFutures();
        this.executor.shutdown();
    }

    /// Wait for any futures to complete.
    private void waitForFutures() {
        this.futures.forEach(future -> {
            if (!future.isDone()) {
                try {
                    future.get();
                } catch (final InterruptedException | ExecutionException e) {
                    System.err.println("A thread incurred an exception or was interrupted");
                    e.printStackTrace(System.err);

                    if (e instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        this.futures.clear();
    }

    /// Process the keyed function.
    ///
    /// @param  function    java.util.function.Function<? super T, java.lang.Void>
    /// @param  key         java.lang.String
    /// @param  value       T
    public void process(final Function<? super T, Void> function, final String key, final T value) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        this.map.put(key, value);

        final Lock lock = this.locks.get(key).writeLock();

        if (lock.tryLock()) {
            try {
                while (map.containsKey(key)) {
                    final T val = map.get(key);

                    this.map.remove(key);

                    final Future<Void> future = this.executor.submit(() -> function.apply(val));

                    this.futures.add(future);
                }
            } finally {
                lock.unlock();
            }
        } else {
            System.err.format("Failed to acquire a lock: %s%n", key);
        }
    }
}
