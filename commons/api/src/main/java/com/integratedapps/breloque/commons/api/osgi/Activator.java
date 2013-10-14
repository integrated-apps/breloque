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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
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

    private long cachedClassesBench;
    private List<Class<?>> cachedClassesForStorageManagers;
    private List<Class<?>> cachedClassesForMarshallManagers;

    public Activator(
            ) {

        cachedClassesBench = System.currentTimeMillis();

        cachedClassesForStorageManagers = new ArrayList<>();
        cachedClassesForMarshallManagers = new ArrayList<>();
    }

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

        if (System.currentTimeMillis() - cachedClassesBench > ENTITIES_REGISTRATION_TIMEOUT) {
            cachedClassesForStorageManagers.clear();
            cachedClassesForMarshallManagers.clear();
        }

        if (event.getType() == BundleEvent.STARTED) {
            final Object[] storageManagers = storageManagerTracker.getServices();
            final Object[] marshallManagers = marshallManagerTracker.getServices();

            if (storageManagers != null) {
                for (Class<?> clazz : cachedClassesForStorageManagers) {
                    registerClassInStorageManagers(clazz, storageManagers);
                }

                cachedClassesForStorageManagers.clear();
            }

            if (marshallManagers != null) {
                for (Class<?> clazz : cachedClassesForMarshallManagers) {
                    registerClassInMarshallManagers(clazz, marshallManagers);
                }

                cachedClassesForMarshallManagers.clear();
            }

            loadAndRegisterClasses(event.getBundle(), storageManagers, marshallManagers);
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    private void loadAndRegisterClasses(
            final Bundle bundle,
            final Object[] storageManagers,
            final Object[] marshallManagers) {

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
                    if (storageManagers != null) {
                        registerClassInStorageManagers(clazz, storageManagers);
                    } else {
                        cachedClassesForStorageManagers.add(clazz);
                    }

                    if (marshallManagers != null) {
                        registerClassInMarshallManagers(clazz, marshallManagers);
                    } else {
                        cachedClassesForMarshallManagers.add(clazz);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to load class for registration.", e);
        }
    }

    private void registerClassInStorageManagers(
            final Class<?> clazz,
            final Object[] storageManagers) {

        for (Object storageManager : storageManagers) {
            try {
                ((StorageManager) storageManager).register(clazz);
            } catch (StorageException e) {
                LOGGER.log(Level.SEVERE,
                        "Failed to register class in a storage manager.", e);
            }
        }
    }

    private void registerClassInMarshallManagers(
            final Class<?> clazz,
            final Object[] marshallManagers) {

        for (Object marshallManager : marshallManagers) {
            try {
                ((MarshallManager) marshallManager).register(clazz);
            } catch (MarshallException e) {
                LOGGER.log(Level.SEVERE,
                        "Failed to register class in a marshall manager.", e);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constants

    private static final long ENTITIES_REGISTRATION_TIMEOUT = 30L * 60L * 1000L; // 30 minutes in ms.

}
