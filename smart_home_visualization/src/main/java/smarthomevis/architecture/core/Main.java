package smarthomevis.architecture.core;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import smarthomevis.architecture.rest.DeviceResource;
import smarthomevis.architecture.rest.LayerDeviceResource;
import smarthomevis.architecture.rest.LayerResource;

public class Main extends Application {

    private static SmartHome smartHome;

    public static void main(String[] args) throws Exception {
        smartHome = new SmartHome();
        smartHome.initialize();

        // TODO read from config
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8183);
        Application application = new Main();
        String contextRoot = "/smarthome";
        component.getDefaultHost().attach(contextRoot, application);
        component.start();

    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.getContext().getAttributes().put("datastore", smartHome.getDatastore());
        router.getContext().getAttributes().put("rootCgNode", smartHome.getCgRootNode());

        router.attach("/devices", DeviceResource.class);
        router.attach("/devices/", DeviceResource.class);
        router.attach("/devices/{id}", DeviceResource.class);
        router.attach("/devices/{id}/", DeviceResource.class);

        router.attach("/layers", LayerResource.class);
        router.attach("/layers/", LayerResource.class);
        router.attach("/layers/{id}", LayerResource.class);
        router.attach("/layers/{id}/", LayerResource.class);

        router.attach("/layers/{layerId}/devices", LayerDeviceResource.class);
        router.attach("/layers/{layerId}/devices/", LayerDeviceResource.class);
        router.attach("/layers/{layerId}/devices/{deviceId}", LayerDeviceResource.class);
        router.attach("/layers/{layerId}/devices/{deviceId}/", LayerDeviceResource.class);
        return router;
    }
}
