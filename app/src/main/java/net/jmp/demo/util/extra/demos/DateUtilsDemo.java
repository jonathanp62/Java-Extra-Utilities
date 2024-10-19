package net.jmp.demo.util.extra.demos;

/*
 * (#)DateUtilsDemo.java    1.4.0   10/19/2024
 * (#)DateUtilsDemo.java    1.3.0   10/08/2024
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

import net.jmp.util.extra.DateUtils;

import net.jmp.util.extra.demo.Demo;

import static net.jmp.util.logging.LoggerUtils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/// The class that demonstrates the date utilities.
///
/// @version    1.4.0
/// @since      1.3.0
public class DateUtilsDemo implements Demo {
    /// The logger.
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /// The default constructor.
    public DateUtilsDemo() {
        super();
    }

    /// The demo method.
    @Override
    public void demo() {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(entry());
        }

        if (this.logger.isInfoEnabled()) {
            this.logger.info(DateUtils.dateToString(new Date()));
            this.logger.info(DateUtils.dateFromString("1962-02-05 03:56:00", "yyyy-MM-dd HH:mm:ss").toString());
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace(exit());
        }
    }
}
