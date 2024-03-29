package org.squonk.core.camel;

import org.apache.camel.component.servletlistener.CamelContextLifecycle;
import org.apache.camel.component.servletlistener.ServletCamelContext;
import org.apache.camel.impl.SimpleRegistry;

/**
 *
 * @author timbo
 */
public class ServletCamelLifeCycle implements CamelContextLifecycle<SimpleRegistry> {

    private final CamelLifeCycle worker = new CamelLifeCycle();

    @Override
    public void beforeStart(ServletCamelContext scc, SimpleRegistry r) throws Exception {
        worker.beforeStart(scc, r);
    }

    @Override
    public void afterStart(ServletCamelContext scc, SimpleRegistry r) throws Exception {
        worker.afterStart(scc, r);
    }

    @Override
    public void beforeStop(ServletCamelContext scc, SimpleRegistry r) throws Exception {
        worker.beforeStop(scc, r);
    }

    @Override
    public void afterStop(ServletCamelContext scc, SimpleRegistry r) throws Exception {
        worker.afterStop(scc, r);
    }

    @Override
    public void beforeAddRoutes(ServletCamelContext scc, SimpleRegistry r) throws Exception {
        worker.beforeAddRoutes(scc, r);
    }

    @Override
    public void afterAddRoutes(ServletCamelContext scc, SimpleRegistry r) throws Exception {
        worker.afterAddRoutes(scc, r);
    }

}
