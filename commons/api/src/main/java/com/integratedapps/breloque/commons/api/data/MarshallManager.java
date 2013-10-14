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
package com.integratedapps.breloque.commons.api.data;

import java.util.Set;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public interface MarshallManager {

    // Letting marshall manager know of an entity class  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Lets the marshall manager know that a certain entity class is available. Depending on the marshall manager
     * implementation it may undertake certain actions to prepare for handling this entity class, for example,
     * generate code to serialize objects of the class.
     *
     * <p>
     * In no way, though, should the implementation retain hard links on the provided class object.
     *
     * @param clazz
     *      Entity class.
     * @throws MarshallException
     *      If something goes wrong while registering the entity class.
     */
    void register(
            Class<?> clazz) throws MarshallException;

    // Marshalling and unmarshalling entities  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    String marshall(
            Object entity,
            String mimeType) throws MarshallException;

    Object marshallToNative(
            Object entity,
            String mimeType) throws MarshallException;

    <T> T unmarshall(
            String entity,
            String mimeType,
            Class<T> clazz) throws MarshallException;

    <T> T unmarshallFromNative(
            Object entity,
            String mimeType,
            Class<T> clazz) throws MarshallException;

    // Describing capabilities - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    Set<String> listMimeTypes(
            );

}
