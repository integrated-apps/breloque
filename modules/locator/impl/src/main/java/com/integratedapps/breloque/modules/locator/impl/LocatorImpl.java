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
import com.integratedapps.breloque.commons.api.scripting.ScriptEvaluationException;
import com.integratedapps.breloque.commons.api.scripting.ScriptEvaluator;
import com.integratedapps.breloque.modules.locator.api.Locator;
import com.integratedapps.breloque.modules.locator.api.LocatorAdmin;
import com.integratedapps.breloque.modules.locator.api.LocatorException;
import com.integratedapps.breloque.modules.locator.api.entities.BusinessRule;
import com.integratedapps.breloque.modules.locator.api.entities.Mapping;
import com.integratedapps.breloque.modules.locator.api.entities.Script;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public final class LocatorImpl implements Locator {

    private StorageManager storageManager;
    private LocatorAdmin locatorAdmin;
    private ScriptEvaluator scriptEvaluator;

    @Override
    public Entity locate(
            final String subject) throws LocatorException {

        return locate(subject, (Object) null);
    }

    @Override
    public Entity locate(
            final String subject,
            final Object context) throws LocatorException {

        try {
            final List<Mapping> mappings = locatorAdmin.list(subject);

            final Map<String, Object> evaluationContext = new HashMap<>();
            evaluationContext.put("subject", subject);
            if (context != null) {
                evaluationContext.put("context", context);
            }

            for (Mapping mapping : mappings) {
                final BusinessRule businessRule = storageManager.get(mapping.getBusinessRuleId(), BusinessRule.class);

                Object businessRuleResult = null;
                if (businessRule != null) {
                    final Script script = storageManager.get(businessRule.getScriptId(), Script.class);
                    businessRuleResult = scriptEvaluator.evaluate(
                            script.getCode(), script.getMimeType(), evaluationContext);
                }

                if ((businessRule == null)
                        || ((businessRuleResult != null) && !"false".equalsIgnoreCase(businessRuleResult.toString()))) {

                    return storageManager.get(mapping.getEntityId());
                }
            }

            return null;
        } catch (StorageException | ScriptEvaluationException e) {
            throw new LocatorException("Failed to locate an entity within the given context.", e);
        }
    }

    @Override
    public <T extends Entity> T locate(
            final String subject,
            final Class<T> clazz) throws LocatorException {

        return locate(subject, null, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> T locate(
            final String subject,
            final Object context,
            final Class<T> clazz) throws LocatorException {

        try {
            final List<Mapping> mappings = locatorAdmin.list(subject);

            final Map<String, Object> evaluationContext = new HashMap<>();
            evaluationContext.put("subject", subject);
            if (context != null) {
                evaluationContext.put("context", context);
            }

            for (Mapping mapping : mappings) {
                final BusinessRule businessRule = storageManager.get(mapping.getBusinessRuleId(), BusinessRule.class);

                Object businessRuleResult = null;
                if (businessRule != null) {
                    final Script script = storageManager.get(businessRule.getScriptId(), Script.class);
                    businessRuleResult = scriptEvaluator.evaluate(
                            script.getCode(), script.getMimeType(), evaluationContext);
                }

                if ((businessRule == null)
                        || ((businessRuleResult != null) && !"false".equalsIgnoreCase(businessRuleResult.toString()))) {

                    final Entity entity = storageManager.get(mapping.getEntityId());

                    if (clazz.isAssignableFrom(entity.getClass())) {
                        return (T) entity;
                    }
                }
            }

            return null;
        } catch (StorageException | ScriptEvaluationException e) {
            throw new LocatorException("Failed to locate an entity within the given context.", e);
        }
    }

    @Override
    public List<Entity> locateAll(
            final String subject) throws LocatorException {

        return locateAll(subject, (Object) null);
    }

    @Override
    public List<Entity> locateAll(
            final String subject,
            final Object context) throws LocatorException {

        try {
            final List<Mapping> mappings = locatorAdmin.list(subject);

            final Map<String, Object> evaluationContext = new HashMap<>();
            evaluationContext.put("subject", subject);
            if (context != null) {
                evaluationContext.put("context", context);
            }

            final List<Entity> result = new ArrayList<>();

            for (Mapping mapping : mappings) {
                final BusinessRule businessRule = storageManager.get(mapping.getBusinessRuleId(), BusinessRule.class);

                Object businessRuleResult = null;
                if (businessRule != null) {
                    final Script script = storageManager.get(businessRule.getScriptId(), Script.class);
                    businessRuleResult = scriptEvaluator.evaluate(
                            script.getCode(), script.getMimeType(), evaluationContext);
                }

                if ((businessRule == null)
                        || ((businessRuleResult != null) && !"false".equalsIgnoreCase(businessRuleResult.toString()))) {

                    result.add(storageManager.get(mapping.getEntityId()));
                }
            }

            return result;
        } catch (StorageException | ScriptEvaluationException e) {
            throw new LocatorException("Failed to locate an entity within the given context.", e);
        }
    }

    @Override
    public <T extends Entity> List<T> locateAll(
            final String subject,
            final Class<T> clazz) throws LocatorException {

        return locateAll(subject, null, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> List<T> locateAll(
            final String subject,
            final Object context,
            final Class<T> clazz) throws LocatorException {

        try {
            final List<Mapping> mappings = locatorAdmin.list(subject);

            final Map<String, Object> evaluationContext = new HashMap<>();
            evaluationContext.put("subject", subject);
            if (context != null) {
                evaluationContext.put("context", context);
            }

            final List<T> result = new ArrayList<>();

            for (Mapping mapping : mappings) {
                final BusinessRule businessRule = storageManager.get(mapping.getBusinessRuleId(), BusinessRule.class);

                Object businessRuleResult = null;
                if (businessRule != null) {
                    final Script script = storageManager.get(businessRule.getScriptId(), Script.class);
                    businessRuleResult = scriptEvaluator.evaluate(
                            script.getCode(), script.getMimeType(), evaluationContext);
                }

                if ((businessRule == null)
                        || ((businessRuleResult != null) && !"false".equalsIgnoreCase(businessRuleResult.toString()))) {

                    final Entity entity = storageManager.get(mapping.getEntityId());

                    if (clazz.isAssignableFrom(entity.getClass())) {
                        result.add((T) entity);
                    }
                }
            }

            return result;
        } catch (StorageException | ScriptEvaluationException e) {
            throw new LocatorException("Failed to locate an entity within the given context.", e);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public void setStorageManager(
            final StorageManager storageManager) {

        this.storageManager = storageManager;
    }

    public void setLocatorAdmin(
            final LocatorAdmin locatorAdmin) {

        this.locatorAdmin = locatorAdmin;
    }

    public void setScriptEvaluator(
            final ScriptEvaluator scriptEvaluator) {

        this.scriptEvaluator = scriptEvaluator;
    }

}
