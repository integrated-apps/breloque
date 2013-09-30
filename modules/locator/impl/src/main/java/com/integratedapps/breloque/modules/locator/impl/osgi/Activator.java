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
package com.integratedapps.breloque.modules.locator.impl.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Provides additional setup and teardown logic for this bundle.
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public final class Activator implements BundleActivator {

    /**
     * Implements additional bundle setup logic, not described in the blueprint files.
     *
     * @param context
     *      Bundle context.
     * @throws Exception
     *      In case any error occurs.
     */
    @Override
    public void start(
            final BundleContext context) throws Exception {

        // Does nothing.
    }

    /**
     * Implements additional bundle teardown logic, not handled by blueprint.
     *
     * @param context
     *      Bundle context.
     * @throws Exception
     *      In case any error occurs.
     */
    @Override
    public void stop(
            final BundleContext context) throws Exception {

        // Does nothing.
    }

}
