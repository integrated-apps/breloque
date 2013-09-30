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
package com.integratedapps.breloque.commons.impl.expressions;

import com.integratedapps.breloque.commons.api.expressions.ExpressionEvaluationException;
import com.integratedapps.breloque.commons.api.expressions.ExpressionEvaluator;
import com.integratedapps.breloque.commons.impl.expressions.spi.ExpressionFunction;
import java.util.List;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public class PluggableEvaluatorImpl implements ExpressionEvaluator {

    private List<ExpressionFunction> functions;

    @Override
    public Object evaluate(
            final Object context,
            final String expression) throws ExpressionEvaluationException {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T evaluate(
            final Object context,
            final String expression,
            final Class<T> clazz) throws ExpressionEvaluationException {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setFunctions(
            final List<ExpressionFunction> functions) {

        this.functions = functions;
    }

}