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
import com.integratedapps.breloque.commons.api.data.entities.DummyEntity;
import com.integratedapps.breloque.commons.impl.data.InMemoryStorageManager;
import com.integratedapps.breloque.commons.impl.scripting.PluggableScriptEvaluator;
import com.integratedapps.breloque.commons.impl.scripting.RhinoEvaluatorEngine;
import com.integratedapps.breloque.commons.impl.scripting.spi.EvaluatorEngine;
import com.integratedapps.breloque.modules.locator.api.entities.BusinessRule;
import com.integratedapps.breloque.modules.locator.api.entities.Script;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public class Test_LocatorImplBasics {

    @Test
    public void singleMappingNoBusinessRule(
            ) throws Exception {

        DummyEntity entity;

        Entity result;

        final InMemoryStorageManager storageManager = InMemoryStorageManager.getInstance();
        storageManager.reset();

        final RhinoEvaluatorEngine evaluatorEngine = new RhinoEvaluatorEngine();
        final PluggableScriptEvaluator scriptEvaluator = new PluggableScriptEvaluator();
        scriptEvaluator.setEngines(Arrays.asList(new EvaluatorEngine[]{evaluatorEngine}));

        final LocatorAdminImpl locatorAdmin = new LocatorAdminImpl();
        locatorAdmin.setStorageManager(storageManager);

        final LocatorImpl locator = new LocatorImpl();
        locator.setLocatorAdmin(locatorAdmin);
        locator.setScriptEvaluator(scriptEvaluator);
        locator.setStorageManager(storageManager);

        entity = new DummyEntity();
        entity.setDummyString("dummy string 1");
        entity.setDummyInt(1);

        entity = storageManager.store(entity);

        locatorAdmin.add("test-service", entity);

        result = locator.locate("test-service");
        Assert.assertNotNull(result);
        Assert.assertEquals(entity.getClass(), result.getClass());
        Assert.assertEquals(entity.getId(), result.getId());
        Assert.assertEquals(entity.getVersion(), result.getVersion());
        Assert.assertEquals(entity.getDummyString(), ((DummyEntity) result).getDummyString());
    }

    @Test
    public void singleMappingWithBusinessRuleWithoutContext_Positive(
            ) throws Exception {

        DummyEntity entity;
        BusinessRule businessRule;
        Script script;

        Entity result;

        final InMemoryStorageManager storageManager = InMemoryStorageManager.getInstance();
        storageManager.reset();

        final RhinoEvaluatorEngine evaluatorEngine = new RhinoEvaluatorEngine();
        final PluggableScriptEvaluator scriptEvaluator = new PluggableScriptEvaluator();
        scriptEvaluator.setEngines(Arrays.asList(new EvaluatorEngine[]{evaluatorEngine}));

        final LocatorAdminImpl locatorAdmin = new LocatorAdminImpl();
        locatorAdmin.setStorageManager(storageManager);

        final LocatorImpl locator = new LocatorImpl();
        locator.setLocatorAdmin(locatorAdmin);
        locator.setScriptEvaluator(scriptEvaluator);
        locator.setStorageManager(storageManager);

        entity = new DummyEntity();
        entity.setDummyString("dummy string 1");
        entity.setDummyInt(1);

        entity = storageManager.store(entity);

        script = new Script();
        script.setMimeType("text/javascript");
        script.setCode("return true;\n");

        script = storageManager.store(script);

        businessRule = new BusinessRule();
        businessRule.setScriptId(script.getId());

        businessRule = storageManager.store(businessRule);

        locatorAdmin.add("test-service", entity, businessRule);

        result = locator.locate("test-service");
        Assert.assertNotNull(result);
        Assert.assertEquals(entity.getClass(), result.getClass());
        Assert.assertEquals(entity.getId(), result.getId());
        Assert.assertEquals(entity.getVersion(), result.getVersion());
        Assert.assertEquals(entity.getDummyString(), ((DummyEntity) result).getDummyString());
    }

    @Test
    public void singleMappingWithBusinessRuleWithContext_Positive(
            ) throws Exception {

        DummyEntity entity;
        BusinessRule businessRule;
        Script script;

        Entity result;

        final InMemoryStorageManager storageManager = InMemoryStorageManager.getInstance();
        storageManager.reset();

        final RhinoEvaluatorEngine evaluatorEngine = new RhinoEvaluatorEngine();
        final PluggableScriptEvaluator scriptEvaluator = new PluggableScriptEvaluator();
        scriptEvaluator.setEngines(Arrays.asList(new EvaluatorEngine[]{evaluatorEngine}));

        final LocatorAdminImpl locatorAdmin = new LocatorAdminImpl();
        locatorAdmin.setStorageManager(storageManager);

        final LocatorImpl locator = new LocatorImpl();
        locator.setLocatorAdmin(locatorAdmin);
        locator.setScriptEvaluator(scriptEvaluator);
        locator.setStorageManager(storageManager);

        entity = new DummyEntity();
        entity.setDummyString("dummy string 1");
        entity.setDummyInt(1);

        entity = storageManager.store(entity);

        script = new Script();
        script.setMimeType("text/javascript");
        script.setCode("return context == \"hello locator\";\n");

        script = storageManager.store(script);

        businessRule = new BusinessRule();
        businessRule.setScriptId(script.getId());

        businessRule = storageManager.store(businessRule);

        locatorAdmin.add("test-service", entity, businessRule);

        result = locator.locate("test-service", "hello locator");
        Assert.assertNotNull(result);
        Assert.assertEquals(entity.getClass(), result.getClass());
        Assert.assertEquals(entity.getId(), result.getId());
        Assert.assertEquals(entity.getVersion(), result.getVersion());
        Assert.assertEquals(entity.getDummyString(), ((DummyEntity) result).getDummyString());
    }

    @Test
    public void singleMappingWithBusinessRuleWithoutContext_Negative(
            ) throws Exception {

        DummyEntity entity;
        BusinessRule businessRule;
        Script script;

        Entity result;

        final InMemoryStorageManager storageManager = InMemoryStorageManager.getInstance();
        storageManager.reset();

        final RhinoEvaluatorEngine evaluatorEngine = new RhinoEvaluatorEngine();
        final PluggableScriptEvaluator scriptEvaluator = new PluggableScriptEvaluator();
        scriptEvaluator.setEngines(Arrays.asList(new EvaluatorEngine[]{evaluatorEngine}));

        final LocatorAdminImpl locatorAdmin = new LocatorAdminImpl();
        locatorAdmin.setStorageManager(storageManager);

        final LocatorImpl locator = new LocatorImpl();
        locator.setLocatorAdmin(locatorAdmin);
        locator.setScriptEvaluator(scriptEvaluator);
        locator.setStorageManager(storageManager);

        entity = new DummyEntity();
        entity.setDummyString("dummy string 1");
        entity.setDummyInt(1);

        entity = storageManager.store(entity);

        script = new Script();
        script.setMimeType("text/javascript");
        script.setCode("return false;\n");

        script = storageManager.store(script);

        businessRule = new BusinessRule();
        businessRule.setScriptId(script.getId());

        businessRule = storageManager.store(businessRule);

        locatorAdmin.add("test-service", entity, businessRule);

        result = locator.locate("test-service");
        Assert.assertNull(result);
    }

    @Test
    public void singleMappingWithBusinessRuleWithContext_Negative(
            ) throws Exception {

        DummyEntity entity;
        BusinessRule businessRule;
        Script script;

        Entity result;

        final InMemoryStorageManager storageManager = InMemoryStorageManager.getInstance();
        storageManager.reset();

        final RhinoEvaluatorEngine evaluatorEngine = new RhinoEvaluatorEngine();
        final PluggableScriptEvaluator scriptEvaluator = new PluggableScriptEvaluator();
        scriptEvaluator.setEngines(Arrays.asList(new EvaluatorEngine[]{evaluatorEngine}));

        final LocatorAdminImpl locatorAdmin = new LocatorAdminImpl();
        locatorAdmin.setStorageManager(storageManager);

        final LocatorImpl locator = new LocatorImpl();
        locator.setLocatorAdmin(locatorAdmin);
        locator.setScriptEvaluator(scriptEvaluator);
        locator.setStorageManager(storageManager);

        entity = new DummyEntity();
        entity.setDummyString("dummy string 1");
        entity.setDummyInt(1);

        entity = storageManager.store(entity);

        script = new Script();
        script.setMimeType("text/javascript");
        script.setCode("return context == \"hello locator\";\n");

        script = storageManager.store(script);

        businessRule = new BusinessRule();
        businessRule.setScriptId(script.getId());

        businessRule = storageManager.store(businessRule);

        locatorAdmin.add("test-service", entity, businessRule);

        result = locator.locate("test-service", "hello transplucator");
        Assert.assertNull(result);
    }

    @Test
    public void twoMappingsWithBusinessRuleWithContext_Positive(
            ) throws Exception {

        DummyEntity entity1;
        DummyEntity entity2;
        BusinessRule businessRule;
        Script script;

        Entity result;

        final InMemoryStorageManager storageManager = InMemoryStorageManager.getInstance();
        storageManager.reset();

        final RhinoEvaluatorEngine evaluatorEngine = new RhinoEvaluatorEngine();
        final PluggableScriptEvaluator scriptEvaluator = new PluggableScriptEvaluator();
        scriptEvaluator.setEngines(Arrays.asList(new EvaluatorEngine[]{evaluatorEngine}));

        final LocatorAdminImpl locatorAdmin = new LocatorAdminImpl();
        locatorAdmin.setStorageManager(storageManager);

        final LocatorImpl locator = new LocatorImpl();
        locator.setLocatorAdmin(locatorAdmin);
        locator.setScriptEvaluator(scriptEvaluator);
        locator.setStorageManager(storageManager);

        entity1 = new DummyEntity();
        entity1.setDummyString("dummy string 1");
        entity1.setDummyInt(1);

        entity1 = storageManager.store(entity1);

        entity2 = new DummyEntity();
        entity2.setDummyString("dummy string 2");
        entity2.setDummyInt(1);

        entity2 = storageManager.store(entity2);

        script = new Script();
        script.setMimeType("text/javascript");
        script.setCode("return context == \"hello transplucator\";\n");

        script = storageManager.store(script);

        businessRule = new BusinessRule();
        businessRule.setScriptId(script.getId());

        businessRule = storageManager.store(businessRule);

        locatorAdmin.add("test-service", entity1, businessRule);

        script = new Script();
        script.setMimeType("text/javascript");
        script.setCode("return context == \"hello locator\";\n");

        script = storageManager.store(script);

        businessRule = new BusinessRule();
        businessRule.setScriptId(script.getId());

        businessRule = storageManager.store(businessRule);

        locatorAdmin.add("test-service", entity2, businessRule);

        result = locator.locate("test-service", "hello locator");
        Assert.assertNotNull(result);
        Assert.assertEquals(entity2.getClass(), result.getClass());
        Assert.assertEquals(entity2.getId(), result.getId());
        Assert.assertEquals(entity2.getVersion(), result.getVersion());
        Assert.assertEquals(entity2.getDummyString(), ((DummyEntity) result).getDummyString());
    }

}
