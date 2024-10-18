package net.jmp.util.extra.demo;

/*
 * (#)DemoUtilException.java    1.4.0   10/18/2024
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

import java.io.Serial;

/// An exception class for demo utility methods.
///
/// @version    1.4.0
/// @since      1.4.0
public final class DemoUtilException extends Exception {
    /// The serial version identifier.
    @Serial
    private static final long serialVersionUID = -7804280418655017139L;

    /// The default constructor.
    DemoUtilException() {
        super();
    }

    /// A constructor that takes a message.
    ///
    /// @param  message java.lang.String
    DemoUtilException(final String message) {
        super(message);
    }

    /// A constructor that takes a message and a cause.
    ///
    /// @param  message java.lang.String
    /// @param  cause   java.lang.Throwable
    DemoUtilException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /// A constructor that takes and a cause.
    ///
    /// @param  cause   java.lang.Throwable
    DemoUtilException(final Throwable cause) {
        super(cause);
    }
}
