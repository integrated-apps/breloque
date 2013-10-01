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
import com.integratedapps.breloque.commons.api.data.StorageException;
import com.integratedapps.breloque.commons.api.data.StorageManager;
import com.integratedapps.breloque.modules.locator.api.LocatorAdmin;
import com.integratedapps.breloque.modules.locator.api.LocatorException;
import com.integratedapps.breloque.modules.locator.api.entities.BusinessRule;
import com.integratedapps.breloque.modules.locator.api.entities.Mapping;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public final class LocatorAdminImpl implements LocatorAdmin {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static

    private static final Logger LOGGER = Logger.getLogger(LocatorAdminImpl.class.getName());

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Instance

    private StorageManager storageManager;

    @Override
    public List<Mapping> list(
            final String subject) throws LocatorException {

        try {
            final Map<String, Object> query = new HashMap<>();
            query.put(Mapping.SUBJECT_FIELD, subject);

            final List<Mapping> mappings = storageManager.search(query, Mapping.class);
            Collections.sort(mappings, BY_INDEX);

            return mappings;
        } catch (StorageException e) {
            throw new LocatorException("Failed to list the mappings.", e);
        }
    }

    @Override
    public Mapping add(
            final String subject,
            final Entity entity) throws LocatorException {

        return add(subject, entity, null);
    }

    @Override
    public Mapping add(
            final String subject,
            final Entity entity,
            final BusinessRule businessRule) throws LocatorException {

        if ((subject == null) || "".equals(subject)) {
            throw new LocatorException(
                    "It is not possible to create a mappng with an empty (or null) subject.");
        }

        if ((entity == null) || (entity.getId() == Entity.DEFAULT_ID)) {
            throw new LocatorException(
                    "It is not possible to create a mappng with an empty (or unregistered) entity.");
        }

        final String trid = UUID.randomUUID().toString();
        try {
            storageManager.begin(trid);

            // First we fetch the list of existing mappings for this subject and select the one with the largest
            // index (it should be the last one in the list). We then increment the index, create the mappign object
            // and return.
            final List<Mapping> existing = list(subject);

            final int index;
            if (existing.isEmpty()) {
                index = INITIAL_INDEX;
            } else {
                index = existing.get(existing.size() - 1).getIndex() + INDEX_INCREMENT;
            }

            // Then we create the mapping itself and save it in the persistent storage. Whatever the storage manager
            // returns we give back to the client, provided we managed to successfully commit a transaction.
            Mapping mapping = new Mapping();
            mapping.setSubject(subject);
            mapping.setEntityId(entity.getId());
            mapping.setBusinessRuleId(businessRule == null ? Entity.DEFAULT_ID : businessRule.getId());
            mapping.setIndex(index);

            mapping = storageManager.store(mapping);
            storageManager.commit(trid);

            return mapping;
        } catch (StorageException e) {
            try {
                storageManager.rollback(trid);
            } catch (StorageException ex) {
                LOGGER.log(Level.SEVERE,
                        "Failed to rollback a storage transaction.", ex);
            }

            throw new LocatorException("Failed to add the mapping.", e);
        }
    }

    @Override
    public void moveUp(
            final long id) throws LocatorException {

        final String trid = UUID.randomUUID().toString();
        try {
            storageManager.begin(trid);

            // First we need to find out what is the subject of the mapping we'll be moving up.
            final Mapping target = storageManager.get(id, Mapping.class);

            // Then we fetch the list of existing mappings for this subject.
            final List<Mapping> existing = list(target.getSubject());

            // Then we find (yeah, again) the one we should be moving up, and if it is not the first one in the list,
            // switch its indices with the one before it.
            for (int i = 0; i < existing.size(); i++) {
                if ((existing.get(i).getId() == id) && (i > 0)) {
                    final Mapping previous = existing.get(i - 1);

                    int previousIndex = previous.getIndex();

                    previous.setIndex(target.getIndex());
                    target.setIndex(previousIndex);

                    storageManager.store(previous);
                    storageManager.store(target);

                    storageManager.commit(trid);

                    return;
                }
            }
        } catch (StorageException e) {
            try {
                storageManager.rollback(trid);
            } catch (StorageException ex) {
                LOGGER.log(Level.SEVERE,
                        "Failed to rollback a storage transaction.", ex);
            }

            throw new LocatorException("Failed to move up the mapping.", e);
        }
    }

    @Override
    public void moveDown(
            final long id) throws LocatorException {

        final String trid = UUID.randomUUID().toString();
        try {
            storageManager.begin(trid);

            // First we need to find out what is the subject of the mapping we'll be moving down.
            final Mapping target = storageManager.get(id, Mapping.class);

            // Then we fetch the list of existing mappings for this subject.
            final List<Mapping> existing = list(target.getSubject());

            // Then we find (yeah, again) the one we should be moving down, and if it is not the last one in the list,
            // switch its indices with the one after it.
            for (int i = 0; i < existing.size(); i++) {
                if ((existing.get(i).getId() == id) && (i < existing.size() - 1)) {
                    final Mapping next = existing.get(i + 1);

                    int nextIndex = next.getIndex();

                    next.setIndex(target.getIndex());
                    target.setIndex(nextIndex);

                    storageManager.store(next);
                    storageManager.store(target);

                    storageManager.commit(trid);

                    return;
                }
            }
        } catch (StorageException e) {
            try {
                storageManager.rollback(trid);
            } catch (StorageException ex) {
                LOGGER.log(Level.SEVERE,
                        "Failed to rollback a storage transaction.", ex);
            }

            throw new LocatorException("Failed to move down the mapping.", e);
        }
    }

    @Override
    public Mapping delete(
            final long id) throws LocatorException {

        final String trid = UUID.randomUUID().toString();
        try {
            storageManager.begin(trid);

            // First we need to find out what is the subject of the mapping we'll be deleting.
            final Mapping target = storageManager.get(id, Mapping.class);

            // Then we remove it, normalize the indices for those who remain and return the one we've just deleted.
            storageManager.delete(id);
            normalizeIndices(target.getSubject());

            storageManager.commit(trid);

            return target;
        } catch (StorageException e) {
            try {
                storageManager.rollback(trid);
            } catch (StorageException ex) {
                LOGGER.log(Level.SEVERE,
                        "Failed to rollback a storage transaction.", ex);
            }

            throw new LocatorException("Failed to delete the mapping.", e);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public void setStorageManager(
            final StorageManager storageManager) {

        this.storageManager = storageManager;
    }

    // Private - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void normalizeIndices(
            final String subject) throws LocatorException, StorageException {

        final List<Mapping> mappings = list(subject);

        int index = INITIAL_INDEX;
        for (int i = 0; i < mappings.size(); i++) {
            mappings.get(i).setIndex(index);
            storageManager.store(mappings.get(i));

            index = index + INDEX_INCREMENT;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constants

    private static final int INITIAL_INDEX = 0;

    private static final int INDEX_INCREMENT = 10;

    private static final Comparator<Mapping> BY_INDEX = new Comparator<Mapping>() {
        @Override
        public int compare(
                final Mapping mapping1,
                final Mapping mapping2) {

            if (mapping1.getIndex() == mapping2.getIndex()) {
                return 0;
            } else {
                return mapping1.getIndex() < mapping2.getIndex() ? -1 : 1;
            }
        }
    };

}
