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
package com.integratedapps.breloque.modules.locator.impl;

import com.integratedapps.breloque.commons.api.data.Entity;
import com.integratedapps.breloque.modules.locator.api.Locator;
import com.integratedapps.breloque.modules.locator.api.LocatorException;
import java.util.List;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public final class LocatorImpl implements Locator {

    @Override
    public Entity locate(
            final String subject,
            final Object context) throws LocatorException {

        return null;
    }

    @Override
    public <T extends Entity> T locate(
            final String subject,
            final Object context,
            final Class<T> clazz) throws LocatorException {

        return null;
    }

    @Override
    public List<Entity> locateAll(
            final String subject,
            final Object context) throws LocatorException {

        return null;
    }

    @Override
    public <T extends Entity> List<T> locateAll(
            final String subject,
            final Object context,
            final Class<T> clazz) throws LocatorException {

        return null;
    }

}
