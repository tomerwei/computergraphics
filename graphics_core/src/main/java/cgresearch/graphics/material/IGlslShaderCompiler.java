package cgresearch.graphics.material;

public interface IGlslShaderCompiler {

	/**
	 * Compile a shader
	 */
	public void compile(int shaderType, String source);
}
