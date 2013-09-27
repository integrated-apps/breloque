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
package com.integratedapps.breloque.commons.api.scripting;

import java.util.Map;
import java.util.Set;

/**
 * Provides the required infrastructure to evaluate scripts using scripting laguages supported by
 * the implementation.
 *
 * <p>
 * Any implementation at least supports the <code>text/javascript</code>, the full list of
 * supported scripting languages can be found using the {@link #listSupportedMimeTypes()} method.
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public interface ScriptEvaluator {

    /**
     * Evaluates the provided script and returns its result.
     *
     * @param script
     *      Source of the script which should be evaluated.
     * @param mimeType
     *      MIME type of the script, which will be used to choose the proper actual script
     *      execution engine.
     * @param context
     *      Context of the evaluation. Variables available to the script and their values.
     * @return
     *      Whatever is the result of the script execution. If the script does not return a
     *      value, <code>null</code> is returned.
     * @throws ScriptEvaluationException
     *      In case any error occurs during script evaluation.
     */
    Object evaluate(
            String script,
            String mimeType,
            Map<String, Object> context) throws ScriptEvaluationException;

    /**
     * Lists all script MIME types supported by the current implementation.
     *
     * @return
     *      Set of script MIME type supported by the implementation.
     */
    Set<String> listMimeTypes(
            );

}
