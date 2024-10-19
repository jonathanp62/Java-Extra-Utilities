package net.jmp.util.extra.demo;

/*
 * (#)DemoUtils.java    1.4.0   10/18/2024
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static net.jmp.util.logging.LoggerUtils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/// A class of static utility methods specifically for demo programs.
///
/// @version    1.4.0
/// @since      1.4.0
public final class DemoUtils {
    /// The logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoUtils.class.getName());

    /// The default constructor.
    private DemoUtils() {
        super();
    }

    /// Get the version of the specified class
    /// if that class is annotated with Version.
    /// If no annotation is found for the class,
    /// return 0.
    ///
    /// @param  className   java.lang.String
    /// @return             double
    /// @throws             net.jmp.util.extra.demo.DemoUtilException   When an exception occurs loading the class
    public static double getDemoClassVersion(final String className) throws DemoUtilException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(entryWith(className));
        }

        double version = 0;

        try {
            final Class<?> clazz = Class.forName(className);

            if (isDemoClass(clazz)) {
                if (clazz.isAnnotationPresent(DemoClass.class)) {
                    if (clazz.isAnnotationPresent(DemoVersion.class)) {
                        final var versionAnnotation = clazz.getAnnotation(DemoVersion.class);

                        version = versionAnnotation.value();

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Class {} is annotated with @Version({})", clazz.getSimpleName(), version);
                        }
                    } else {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Class {} is not annotated with {}", clazz.getSimpleName(), DemoVersion.class.getName());
                        }
                    }
                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Class {} is not annotated with {}", clazz.getSimpleName(), DemoClass.class.getName());
                    }
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Class {} does not implement {}", clazz.getSimpleName(), Demo.class.getName());
                }
            }
        } catch (final ClassNotFoundException cnfe) {
            LOGGER.error(throwing(cnfe));
            throw new DemoUtilException(String.format("Exception loading class %s", className), cnfe);
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(exitWith(version));
        }

        return version;
    }

    /// Run the specified method in the specified demo class.
    ///
    /// @param  <T>         The type of return value
    /// @param  className   java.lang.String
    /// @param  methodName  java.lang.String
    /// @param  returnType  java.lang.Class<T>
    /// @return             T
    /// @throws             net.jmp.util.extra.demo.DemoUtilException   When an exception occurs running the method in the class
    @SuppressWarnings("unchecked")
    public static <T> T runDemoClassMethod(final String className, final String methodName, final Class<T> returnType) throws DemoUtilException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(entryWith(className, methodName, returnType));
        }

        T result = null;

        try {
            final Class<?> clazz = Class.forName(className);

            if (isDemoClass(clazz)) {
                if (clazz.isAnnotationPresent(DemoClass.class)) {
                    final Object instance = clazz.getDeclaredConstructor().newInstance();
                    final Method method = clazz.getDeclaredMethod(methodName);

                    if (returnType.equals(Void.class)) {
                        method.invoke(instance);
                    } else {
                        result = (T) method.invoke(instance);
                    }
                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Class {} is not annotated with {}", clazz.getSimpleName(), DemoClass.class.getName());
                    }
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Class {} does not implement {}", clazz.getSimpleName(), Demo.class.getName());
                }
            }
        } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException |
                       NoSuchMethodException | InvocationTargetException e) {
            LOGGER.error(throwing(e));
            throw new DemoUtilException(String.format("Exception running method %s:%s", className, methodName), e);
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(exitWith(result));
        }

        return result;
    }

    /// Run the demo method in the specified demo class.
    ///
    /// @param  <T>         The type of return value
    /// @param  className   java.lang.String
    /// @param  returnType  java.lang.Class<T>
    /// @return             T
    /// @throws             net.jmp.util.extra.demo.DemoUtilException   When an exception occurs running the method in the class
    public static <T> T runDemoClassDemo(final String className, final Class<T> returnType) throws DemoUtilException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(entryWith(className, returnType));
        }

        final T result = runDemoClassMethod(className, "demo", returnType);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(exitWith(result));
        }

        return result;
    }

    /// Return true if the specified class
    /// implements the Demo interface.
    ///
    /// @param  clazz   java.lang.Class<?>
    private static boolean isDemoClass(final Class<?> clazz) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(entryWith(clazz));
        }

        boolean result = false;

        final Class<?>[] interfaces = clazz.getInterfaces();

        for (final Class<?> interfaceClass : interfaces) {
            if (Demo.class.equals(interfaceClass)) {
                result = true;

                break;
            }
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(exitWith(result));
        }

        return result;
    }
}
