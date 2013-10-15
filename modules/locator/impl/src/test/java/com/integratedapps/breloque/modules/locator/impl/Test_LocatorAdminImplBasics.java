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

import com.integratedapps.breloque.commons.api.data.entities.DummyEntity;
import com.integratedapps.breloque.commons.impl.data.InMemoryStorageManager;
import com.integratedapps.breloque.modules.locator.api.entities.BusinessRule;
import com.integratedapps.breloque.modules.locator.api.entities.Mapping;
import com.integratedapps.breloque.modules.locator.api.entities.Script;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public class Test_LocatorAdminImplBasics {

    @Test
    public void addOneAndThenList(
            ) throws Exception {

        DummyEntity entity;
        BusinessRule businessRule;
        Script script;
        Mapping mapping;
        List<Mapping> mappings;

        final InMemoryStorageManager storageManager = InMemoryStorageManager.getInstance();
        storageManager.reset();

        final LocatorAdminImpl locatorAdmin = new LocatorAdminImpl();
        locatorAdmin.setStorageManager(storageManager);

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

        mapping = locatorAdmin.add("test-service", entity, businessRule);
        Assert.assertEquals(0, mapping.getIndex());
        Assert.assertEquals("test-service", mapping.getSubject());
        Assert.assertEquals(entity.getId(), mapping.getEntityId());
        Assert.assertEquals(businessRule.getId(), mapping.getBusinessRuleId());

        mappings = locatorAdmin.list("test-service");
        Assert.assertEquals(1, mappings.size());
        Assert.assertEquals(0, mappings.get(0).getIndex());
        Assert.assertEquals("test-service", mappings.get(0).getSubject());
        Assert.assertEquals(entity.getId(), mappings.get(0).getEntityId());
        Assert.assertEquals(businessRule.getId(), mappings.get(0).getBusinessRuleId());
    }

    @Test
    public void addTwoAndSwitchPlaces(
            ) throws Exception {

        DummyEntity entity1;
        DummyEntity entity2;
        BusinessRule businessRule;
        Script script;

        Mapping mapping;
        List<Mapping> mappings;

        final InMemoryStorageManager storageManager = InMemoryStorageManager.getInstance();
        storageManager.reset();

        final LocatorAdminImpl locatorAdmin = new LocatorAdminImpl();
        locatorAdmin.setStorageManager(storageManager);

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
        script.setCode("return true;\n");

        script = storageManager.store(script);

        businessRule = new BusinessRule();
        businessRule.setScriptId(script.getId());

        businessRule = storageManager.store(businessRule);

        mapping = locatorAdmin.add("test-service", entity1, businessRule);
        Assert.assertEquals(0, mapping.getIndex());
        Assert.assertEquals("test-service", mapping.getSubject());
        Assert.assertEquals(entity1.getId(), mapping.getEntityId());
        Assert.assertEquals(businessRule.getId(), mapping.getBusinessRuleId());

        mapping = locatorAdmin.add("test-service", entity2, businessRule);
        Assert.assertEquals(10, mapping.getIndex());
        Assert.assertEquals("test-service", mapping.getSubject());
        Assert.assertEquals(entity2.getId(), mapping.getEntityId());
        Assert.assertEquals(businessRule.getId(), mapping.getBusinessRuleId());

        mappings = locatorAdmin.list("test-service");
        Assert.assertEquals(2, mappings.size());
        Assert.assertEquals(0, mappings.get(0).getIndex());
        Assert.assertEquals("test-service", mappings.get(0).getSubject());
        Assert.assertEquals(entity1.getId(), mappings.get(0).getEntityId());
        Assert.assertEquals(businessRule.getId(), mappings.get(0).getBusinessRuleId());
        Assert.assertEquals(10, mappings.get(1).getIndex());
        Assert.assertEquals("test-service", mappings.get(1).getSubject());
        Assert.assertEquals(entity2.getId(), mappings.get(1).getEntityId());
        Assert.assertEquals(businessRule.getId(), mappings.get(1).getBusinessRuleId());

        locatorAdmin.moveUp(mappings.get(1).getId());

        mappings = locatorAdmin.list("test-service");
        Assert.assertEquals(2, mappings.size());
        Assert.assertEquals(0, mappings.get(0).getIndex());
        Assert.assertEquals("test-service", mappings.get(0).getSubject());
        Assert.assertEquals(entity2.getId(), mappings.get(0).getEntityId());
        Assert.assertEquals(businessRule.getId(), mappings.get(0).getBusinessRuleId());
        Assert.assertEquals(10, mappings.get(1).getIndex());
        Assert.assertEquals("test-service", mappings.get(1).getSubject());
        Assert.assertEquals(entity1.getId(), mappings.get(1).getEntityId());
        Assert.assertEquals(businessRule.getId(), mappings.get(1).getBusinessRuleId());

        locatorAdmin.moveDown(mappings.get(0).getId());

        mappings = locatorAdmin.list("test-service");
        Assert.assertEquals(2, mappings.size());
        Assert.assertEquals(0, mappings.get(0).getIndex());
        Assert.assertEquals("test-service", mappings.get(0).getSubject());
        Assert.assertEquals(entity1.getId(), mappings.get(0).getEntityId());
        Assert.assertEquals(businessRule.getId(), mappings.get(0).getBusinessRuleId());
        Assert.assertEquals(10, mappings.get(1).getIndex());
        Assert.assertEquals("test-service", mappings.get(1).getSubject());
        Assert.assertEquals(entity2.getId(), mappings.get(1).getEntityId());
        Assert.assertEquals(businessRule.getId(), mappings.get(1).getBusinessRuleId());
    }

    @Test
    public void addTwoAndDelete(
            ) throws Exception {

        DummyEntity entity1;
        DummyEntity entity2;
        BusinessRule businessRule;
        Script script;

        Mapping mapping;
        List<Mapping> mappings;

        final InMemoryStorageManager storageManager = InMemoryStorageManager.getInstance();
        storageManager.reset();

        final LocatorAdminImpl locatorAdmin = new LocatorAdminImpl();
        locatorAdmin.setStorageManager(storageManager);

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
        script.setCode("return true;\n");

        script = storageManager.store(script);

        businessRule = new BusinessRule();
        businessRule.setScriptId(script.getId());

        businessRule = storageManager.store(businessRule);

        mapping = locatorAdmin.add("test-service", entity1, businessRule);
        Assert.assertEquals(0, mapping.getIndex());
        Assert.assertEquals("test-service", mapping.getSubject());
        Assert.assertEquals(entity1.getId(), mapping.getEntityId());
        Assert.assertEquals(businessRule.getId(), mapping.getBusinessRuleId());

        mapping = locatorAdmin.add("test-service", entity2, businessRule);
        Assert.assertEquals(10, mapping.getIndex());
        Assert.assertEquals("test-service", mapping.getSubject());
        Assert.assertEquals(entity2.getId(), mapping.getEntityId());
        Assert.assertEquals(businessRule.getId(), mapping.getBusinessRuleId());

        mappings = locatorAdmin.list("test-service");
        Assert.assertEquals(2, mappings.size());
        Assert.assertEquals(0, mappings.get(0).getIndex());
        Assert.assertEquals("test-service", mappings.get(0).getSubject());
        Assert.assertEquals(entity1.getId(), mappings.get(0).getEntityId());
        Assert.assertEquals(businessRule.getId(), mappings.get(0).getBusinessRuleId());
        Assert.assertEquals(10, mappings.get(1).getIndex());
        Assert.assertEquals("test-service", mappings.get(1).getSubject());
        Assert.assertEquals(entity2.getId(), mappings.get(1).getEntityId());
        Assert.assertEquals(businessRule.getId(), mappings.get(1).getBusinessRuleId());

        locatorAdmin.delete(mappings.get(0).getId());

        mappings = locatorAdmin.list("test-service");
        Assert.assertEquals(1, mappings.size());
        Assert.assertEquals(0, mappings.get(0).getIndex());
        Assert.assertEquals("test-service", mappings.get(0).getSubject());
        Assert.assertEquals(entity2.getId(), mappings.get(0).getEntityId());
        Assert.assertEquals(businessRule.getId(), mappings.get(0).getBusinessRuleId());

        locatorAdmin.delete(mappings.get(0).getId());

        mappings = locatorAdmin.list("test-service");
        Assert.assertEquals(0, mappings.size());
    }

}
