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
package com.integratedapps.breloque.commons.impl.data.osgi;

import com.integratedapps.breloque.commons.api.data.Entity;
import com.integratedapps.breloque.commons.api.data.MarshallException;
import com.integratedapps.breloque.commons.api.data.StorageException;
import com.integratedapps.breloque.commons.impl.data.InMemoryStorageManager;
import com.integratedapps.breloque.commons.impl.data.PluggableMarshallManager;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

/**
 *
 * @author Kir Sorokin, kir.sorokin@integrated-apps.com
 */
public final class Activator implements BundleActivator, BundleListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static

    private static final Logger LOGGER = Logger.getLogger(Activator.class.getName());

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Instance

    /**
     * Implements additional bundle setup logic, not described in the blueprint files.
     *
     * @param context
     *      Bundle context.
     * @throws Exception
     *      In case any error occurs.
     */
    @Override
    public void start(
            final BundleContext context) throws Exception {

        context.addBundleListener(this);

        for (Bundle bundle : context.getBundles()) {
            if ((bundle.getState() == Bundle.ACTIVE) && checkBundle(bundle)) {
                loadAndRegisterClasses(bundle);
            }
        }
    }

    /**
     * Implements additional bundle teardown logic, not handled by blueprint.
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

        if ((event.getType() == BundleEvent.STARTED) && checkBundle(event.getBundle())) {
            loadAndRegisterClasses(event.getBundle());
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    private void loadAndRegisterClasses(
            final Bundle bundle) {

        final Enumeration clazzes = bundle.findEntries("/", "*.class", true);

        if (clazzes == null) {
            return;
        }

        while (clazzes.hasMoreElements()) {
            try {
                final String clazzPath = ((URL) clazzes.nextElement()).getPath();
                final String clazzName =
                        clazzPath.substring(1).replaceFirst(".class$", "").replace("/", ".");

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
                    try {
                        InMemoryStorageManager.getInstance().register(clazz);
                    } catch (StorageException e) {
                        LOGGER.log(Level.SEVERE,
                                "Failed to register class in the storage manager.", e);
                    }

                    try {
                        PluggableMarshallManager.getInstance().register(clazz);
                    } catch (MarshallException e) {
                        LOGGER.log(Level.SEVERE,
                                "Failed to register class in a marshall manager.", e);
                    }
                }
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.FINE,
                        "Failed to load class for registration.", e);
            }
        }
    }

    private boolean checkBundle(
            final Bundle bundle) {

        final String bundleName = bundle.getSymbolicName();

        for (String pattern : BUNDLES_TO_SKIP) {
            if (bundleName.matches(pattern)) {
                return false;
            }
        }

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constants

    private static final List<String> BUNDLES_TO_SKIP = Arrays.asList(new String[] {
        "javax.*",
        "joda-time",
        "org\\.apache.*",
        "org\\.codehaus.*",
        "org\\.eclipse.*",
        "org\\.objectweb.*",
        "org\\.ops4j.*",
        "org\\.springframework.*",
        "stax2.*",
        "woodstox.*",
    });

}
