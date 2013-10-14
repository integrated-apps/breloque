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

import java.util.List;
import java.util.Map;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public interface StorageManager {

    // Letting storage manager know of an entity class - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Lets the storage manager know that a certain entity class is available. Depending on the storage manager
     * implementation it may undertake certain actions to prepare for handling this entity class, for example,
     * initialize the database tables.
     *
     * <p>
     * In no way, though, should the implementation retain hard links on the provided class object.
     *
     * @param clazz
     *      Entity class.
     * @throws StorageException
     *      If something goes wrong while registering the entity class.
     */
    void register(
            Class<?> clazz) throws StorageException;

    // Accessing, storing and deleting entities  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    <T extends Entity> List<T> list(
            Class<T> clazz) throws StorageException;

    Entity get(
            long id) throws StorageException;

    <T extends Entity> T get(
            long id,
            Class<T> clazz) throws StorageException;

    Entity get(
            long id,
            int version) throws StorageException;

    <T extends Entity> T get(
            long id,
            int version,
            Class<T> clazz) throws StorageException;

    <T extends Entity> T store(
            T entity) throws StorageException;

    Entity delete(
            long id) throws StorageException;

    <T extends Entity> T delete(
            long id,
            Class<T> clazz) throws StorageException;

    Entity delete(
            long id,
            int version) throws StorageException;

    <T extends Entity> T delete(
            long id,
            int version,
            Class<T> clazz) throws StorageException;

    // Searching - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    <T extends Entity> List<T> search(
            Map<String, Object> query,
            Class<T> clazz) throws StorageException;

    // Handling transactions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    String begin(
            ) throws StorageException;

    void begin(
            String trid) throws StorageException;

    void commit(
            ) throws StorageException;

    void commit(
            String trid) throws StorageException;

    void rollback(
            ) throws StorageException;

    void rollback(
            String trid) throws StorageException;

}
