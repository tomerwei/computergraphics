package cgresearch.ui.resources;

public interface IGlslShaderCompiler {

	/**
	 * Compile a shader
	 */
	public void compile(int shaderType, String source);
}
