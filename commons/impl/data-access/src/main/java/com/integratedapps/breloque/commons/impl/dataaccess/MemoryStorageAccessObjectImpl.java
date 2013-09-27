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

import com.integratedapps.breloque.commons.api.dataaccess.Entity;
import com.integratedapps.breloque.commons.api.dataaccess.StorageAccessObject;
import com.integratedapps.breloque.commons.api.dataaccess.StorageException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public final class MemoryStorageAccessObjectImpl implements StorageAccessObject {

    private long maxId;

    private Map<String, List<Entity>> entities;

    public MemoryStorageAccessObjectImpl(
            ) {

        this.maxId = INITIAL_ID;
        this.entities = new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> List<T> list(
            final Class<T> clazz) throws StorageException {

        final String key = clazz.getName();

        if (entities.containsKey(key)) {
            final List<T> result = new ArrayList<>();

            for (Entity current : entities.get(key)) {
                result.add((T) current);
            }

            return result;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> T get(
            final long id,
            final Class<T> clazz) throws StorageException {

        final String key = clazz.getName();

        if (entities.containsKey(key)) {
            Entity max = null;

            for (Entity current : entities.get(key)) {
                if ((current.getId() == id)
                        && ((max == null) || (max.getVersion() < current.getVersion()))) {
                    max = current;
                }
            }

            return (T) max;
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> T get(
            final long id,
            final int version,
            final Class<T> clazz) throws StorageException {

        final String key = clazz.getName();

        if (entities.containsKey(key)) {
            for (Entity current : entities.get(key)) {
                if ((current.getId() == id) && (current.getVersion() == version)) {
                    return (T) current;
                }
            }
        }

        return null;
    }

    @Override
    public <T extends Entity> T store(
            final T entity) throws StorageException {

        final String key = entity.getClass().getName();

        if (!entities.containsKey(key)) {
            entities.put(key, new ArrayList<Entity>());
        }

        if (entity.getId() == Entity.DEFAULT_ID) {
            entity.setId(maxId);
            maxId = maxId + 1;
        }

        final Entity existing = get(entity.getId(), entity.getClass());
        if (existing == null) {
            entity.setVersion(INITIAL_VERSION);
        } else {
            entity.setVersion(existing.getVersion() + 1);
        }

        entities.get(key).add(entity);

        return entity;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Constants

    private static final long INITIAL_ID = 1L;

    private static final int INITIAL_VERSION = 1;

}
