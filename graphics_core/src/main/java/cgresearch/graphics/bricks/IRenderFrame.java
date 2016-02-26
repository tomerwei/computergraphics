/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.bricks;

import cgresearch.graphics.material.IGlslShaderCompiler;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Interface for all rendering frames with different 3D engines. The generic
 * parameter T is the type of the render scene graph node.
 * 
 * @author Philipp Jenke
 * 
 */
public interface IRenderFrame<T> {

	/**
	 * Register an additional render objects factory.
	 * 
	 * @param factory
	 *            Corresponding factory.
	 */
	public void registerRenderObjectsFactory(IRenderObjectsFactory<T> factory);

	/**
	 * Return a shader compiler instance.
	 */
  public IGlslShaderCompiler getShaderCompiler();

}
