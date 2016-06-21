package cgresearch.rendering.core;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;

/**
 * This class provides an interface for changing certain parameters that were hard-coded constants of the renderer.
 * It was designed to be able to change the background colour of the scene.
 * Created by christian on 18.06.16.
 */
public class RendererOptions {
    /**
     * Default clear color.
     */
    static final Vector CLEAR_COLOR = VectorFactory.createVector3(1, 1, 1);
    //TODO move default constants from JoglRenderer3D to here

    /**
     * Set up the clear color of the scene.
     */
    private Vector clearColor = new Vector(CLEAR_COLOR);

    /**
     * Getter.
     * @return the current clear color
     */
    public Vector getClearColor() {
        return clearColor;
    }

    /**
     * Setter.
     * @param clearColor the new clear color
     */
    public void setClearColor(Vector clearColor) {
        this.clearColor = clearColor;
    }

    public static RendererOptions defaultRendererOptions() {
        return new RendererOptions();
    }
}
