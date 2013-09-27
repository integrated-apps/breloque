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
package com.integratedapps.breloque.commons.api.dataaccess;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public abstract class Entity {

    protected long id;
    protected int version;

    protected Entity(
            ) {

        this.id = DEFAULT_ID;
        this.version = DEFAULT_VERSION;
    }

    protected Entity(
            final long id,
            final int version) {

        this.id = id;
        this.version = version;
    }

    public final long getId(
            ) {

        return id;
    }

    public final void setId(
            long id) {

        this.id = id;
    }

    public final int getVersion(
            ) {

        return version;
    }

    public final void setVersion(
            final int version) {

        this.version = version;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Constants

    public static final long DEFAULT_ID = -1L;

    public static final int DEFAULT_VERSION = -1;

}
