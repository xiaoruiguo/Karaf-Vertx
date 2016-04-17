/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.nierbeck.example.vertx;

import java.util.logging.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

@Component(immediate = true, service = {})
public class VertxService {

    private final static Logger LOGGER = Logger.getLogger("VertxPublisher");
    private ServiceRegistration<Vertx> vertxRegistration;
    private ServiceRegistration<EventBus> ebRegistration;
    private Vertx vertx;

    @Activate
    public void start(BundleContext context) {
        LOGGER.info("Creating Vert.x instance");
        vertx = Vertx.vertx();
        vertxRegistration = context.registerService(Vertx.class, vertx, null);
        LOGGER.info("Vert.x service registered");
        ebRegistration = context.registerService(EventBus.class, vertx.eventBus(), null);
        LOGGER.info("Vert.x Event Bus service registered");
    }

    @Deactivate
    public void stop(BundleContext context) {
        if (ebRegistration != null) {
            ebRegistration.unregister();
            ebRegistration = null;
        }
        if (vertxRegistration != null) {
            vertxRegistration.unregister();
            vertxRegistration = null;
        }
        if (vertx != null) {
            vertx.close();
        }
    }

}