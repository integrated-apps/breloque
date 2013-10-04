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
package com.integratedapps.breloque.modules.locator.wssoap;

import com.integratedapps.breloque.commons.api.constants.MimeTypes;
import com.integratedapps.breloque.commons.api.data.Entity;
import com.integratedapps.breloque.commons.api.data.MarshallException;
import com.integratedapps.breloque.commons.api.data.MarshallManager;
import com.integratedapps.breloque.commons.api.data.StorageManager;
import com.integratedapps.breloque.commons.api.data.entities.DummyEntity;
import com.integratedapps.breloque.commons.utils.XmlUtils;
import com.integratedapps.breloque.modules.locator.api.Locator;
import com.integratedapps.breloque.modules.locator.api.LocatorAdmin;
import com.integratedapps.breloque.modules.locator.api.LocatorException;
import com.integratedapps.ns.breloque.modules.locator.WsLocator;
import com.integratedapps.ns.breloque.modules.locator.WsLocatorError;
import com.integratedapps.ns.breloque.modules.locator.types.WsLocate;
import com.integratedapps.ns.breloque.modules.locator.types.WsLocateResponse;
import com.integratedapps.ns.breloque.types.WsError;
import com.integratedapps.ns.breloque.types.WsMetaData;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
@WebService(
        serviceName = "locator",
        portName = "port",
        targetNamespace = "http://integrated-apps.com/ns/breloque/modules/locator",
        endpointInterface = "com.integratedapps.ns.breloque.modules.locator.WsLocator",
        wsdlLocation = "META-INF/wsdl/modules/locator/service.wsdl")
public class LocatorWsImpl implements WsLocator {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static

    private static final Logger LOGGER = Logger.getLogger(LocatorWsImpl.class.getName());

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Instance

    private Locator locator;
    private LocatorAdmin locatorAdmin;
    private StorageManager storageManager;
    private MarshallManager marshallManager;

    @Override
    public WsLocateResponse locate(
            final WsLocate request) throws WsLocatorError {

        final long benchmark = System.currentTimeMillis();

        try {
            // For debugging -- to be removed  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            DummyEntity dummyEntity = new DummyEntity();
            dummyEntity.setDummyString("dummy string " + UUID.randomUUID());
            dummyEntity.setDummyInt(new Random(System.currentTimeMillis()).nextInt());

            dummyEntity = storageManager.store(dummyEntity);

            locatorAdmin.add("test-subject", dummyEntity);
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

            final Entity entity = locator.locate(request.getSubject(), request.getContext());

            if (entity == null) {
                final WsError error = new WsError();
                error.setCode("LCTR-400");
                error.setMessage("No matching entity was found.");

                throw new WsLocatorError("No matching entity was found.", error);
            }

            final WsLocateResponse response = new WsLocateResponse();
            response.setEntity(marshallManager.marshallToNative(entity, MimeTypes.TEXT_XML));

            // - - - - - - - - - - - - - - - - - -
            response.setCallerId(request.getCallerId());
            response.setCalleeId(getClass().getName());
            response.setMessageExchangeId(request.getMessageExchangeId());
            response.setMetaData(new WsMetaData());

            WsMetaData.Property property;

            property = new WsMetaData.Property();
            property.setName("breloque.date-time");
            property.setValue(XmlUtils.toXml(new Date()));
            response.getMetaData().getProperties().add(property);

            property = new WsMetaData.Property();
            property.setName("breloque.processing-time");
            property.setValue("" + (System.currentTimeMillis() - benchmark));
            response.getMetaData().getProperties().add(property);

            property = new WsMetaData.Property();
            property.setName("breloque.component-id");
            property.setValue("" + getClass().getName());
            response.getMetaData().getProperties().add(property);
            // - - - - - - - - - - - - - - - - - -

            return response;
        } catch (WsLocatorError e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to 'locate'. Location failure.", e);

            throw e;
        } catch (LocatorException e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to 'locate'. Location failure.", e);

            final WsError error = new WsError();
            error.setCode("LCTR-500");
            error.setMessage(e.getMessage());

            throw new WsLocatorError(e.getMessage(), error, e);
        } catch (MarshallException e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to 'locate'. Marshalling failure.", e);

            final WsError error = new WsError();
            error.setCode("LCTR-501");
            error.setMessage(e.getMessage());

            throw new WsLocatorError(e.getMessage(), error, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to 'locate'. Unknown failure.", e);

            final WsError error = new WsError();
            error.setCode("LCTR-999");
            error.setMessage(e.getMessage());

            throw new WsLocatorError(e.getMessage(), error, e);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public void setLocator(
            final Locator locator) {

        this.locator = locator;
    }

    public void setLocatorAdmin(
            final LocatorAdmin locatorAdmin) {

        this.locatorAdmin = locatorAdmin;
    }

    public void setStorageManager(
            final StorageManager storageManager) {

        this.storageManager = storageManager;
    }

    public void setMarshallManager(
            final MarshallManager marshallManager) {

        this.marshallManager = marshallManager;
    }

}
