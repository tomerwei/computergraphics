package smarthomevis.architecture.rest;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import smarthomevis.architecture.config.Configuration;

public class RESTServer extends Application {

    public static void main(String[] args) throws Exception {
        Component component = new Component();
        addProtocols(component);

        Application application = new RESTServer();
        String contextRoot = Configuration.getRESTroot();
        component.getDefaultHost().attach(contextRoot, application);
        component.start();
    }

    private static void addProtocols(Component component) {
        int port = Configuration.getRESTport();

        component.getServers().add(Protocol.HTTP, port);
        //component.getServers().add(Protocol.HTTPS, port);
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("", RootResource.class);
        router.attach("/", RootResource.class);

        router.attach("/devices", DevicesResource.class);
        router.attach("/devices/", DevicesResource.class);
        router.attach("/devices/{id}", DeviceResource.class);
        router.attach("/devices/{id}/", DeviceResource.class);

        router.attach("/layers", DeviceLayersResource.class);
        router.attach("/layers/", DeviceLayersResource.class);
        router.attach("/layers/{id}", DeviceLayerResource.class);
        router.attach("/layers/{id}/", DeviceLayerResource.class);

        return router;
    }
}
