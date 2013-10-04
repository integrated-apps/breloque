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

import java.util.Locale;
import java.util.logging.Logger;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public final class StringUtils {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static

    private static final Logger LOGGER = Logger.getLogger(StringUtils.class.getName());

    public static String capitalize(
            final String string) {

        return capitalize(string, Locale.getDefault());
    }

    public static String capitalize(
            final String string,
            final Locale locale) {

        if (string == null) {
            return null;
        }

        if ("".equals(string)) {
            return "";
        }

        if (string.length() == 1) {
            return string.toUpperCase(locale);
        }

        return string.substring(0, 1).toUpperCase(locale) + string.substring(1);
    }

    public static String decapitalize(
            final String string) {

        return decapitalize(string, Locale.getDefault());
    }

    public static String decapitalize(
            final String string,
            final Locale locale) {

        if (string == null) {
            return null;
        }

        if ("".equals(string)) {
            return "";
        }

        if (string.length() == 1) {
            return string.toLowerCase(locale);
        }

        return string.substring(0, 1).toLowerCase(locale) + string.substring(1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Instance

    private StringUtils(
            ) {

        // Does nothing.
    }

}
