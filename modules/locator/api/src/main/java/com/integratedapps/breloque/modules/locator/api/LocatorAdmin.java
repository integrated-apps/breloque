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
package com.integratedapps.breloque.modules.locator.api;

import com.integratedapps.breloque.commons.api.data.Entity;
import com.integratedapps.breloque.modules.locator.api.entities.BusinessRule;
import com.integratedapps.breloque.modules.locator.api.entities.Mapping;
import java.util.List;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public interface LocatorAdmin {

    List<Mapping> list(
            String subject) throws LocatorException;

    Mapping add(
            String subject,
            Entity entity) throws LocatorException;

    Mapping add(
            String subject,
            Entity entity,
            BusinessRule businessRule) throws LocatorException;

    void moveUp(
            long id) throws LocatorException;

    void moveDown(
            long id) throws LocatorException;

    Mapping delete(
            long id) throws LocatorException;

}
