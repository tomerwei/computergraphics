package smarthomevis.architecture.rest;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class RootResource extends ServerResource {

    @Get
    public String getRoot() {
        return "This is the root node";
    }
}
