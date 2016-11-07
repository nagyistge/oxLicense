package org.xdi.oxd.licenser.server.ws;

import com.google.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.xdi.oxd.license.client.Jackson;
import org.xdi.oxd.license.client.js.Configuration;
import org.xdi.oxd.licenser.server.conf.ConfigurationFactory;
import org.xdi.oxd.rs.protect.RsProtector;
import org.xdi.oxd.rs.protect.RsResource;
import org.xdi.oxd.rs.protect.StaticStorage;
import org.xdi.oxd.rs.protect.resteasy.ObtainPatProvider;
import org.xdi.oxd.rs.protect.resteasy.PatProvider;
import org.xdi.oxd.rs.protect.resteasy.ResourceRegistrar;
import org.xdi.oxd.rs.protect.resteasy.RptPreProcessInterceptor;
import org.xdi.oxd.rs.protect.resteasy.ServiceProvider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 24/06/2016
 */
@Provider
@ServerInterceptor
public class UmaProtector implements PreProcessInterceptor {

    private static final Logger LOG = Logger.getLogger(UmaProtector.class);
    private static volatile boolean initialized = false;

    private RptPreProcessInterceptor interceptor;
    private Configuration conf;

    @Inject
    public UmaProtector(Configuration conf) {
        try {
            this.conf = conf;
            LOG.info("Configuration : " + conf);

            initIfNeeded();
            interceptor = new RptPreProcessInterceptor(StaticStorage.get(ResourceRegistrar.class)) {
                @Override
                public String getPath(HttpRequest request) {
                    return PathPatcher.patchPath(super.getPath(request));
                }
            };
            LOG.info("UMA Protector started successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to create UMA HTTP interceptor.", e);
        }
    }

    private void initIfNeeded() {
        if (initialized) {
            return;
        }

        init();

        initialized = true;
    }

    private void init() {
        try {
            Collection<RsResource> values = resources();

            if (values.isEmpty()) {
                throw new RuntimeException("Failed to load UMA protection_document.");
            }

            LOG.info("Protection configuration: " + Jackson.asJsonSilently(values));

            ServiceProvider serviceProvider = new ServiceProvider(conf.getOpHost());

            org.xdi.oxd.rs.protect.resteasy.Configuration umaLibConf = new org.xdi.oxd.rs.protect.resteasy.Configuration();
            umaLibConf.setOpHost(conf.getOpHost());
            umaLibConf.setTrustAll(true);
            umaLibConf.setUmaPatClientId(conf.getUmaPatClientId());
            umaLibConf.setUmaPatClientSecret(conf.getUmaPatClientSecret());

            ObtainPatProvider patProvider = new ObtainPatProvider(serviceProvider, umaLibConf);
            ResourceRegistrar resourceRegistrar = new ResourceRegistrar(patProvider, serviceProvider);

            resourceRegistrar.register(values);
            LOG.info("Resources are registered at AS: " + umaLibConf);

            StaticStorage.put(PatProvider.class, patProvider);
            StaticStorage.put(ResourceRegistrar.class, resourceRegistrar);

            LOG.info("Resource Server started successfully.");
        } catch (Exception e) {
            LOG.error("Failed to initialize UMA Protector. " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private Collection<RsResource> resources() {
        InputStream stream = null;
        try {
            LOG.info("UMA protect file location: " + ConfigurationFactory.UMA_PROTECT_FILE_LOCATION);
            final File configFile = new File(ConfigurationFactory.UMA_PROTECT_FILE_LOCATION);
            if (configFile.exists()) {
                stream = new FileInputStream(configFile);
            } else {
                LOG.error("No configuration file. Fail to start! Location: " + ConfigurationFactory.UMA_PROTECT_FILE_LOCATION);
            }
            return RsProtector.instance(stream).getResourceMap().values();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    @Override
    public ServerResponse preProcess(HttpRequest request, ResourceMethod method) throws Failure, WebApplicationException {
        return interceptor.preProcess(request, method);
    }
}
