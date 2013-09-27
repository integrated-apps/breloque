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
package com.integratedapps.breloque.commons.impl.dataaccess;

import com.integratedapps.breloque.commons.api.dataaccess.MarshalAccessObject;
import com.integratedapps.breloque.commons.api.dataaccess.MarshalException;
import com.integratedapps.breloque.commons.impl.dataaccess.spi.Marshaler;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public class PluggableMarshalAccessObjectImpl implements MarshalAccessObject {

    private List<Marshaler> marshalers;

    @Override
    public String marshal(
            final Object entity,
            final String mimeType) throws MarshalException {

        for (Marshaler marshaler : marshalers) {
            if (marshaler.getMimeType().equals(mimeType)) {
                return marshaler.marshal(entity);
            }
        }

        throw new MarshalException("Unsupported mime type: '" + mimeType + "'.");
    }

    @Override
    public <T> T unmarshal(
            final String entity,
            final String mimeType,
            final Class<T> clazz) throws MarshalException {

        for (Marshaler marshaler : marshalers) {
            if (marshaler.getMimeType().equals(mimeType)) {
                return marshaler.unmarshal(entity, clazz);
            }
        }

        throw new MarshalException("Unsupported mime type: '" + mimeType + "'.");
    }

    @Override
    public Set<String> listMimeTypes(
            ) {

        final Set<String> result = new HashSet<>();
        for (Marshaler marshaler : marshalers) {
            result.add(marshaler.getMimeType());
        }

        return result;
    }

    public void setMarshalers(
            final List<Marshaler> marshalers) {

        this.marshalers = marshalers;
    }

}
