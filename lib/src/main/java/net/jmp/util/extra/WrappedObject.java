package net.jmp.util.extra;

/*
 * (#)WrappedObject.java    1.3.1   10/08/2024
 * (#)WrappedObject.java    1.0.0   09/25/2024
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

import java.util.Objects;

/// A wrapper for objects. Created initially to address the issue
/// of needing to change the state of a non-final variable from
/// a lambda.
///
/// @param  <T> The type of object to wrap
/// @version    1.3.1
/// @since      1.0.0
public final class WrappedObject<T> {
    /// Object is null message text.
    private static final String OBJECT_IS_NULL = "T 'object' is null";

    /// The object.
    private T object;

    /// * The default constructor.
    public WrappedObject() {
        super();
    }

    /// A constructor that takes the object.
    ///
    /// @param  object  T
    public WrappedObject(final T object) {
        super();

        this.object = Objects.requireNonNull(object, () -> OBJECT_IS_NULL);
    }

    /// Construct and return a wrapped object of type T.
    ///
    /// @param  <T>     The type of object
    /// @param  object  java.lang.Object
    /// @return         net.jmp.util.extra.WrappedObject<T>
    public static <T> WrappedObject<T> of(final T object) {
        return new WrappedObject<>(Objects.requireNonNull(object, () -> OBJECT_IS_NULL));
    }

    /// Set the object.
    ///
    /// @param  object  T
    public void set(final T object) {
        this.object = Objects.requireNonNull(object, () -> OBJECT_IS_NULL);
    }

    /// Return the object.
    ///
    /// @return T
    public T get() {
        return this.object;
    }

    /// The equals method.
    ///
    /// @param  o   java.lang.Object
    /// @return     boolean
    /// @since  1.3.1
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final WrappedObject<?> that = (WrappedObject<?>) o;

        return Objects.equals(this.object, that.object);
    }

    /// The hash-code method.
    ///
    /// @return int
    /// @since  1.3.1
    @Override
    public int hashCode() {
        return Objects.hashCode(object);
    }

    /// The to-string method.
    ///
    /// @return java.lang.String
    /// @since  1.3.1
    @Override
    public String toString() {
        return "WrappedObject{" +
                "object=" + this.object +
                '}';
    }
}
