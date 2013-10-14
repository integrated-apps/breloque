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
package com.integratedapps.breloque.commons.impl.data;

import com.integratedapps.breloque.commons.api.data.Entity;
import com.integratedapps.breloque.commons.api.data.StorageManager;
import com.integratedapps.breloque.commons.api.data.StorageException;
import com.integratedapps.breloque.commons.utils.ClassUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public final class InMemoryStorageManager implements StorageManager {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static

    private static final Logger LOGGER = Logger.getLogger(InMemoryStorageManager.class.getName());

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Instance

    private long maxId;

    private Map<String, List<Entity>> entities;

    public InMemoryStorageManager(
            ) {

        this.maxId = INITIAL_ID;
        this.entities = new HashMap<>();
    }

    // Letting storage manager know of an entity class - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public void register(
            final Class<?> clazz) throws StorageException {

        LOGGER.log(Level.WARNING, "REGISTERED for STORAGE: " + clazz.getName());
    }

    // Accessing, storing and deleting entities  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> List<T> list(
            final Class<T> clazz) throws StorageException {

        final String key = clazz.getName();

        if (entities.containsKey(key)) {
            final Map<Long, T> result = new HashMap<>();

            final List<Entity> keyedEntities = entities.get(key);
            for (Entity candidate : keyedEntities) {
                if (!result.containsKey(candidate.getId())
                        || (result.get(candidate.getId()).getVersion() < candidate.getVersion())) {

                    result.put(candidate.getId(), (T) candidate);
                }
            }

            return new ArrayList<>(result.values());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Entity get(
            final long id) throws StorageException {

        Entity result = null;

        final List<String> keys = new ArrayList<>(entities.keySet());
        for (String key : keys) {
            final List<Entity> keydEntities = new ArrayList<>(entities.get(key));

            for (Entity candidate : keydEntities) {
                if ((candidate.getId() == id) && ((result == null) || (result.getVersion() < candidate.getVersion()))) {
                    result = candidate;
                }
            }

            if (result != null) {
                break;
            }
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> T get(
            final long id,
            final Class<T> clazz) throws StorageException {

        final String key = clazz.getName();

        if (entities.containsKey(key)) {
            Entity result = null;

            final List<Entity> keyedEntities = new ArrayList<>(entities.get(key));
            for (Entity candidate : keyedEntities) {
                if ((candidate.getId() == id) && ((result == null) || (result.getVersion() < candidate.getVersion()))) {
                    result = candidate;
                }
            }

            return (T) result;
        }

        return null;
    }

    @Override
    public Entity get(
            final long id,
            final int version) throws StorageException {

        final List<String> keys = new ArrayList<>(entities.keySet());
        for (String key : keys) {
            final List<Entity> keydEntities = new ArrayList<>(entities.get(key));

            for (Entity candidate : keydEntities) {
                if ((candidate.getId() == id) && (candidate.getVersion() == version)) {
                    return candidate;
                }
            }
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
            for (Entity candidate : entities.get(key)) {
                if ((candidate.getId() == id) && (candidate.getVersion() == version)) {
                    return (T) candidate;
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

    @Override
    public Entity delete(
            final long id) throws StorageException {

        Entity result = null;

        final List<String> keys = new ArrayList<>(entities.keySet());
        for (String key : keys) {
            final List<Entity> keyedEntities = new ArrayList<>(entities.get(key));

            for (int i = 0; i < keyedEntities.size(); i++) {
                final Entity candidate = keyedEntities.get(i);

                if ((candidate.getId() == id)) {
                    if ((result == null) || (result.getVersion() < candidate.getVersion())) {
                        result = candidate;
                    }

                    entities.get(key).remove(candidate);
                }

            }

            if (result != null) {
                break;
            }
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> T delete(
            final long id,
            final Class<T> clazz) throws StorageException {

        final String key = clazz.getName();

        if (entities.containsKey(key)) {
            Entity result = null;

            final List<Entity> keyedEntities = new ArrayList<>(entities.get(key));
            for (Entity candidate : keyedEntities) {
                if ((candidate.getId() == id)) {
                    if ((result == null) || (result.getVersion() < candidate.getVersion())) {
                        result = candidate;
                    }

                    entities.get(key).remove(candidate);
                }
            }

            return (T) result;
        }

        return null;
    }

    @Override
    public Entity delete(
            final long id,
            final int version) throws StorageException {

        final List<String> keys = new ArrayList<>(entities.keySet());
        for (String key : keys) {
            final List<Entity> keyedEntities = new ArrayList<>(entities.get(key));

            for (int i = 0; i < keyedEntities.size(); i++) {
                final Entity candidate = keyedEntities.get(i);

                if ((candidate.getId() == id) && (candidate.getVersion() == version)) {
                    entities.get(key).remove(candidate);

                    return candidate;
                }

            }
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> T delete(
            final long id,
            final int version,
            final Class<T> clazz) throws StorageException {

        final String key = clazz.getName();

        if (entities.containsKey(key)) {
            final List<Entity> keyedEntities = new ArrayList<>(entities.get(key));
            for (Entity candidate : keyedEntities) {
                if ((candidate.getId() == id) && (candidate.getVersion() == version)) {
                    entities.get(key).remove(candidate);

                    return (T) candidate;
                }
            }
        }

        return null;
    }

    // Searching - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> List<T> search(
            final Map<String, Object> query,
            final Class<T> clazz) throws StorageException {

        try {
            final String key = clazz.getName();

            if (!entities.containsKey(key)) {
                return new ArrayList<>();
            }

            final Map<Long, T> result = new HashMap<>();

            for (Entity candidate : entities.get(key)) {
                boolean allMatch = true;
                for (String fieldName : query.keySet()) {
                    if (!ClassUtils.getFieldValue(candidate, fieldName).equals(query.get(fieldName))) {
                        allMatch = false;
                        break;
                    }
                }

                if (allMatch) {
                    if (!result.containsKey(candidate.getId())
                            || (result.get(candidate.getId()).getVersion() < candidate.getVersion())) {

                        result.put(candidate.getId(), (T) candidate);
                    }
                }
            }

            return new ArrayList<>(result.values());
        } catch (Exception e) {
            throw new StorageException("Failed to do a search on entities of class '" + clazz.getName() + "'.", e);
        }
    }

    // Handling transactions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public String begin(
            ) throws StorageException {

        // No support for transactions.

        return TRANSACTION_ID;
    }

    @Override
    public void begin(
            final String trid) throws StorageException {

        // No support for transactions.
    }

    @Override
    public void commit(
            ) throws StorageException {

        // No support for transactions.
    }

    @Override
    public void commit(
            final String trid) throws StorageException {

        // No support for transactions.
    }

    @Override
    public void rollback(
            ) throws StorageException {

        // No support for transactions.
    }

    @Override
    public void rollback(
            final String trid) throws StorageException {

        // No support for transactions.
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constants

    private static final long INITIAL_ID = 1L;

    private static final int INITIAL_VERSION = 1;

    private static final String TRANSACTION_ID = "";

}
