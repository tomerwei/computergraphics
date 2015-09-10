/**
 * The GrammarRule Class Save all Attributes from A Grammar
 * 
 * @author Thorben Watzl
 */
package cgresearch.studentprojects.shapegrammar.datastructures.rules;

import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualMethod;

public class GrammarRule{
	private String name;
	private VirtualMethod vMethod;
	private String texture;
	private String model3d;
	private String modelRotation; 
	
	/**
	 * Special Constructor For The GrammarRule Class
	 * 
	 * @param name This Is The Name Of The Rule
	 * @param method Contains The Virtual Method from The Rule
	 * @param texture Contains The ID Name for a Texture
	 */
	public GrammarRule(String name, VirtualMethod method, String texture) {
		this.name = name;
		this.vMethod = method;
		this.texture = texture;
	}

	/**
	 * Default Constructor For The GrammarRule Class
	 */
	public GrammarRule() {
		this.vMethod = null;
		this.name = null;
		this.texture = null;
	}
	
	/**
	 * Return The Grammar Name
	 * @return Grammar Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set The Grammar Name
	 * @param name New Grammar Name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return Virtual Method
	 * @return Virtual Method
	 */
	public VirtualMethod getMethod() {
		return vMethod;
	}

	/**
	 * Set The Virtual Method
	 * @param method New Virtual Method
	 */
	public void setMethod(VirtualMethod method) {
		this.vMethod = method;
	}

	/**
	 * Return Texture ID Name
	 * @return Texture ID Name
	 */
	public String getTexture() {
		return texture;
	}

	/**
	 * Set The Texture ID Name
	 * @param method New Texture ID Name
	 */
	public void setTexture(String texture) {
		this.texture = texture;
	}

	/**
	 * Return Path to 3D Model
	 * @return Path to 3D Model
	 */
	public String getModel3d() {
		return model3d;
	}

	/**
	 * Set The Path to the 3D Model
	 * @param method New Path to the 3D Model
	 */
	public void setModel3d(String model3d) {
		this.model3d = model3d;
	}
	
	/**
	 * Return Rotation from 3D Model
	 * @return Rotation from 3D Model
	 */
	public String getModel3dRotation() {
		return modelRotation;
	}

	/**
	 * Set The Rotation from the 3D Model
	 * @param method New Rotation from the 3D Model
	 */
	public void setModel3dRotation(String model3dRotation) {
		this.modelRotation = model3dRotation;
	}
	
}
