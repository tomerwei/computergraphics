package smarthomevis.architecture.rest;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

public class Server extends Application {

    public static void main(String[] args) throws Exception {
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8183);
        Application application = new Server();
        String contextRoot = "/smarthome";
        component.getDefaultHost().attach(contextRoot, application);
        component.start();
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/devices", DeviceResource.class);
        router.attach("/devices/{id}", DeviceResource.class);

        router.attach("/layers", LayerResource.class);
        router.attach("/layers/{id}", LayerResource.class);

        router.attach("/layers/{layerId}/devices/{deviceId}", LayerDeviceResource.class);
        return router;
    }
}
