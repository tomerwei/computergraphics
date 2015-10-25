package smarthomevis.architecture.rest;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

public class Server extends Application {

    public static void main(String[] args) throws Exception {
        runServer(8182);
    }

    public static void runServer(int port) throws Exception {
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, port);
        Application application = new Server();
        String contextRoot = "/smarthome";
        component.getDefaultHost().attach(contextRoot, application);
        component.start();
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/datasources", DataSourceResource.class);
        router.attach("/datasources/{objectId}", DataSourceResource.class);
        return router;
    }
}
