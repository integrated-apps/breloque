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
package com.integratedapps.breloque.commons.impl.scripting;

import com.integratedapps.breloque.commons.api.scripting.ScriptEvaluationException;
import com.integratedapps.breloque.commons.impl.scripting.spi.EvaluatorEngine;
import java.util.Map;
import java.util.Map.Entry;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public class RhinoEvaluatorEngine implements EvaluatorEngine {

    @Override
    public Object evaluate(
            final String script,
            final Map<String, Object> context) throws ScriptEvaluationException {

        try {
            final Context rhino = Context.enter();
            final Scriptable scope = new ImporterTopLevel(rhino);

            for (Entry<String, Object> entry : context.entrySet()) {
                scope.put(entry.getKey(), scope, entry.getValue());
            }

            final String code =
                    "function ___function() { "
                  + " "
                  + script
                  + " "
                  + "} "
                  + " "
                  + "___function(); ";

            Object result = rhino.evaluateString(scope, code, "<cmd>", 1, null);

            if (result instanceof NativeJavaObject) {
                result = ((NativeJavaObject) result).unwrap();
            }

            return result;
        } catch (Exception e) {
            throw new ScriptEvaluationException("Failed to evaluate the script.", e);
        } finally {
            Context.exit();
        }
    }

    @Override
    public String getMimeType(
            ) {

        return MIME_TYPE;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Constants

    private static final String MIME_TYPE = "text/javascript";

}
