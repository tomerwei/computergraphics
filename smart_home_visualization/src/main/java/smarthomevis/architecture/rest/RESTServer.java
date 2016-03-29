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

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("/devices", DeviceResource.class);
        router.attach("/devices/", DeviceResource.class);
        router.attach("/devices/{id}", DeviceResource.class);
        router.attach("/devices/{id}/", DeviceResource.class);

        router.attach("/layers", DeviceLayerResource.class);
        router.attach("/layers/", DeviceLayerResource.class);
        router.attach("/layers/{id}", DeviceLayerResource.class);
        router.attach("/layers/{id}/", DeviceLayerResource.class);

        router.attach("/layers/{layerId}/devices", DeviceLayerResource.class);
        router.attach("/layers/{layerId}/devices/", DeviceLayerResource.class);
        router.attach("/layers/{layerId}/devices/{deviceId}", DeviceLayerResource.class);
        router.attach("/layers/{layerId}/devices/{deviceId}/", DeviceLayerResource.class);
        return router;
    }

    private static void addProtocols(Component component) {
        int port = Configuration.getRESTport();

        component.getServers().add(Protocol.HTTP, port);
        //component.getServers().add(Protocol.HTTPS, port);
    }
}
