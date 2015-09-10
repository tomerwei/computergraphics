package cgresearch.graphics.material;

/**
 * Provider for resource managers (shader, textures)
 * 
 * @author Philipp Jenke
 *
 */
public class ResourceManager {
	/**
	 * Singleton instance for shaders
	 */
	private static GenericManager<CgGlslShader> shaderInstance = null;

	/**
	 * Singleton instance for textures
	 */
	private static GenericManager<CgTexture> textureInstance = null;

	/**
	 * Singleton instance access.
	 */
	public static GenericManager<CgGlslShader> getShaderManagerInstance() {
		if (shaderInstance == null) {
			shaderInstance = new GenericManager<CgGlslShader>();
			createDefaultShaders();
		}
		return shaderInstance;
	}

	/**
	 * Singleton instance access.
	 */
	public static GenericManager<CgTexture> getTextureManagerInstance() {
		if (textureInstance == null) {
			textureInstance = new GenericManager<CgTexture>();
			createDefaultTextures();
		}
		return textureInstance;
	}

	/**
	 * Create default shaders
	 */
	private static void createDefaultShaders() {
		shaderInstance.addResource(Material.SHADER_WIREFRAME, new CgGlslShader(
				"shader/vertex_shader_wireframe.glsl",
				"shader/fragment_shader_wireframe.glsl"));
		shaderInstance.addResource(Material.SHADER_TEXTURE_PHONG,
				new CgGlslShader(
						"shader/vertex_shader_texture_phong_shading.glsl",
						"shader/fragment_shader_texture_phong_shading.glsl"));
		shaderInstance.addResource(Material.SHADER_TEXTURE, new CgGlslShader(
				"shader/vertex_shader_texture.glsl",
				"shader/fragment_shader_texture.glsl"));
		shaderInstance
				.addResource(
						Material.SHADER_TEXTURE_PHONG_SPOTLIGHT,
						new CgGlslShader(
								"shader/vertex_shader_texture_phong_shading_spotlight.glsl",
								"shader/fragment_shader_texture_phong_shading_spotlight.glsl"));
		shaderInstance.addResource(Material.SHADER_PHONG_SHADING,
				new CgGlslShader("shader/vertex_shader_phong_shading.glsl",
						"shader/fragment_shader_phong_shading.glsl"));
		shaderInstance.addResource(Material.SHADER_GOURAUD_SHADING,
				new CgGlslShader("shader/vertex_shader_gouraud_shading.glsl",
						"shader/fragment_shader_gouraud_shading.glsl"));
		shaderInstance.addResource(Material.SHADER_ENVIRONMENT_MAPPING,
				new CgGlslShader(
						"shader/vertex_shader_environmentmapping.glsl",
						"shader/fragment_shader_environmentmapping.glsl"));
		shaderInstance.addResource(Material.SHADER_COLOR, new CgGlslShader(
				"shader/vertex_shader_color.glsl",
				"shader/fragment_shader_color.glsl"));
		shaderInstance.addResource(Material.SHADER_COMIC, new CgGlslShader(
				"shader/vertex_shader_comic.glsl",
				"shader/fragment_shader_comic.glsl"));
		shaderInstance.addResource(Material.SHADER_SILHOUETTE,
				new CgGlslShader("shader/vertex_shader_outline.glsl",
						"shader/fragment_shader_outline.glsl"));
	}

	/**
	 * Create default textures
	 */
	private static void createDefaultTextures() {
		textureInstance.addResource(Material.TEXTURE_DRAGON, new CgTexture(
				"textures/dragon.png"));
		textureInstance.addResource(Material.TEXTURE_WORLD, new CgTexture(
				"textures/world.jpg"));
		textureInstance.addResource(Material.TEXTURE_WALL, new CgTexture(
				"textures/wall.jpg"));

	}
}
