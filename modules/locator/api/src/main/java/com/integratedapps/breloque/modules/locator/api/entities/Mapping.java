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
package com.integratedapps.breloque.modules.locator.api.entities;

import com.integratedapps.breloque.commons.api.data.Entity;
import com.integratedapps.breloque.commons.api.data.StorageConfig;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
@StorageConfig
public class Mapping extends Entity {

    private String subject;

    private long entityId;

    private long businessRuleId;

    private int index;

    public Mapping(
            ) {

        super();
    }

    public Mapping(
            final long id,
            final int version) {

        super(id, version);
    }

    public String getSubject(
            ) {

        return subject;
    }

    public void setSubject(
            final String subject) {

        this.subject = subject;
    }

    public long getEntityId(
            ) {

        return entityId;
    }

    public void setEntityId(
            final long entityId) {

        this.entityId = entityId;
    }

    public long getBusinessRuleId(
            ) {

        return businessRuleId;
    }

    public void setBusinessRuleId(
            final long businessRuleId) {

        this.businessRuleId = businessRuleId;
    }

    public int getIndex(
            ) {

        return index;
    }

    public void setIndex(
            final int index) {

        this.index = index;
    }

}
