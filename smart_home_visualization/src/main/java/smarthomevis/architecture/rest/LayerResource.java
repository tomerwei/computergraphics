package smarthomevis.architecture.rest;

import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import smarthomevis.architecture.entities.Layer;
import smarthomevis.architecture.logic.LayerController;

public class LayerResource extends ServerResource {

    LayerController controller = new LayerController();

    @Get
    public String getLayer() {
        String id = getRequestAttributes().get("id").toString();
        Layer layer = controller.get(id);
        return layer.getName();
    }

    @Put
    public String createLayer(String name) {
        return controller.save(name);
    }
}
