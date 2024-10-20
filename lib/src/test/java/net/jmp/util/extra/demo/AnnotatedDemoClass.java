package net.jmp.util.extra.demo;

/*
 * (#)AnnotatedDemoClass.java   1.4.0   10/18/2024
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

/// A demo class with both annotations..
///
/// @version    1.4.0
/// @since      1.4.0
@DemoClass
@DemoVersion(value = 1.4)
public final class AnnotatedDemoClass implements Demo {
    /// The default constructor.
    public AnnotatedDemoClass() {
        super();
    }

    /// The demo method.
    @Override
    public void demo() {
        System.out.println("Method 'demo' called");
    }

    /// A method that returns nothing.
    public void someVoidMethod() {
        System.out.println("Method 'someVoidMethod' called");
    }

    /// A method that returns a string.
    ///
    /// @return java.lang.String
    public String someStringMethod() {
        System.out.println("Method 'someStringMethod' called");

        return "Some string";
    }
}
