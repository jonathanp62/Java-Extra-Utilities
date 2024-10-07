package net.jmp.util.extra;

/*
 * (#)DateUtils.java    1.3.0   10/07/2024
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Objects;

/// A collection of static date utility methods.
///
/// @version    1.3.0
/// @since      1.3.0
public final class DateUtils {
    /// The date-time format string.
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /// The default constructor.
    private DateUtils() {
        super();
    }

    /// Return the specified date as a string
    /// formatted using the default format.
    ///
    /// @param  date    java.util.Date
    /// @return         java.lang.String
    public static String dateToString(final Date date) {
        Objects.requireNonNull(date);

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
        final Instant instant = date.toInstant();
        final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return localDateTime.format(formatter);
    }

    /// Return the specified date as a string
    /// formatted using the specified format.
    ///
    /// @param  date    java.util.Date
    /// @param  format  java.lang.String
    /// @return         java.lang.String
    public static String dateToString(final Date date, final String format) {
        Objects.requireNonNull(date);
        Objects.requireNonNull(format);

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        final Instant instant = date.toInstant();
        final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return localDateTime.format(formatter);
    }

    /// Return the date from a string formatted
    /// using the default format.
    ///
    /// @param  formattedDateString java.lang.String
    /// @return                     java.util.Date
    public static Date dateFromString(final String formattedDateString) {
        Objects.requireNonNull(formattedDateString);

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
        final LocalDateTime localDateTime = LocalDateTime.parse(formattedDateString, formatter);
        final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

        return Date.from(zonedDateTime.toInstant());
    }

    /// Return the date from a string formatted
    /// using the specified format.
    ///
    /// @param  formattedDateString java.lang.String
    /// @param  format              java.lang.String
    /// @return                     java.util.Date
    public static Date dateFromString(final String formattedDateString, final String format) {
        Objects.requireNonNull(formattedDateString);
        Objects.requireNonNull(format);

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        final LocalDateTime localDateTime = LocalDateTime.parse(formattedDateString, formatter);
        final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

        return Date.from(zonedDateTime.toInstant());
    }
}
