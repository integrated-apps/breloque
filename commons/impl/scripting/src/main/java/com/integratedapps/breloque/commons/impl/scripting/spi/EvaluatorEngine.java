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
package com.integratedapps.breloque.commons.impl.scripting.spi;

import com.integratedapps.breloque.commons.api.scripting.ScriptEvaluationException;
import com.integratedapps.breloque.commons.api.scripting.ScriptEvaluator;
import java.util.Map;

/**
 * Handles the evaluation of scripts of a certain MIME type.
 *
 * <p>
 * Services implementing this interface are used by the default implementation of the {@link
 * ScriptEvaluator}.
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public interface EvaluatorEngine {

    /**
     * Evaluates the provided script and returns its result.
     *
     * @param script
     *      Source of the script which should be evaluated.
     * @param context
     *      Context of the evaluation. Variables available to the script and their values.
     * @return
     *      Whatever is the result of the script execution. If the script does not return a
     *      value, <code>null</code> is returned.
     * @throws ScriptEvaluationException
     *      In case any error occurs during script evaluation.
     */
    Object evaluate(
            final String script,
            final Map<String, Object> context) throws ScriptEvaluationException;

    /**
     * Returns the MIME type of scripts this engine is capable of working with.
     *
     * @return
     *      MIME type of scripts this engine is capable of working with.
     */
    String getMimeType(
            );

}
