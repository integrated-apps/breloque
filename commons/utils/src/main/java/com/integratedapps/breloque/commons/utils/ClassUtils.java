/*
 *  Copyright (C) 2013 integratedApps
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or
 *  substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 *  BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.integratedapps.breloque.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public final class ClassUtils {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static

    private static final Logger LOGGER = Logger.getLogger(ClassUtils.class.getName());

    public static Object getFieldValue(
            final Object object,
            final String fieldName) throws Exception {

        final Method getter = getGetter(object, fieldName);
        if (getter != null) {
            return getter.invoke(object);
        } else {
            final Field[] declared = object.getClass().getDeclaredFields();
            for (Field field : declared) {
                if (fieldName.equals(field.getName())) {
                    field.setAccessible(true);
                    return field.get(object);
                }
            }

            throw new Exception(
                    "The required field '" + fieldName + "' is not declared on the "
                  + "supplied object of class '" + object.getClass().getName() + "'.");
        }
    }

    public static Method getGetter(
            final Object object,
            final String fieldName) throws Exception {

        final String getterName = "get" + StringUtils.capitalize(fieldName);
        final String isserName = "is" + StringUtils.capitalize(fieldName);

        final Method[] declared = object.getClass().getDeclaredMethods();
        for (Method method : declared) {
            if (getterName.equals(method.getName()) && (method.getParameterTypes().length == 0)) {
                return method;
            } else if (isserName.equals(method.getName()) && (method.getParameterTypes().length == 0)
                    && (method.getReturnType().equals(Boolean.TYPE) || method.getReturnType().equals(Boolean.class))) {

                return method;
            }
        }

        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Instance

    private ClassUtils(
            ) {

        // Does nothing.
    }

}
