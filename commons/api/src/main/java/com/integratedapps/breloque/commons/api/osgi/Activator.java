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
package com.integratedapps.breloque.commons.api.osgi;

import com.integratedapps.breloque.commons.api.data.Entity;
import com.integratedapps.breloque.commons.api.data.MarshallException;
import com.integratedapps.breloque.commons.api.data.MarshallManager;
import com.integratedapps.breloque.commons.api.data.StorageException;
import com.integratedapps.breloque.commons.api.data.StorageManager;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides additional setup and teardown logic for this bundle. Also tracks events that occur with other bundles and
 * reacts accordingly.
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public final class Activator implements BundleActivator, BundleListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static

    private static final Logger LOGGER = Logger.getLogger(Activator.class.getName());

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Instance

    private ServiceTracker storageManagerTracker;
    private ServiceTracker marshallManagerTracker;

    /**
     * Implements additional bundle setup logic, not described in the blueprint files.
     *
     * <p>
     * This method registers this instance as the bundle listener (to load and register the entities that can be found
     * in other bundles) and creates/starts the {@link #storageManagerTracker} and the {@link #marshallManagerTracker}.
     *
     * @param context
     *      Bundle context.
     * @throws Exception
     *      In case any error occurs.
     */
    @Override
    public void start(
            final BundleContext context) throws Exception {

        storageManagerTracker = new ServiceTracker(context, StorageManager.class, null);
        marshallManagerTracker = new ServiceTracker(context, MarshallManager.class, null);

        storageManagerTracker.open();
        marshallManagerTracker.open();

        context.addBundleListener(this);
    }

    /**
     * Implements additional bundle teardown logic, not handled by blueprint.
     *
     * <p>
     * This method uninstalls the bundle listener and closes the {@link #storageManagerTracker} and the {@link
     * #marshallManagerTracker}.
     *
     * @param context
     *      Bundle context.
     * @throws Exception
     *      In case any error occurs.
     */
    @Override
    public void stop(
            final BundleContext context) throws Exception {

        context.removeBundleListener(this);

        storageManagerTracker.close();
        marshallManagerTracker.close();
    }

    /**
     * Handles the lifecycle events that occur with other bundles in the same OSGi container.
     *
     * <p>
     * This method invokes {@link #loadAndRegisterClasses(Bundle)} whenever a bundle is started.
     *
     * @param event
     *      Description of the event, an instance of {@link BundleEvent}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void bundleChanged(
            final BundleEvent event) {

        if (event.getType() == BundleEvent.STARTED) {
            loadAndRegisterClasses(event.getBundle());
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    private void loadAndRegisterClasses(
            final Bundle bundle) {

        try {
            final BundleContext context = bundle.getBundleContext();

            final Enumeration clazzes = bundle.findEntries("/", "*.class", true);
            while (clazzes.hasMoreElements()) {
                final String clazzPath = ((URL) clazzes.nextElement()).getPath();
                final String clazzName = clazzPath.substring(1).replace(".class", "").replace("/", ".");

                final Class<?> clazz = bundle.loadClass(clazzName);
                boolean isEntity = false;

                Class<?> superClazz = clazz.getSuperclass();
                while (superClazz != null) {
                    if (Entity.class.equals(superClazz)) {
                        isEntity = true;
                    }
                    superClazz = superClazz.getSuperclass();
                }

                if (isEntity) {
                    final ServiceReference[] storageManagerRefs = storageManagerTracker.getServiceReferences();
                    if (storageManagerRefs != null) {
                        for (ServiceReference storageManagerRef : storageManagerRefs) {
                            try {
                                ((StorageManager) context.getService(storageManagerRef)).register(clazz);
                            } catch (StorageException e) {
                                LOGGER.log(Level.SEVERE,
                                        "Failed to register class in a storage manager.", e);
                            }
                        }
                    }

                    final ServiceReference[] marshallManagerRefs = marshallManagerTracker.getServiceReferences();
                    if (marshallManagerRefs != null) {
                        for (ServiceReference marshallManagerRef : marshallManagerRefs) {
                            try {
                                ((MarshallManager) context.getService(marshallManagerRef)).register(clazz);
                            } catch (MarshallException e) {
                                LOGGER.log(Level.SEVERE,
                                        "Failed to register class in a marshall manager.", e);
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to load class for registration.", e);
        }
    }

}
