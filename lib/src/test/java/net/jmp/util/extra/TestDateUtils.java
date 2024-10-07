package net.jmp.util.extra;

/*
 * (#)TestDateUtils.java    1.3.0   10/07/2024
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

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.*;

/// The test class for DateUtils.
///
/// @version    1.3.0
/// @since      1.3.0
public final class TestDateUtils {
    @Test
    public void testDateToStringDefaultFormat() {
        final Date now = new Date();
        final String formatted = DateUtils.dateToString(now);
        final Date result = DateUtils.dateFromString(formatted);

        assertEquals(now, result);
    }

    @Test
    public void testDateToStringWithFormat() {
        final Date now = new Date();
        final String formatted = DateUtils.dateToString(now, "yyyy-MM-dd HH:mm:ss.SSS");
        final Date result = DateUtils.dateFromString(formatted);

        assertEquals(now, result);
    }

    @Test
    public void testDateFromStringDefaultFormat() {
        final String formatted = "2024-10-07 01:23:45.678";
        final Date date = DateUtils.dateFromString(formatted);
        final String result = DateUtils.dateToString(date);

        assertEquals(formatted, result);
    }

    @Test
    public void testDateFromStringWithFormat() {
        final String formatted = "2024-10-07 01:23:45.678";
        final Date date = DateUtils.dateFromString(formatted);
        final String result = DateUtils.dateToString(date, "yyyy-MM-dd HH:mm:ss.SSS");

        assertEquals(formatted, result);
    }
}
