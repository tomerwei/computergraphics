package cgresearch.rendering.jogl.material;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.material.CgGlslShader;
import cgresearch.rendering.jogl.core.JoglHelper;

public class JoglShader {

  /**
   * Shader constants.
   */
  private static final int COMPILE_STATUS_OK = 1;

  /**
   * Convert to GL shader constants.
   */
  public static int getGlShaderType(CgGlslShader.ShaderType type) {
    if (type == CgGlslShader.ShaderType.VERTEX) {
      return GL2.GL_VERTEX_SHADER;
    } else if (type == CgGlslShader.ShaderType.FRAGMENT) {
      return GL2.GL_FRAGMENT_SHADER;
    } else {
      return -1;
    }
  }

  /**
   * Compile and link the shaders.
   */
  private static void compileAndLink(CgGlslShader shader, GL2 gl) {

    // We set this flag to true even if the shader is not working. Otherwise
    // the tries again over and over.
    shader.setCompiled(true);

    JoglHelper.hasGLError(gl, "a");

    // Compile
    int v = compileShader(gl, getGlShaderType(CgGlslShader.ShaderType.VERTEX), shader.getVertexShaderFilename());
    int f = compileShader(gl, getGlShaderType(CgGlslShader.ShaderType.FRAGMENT), shader.getFragmentShaderFilename());
    if (v < 0 || f < 0) {
      Logger.getInstance().error("Shader not created.");
      return;
    }

    // Link
    int shaderProgram = linkProgram(gl, v, f);
    if (shaderProgram < 0) {
      Logger.getInstance().error("Shader not created.");
      return;
    }

    if (JoglHelper.hasGLError(gl, "Shader link")) {
      Logger.getInstance().error("Shader link error.");
    }

    shader.setShaderProgram(shaderProgram);
    Logger.getInstance().debug("Successfully created shader with\n  " + shader.getVertexShaderFilename() + "\n  "
        + shader.getFragmentShaderFilename());
  }

  /**
   * Link the vertex and fragment shaders.
   */
  private static int linkProgram(GL2 gl, int vertexShaderId, int fragmentShaderId) {
    int shaderProgram = gl.glCreateProgram();
    gl.glAttachShader(shaderProgram, vertexShaderId);
    gl.glAttachShader(shaderProgram, fragmentShaderId);
    gl.glLinkProgram(shaderProgram);
    gl.glValidateProgram(shaderProgram);
    IntBuffer intBuffer = IntBuffer.allocate(1);
    gl.glGetProgramiv(shaderProgram, GL2.GL_LINK_STATUS, intBuffer);

    String errorMsg = getLinkErrorMessage(shaderProgram, gl);
    if (errorMsg != null) {
      Logger.getInstance().error("Link error " + ": \n" + errorMsg);
    }

    return shaderProgram;
  }

  /**
   * Compile the specified shader from the filename and return the OpenGL id.
   */
  public static int compileShader(GL2 gl, int shaderType, String shaderFilename) {
    String vsrc = CgGlslShader.readShaderSource(shaderFilename);
    int id = compileShaderFromSource(gl, shaderType, vsrc);

    String errorMsg = getCompileErrorMessage(id, gl);
    if (errorMsg != null) {
      Logger.getInstance().error("Compile error " + ": \n" + errorMsg);
    }

    if (id < 0) {
      Logger.getInstance().error("Compile error in shader file " + shaderFilename);
    }
    return id;
  }

  /**
   * Compile the specified shader from the filename and return the OpenGL id.
   */
  public static int compileShaderFromSource(GL2 gl, int shaderType, String shaderSource) {
    int id = gl.glCreateShader(shaderType);
    gl.glShaderSource(id, 1, new String[] { shaderSource }, (int[]) null, 0);
    gl.glCompileShader(id);
    if (checkCompileError(id, gl)) {
      String errorMsg = getCompileErrorMessage(id, gl);
      Logger.getInstance().error(errorMsg);
      return -1;
    }
    return id;
  }

  /**
   * Delete the shader with the given id.
   */
  public static void deleteShader(GL2 gl, int id) {
    if (id < 0) {
      Logger.getInstance().error("deleteShader: invalid id");
      return;
    }
    gl.glDeleteShader(id);
  }

  /**
   * Return the link error message, return null if no error occurred.
   */
  private static String getLinkErrorMessage(int programId, GL2 gl) {
    IntBuffer intBuffer = IntBuffer.allocate(1);
    gl.glGetProgramiv(programId, GL2.GL_INFO_LOG_LENGTH, intBuffer);
    int size = intBuffer.get(0);
    if (size > 0) {
      ByteBuffer byteBuffer = ByteBuffer.allocate(size);
      gl.glGetProgramInfoLog(programId, size, intBuffer, byteBuffer);
      String errorMsg = "";
      for (byte b : byteBuffer.array()) {
        errorMsg += b;
      }
      return errorMsg;
    }
    return null;
  }

  /**
   * Extract the compile error message. Return null if no error occurred.
   */
  private static String getCompileErrorMessage(int id, GL2 gl) {
    IntBuffer intBuffer = IntBuffer.allocate(1);
    gl.glGetShaderiv(id, GL2.GL_INFO_LOG_LENGTH, intBuffer);
    int size = intBuffer.get(0);
    if (size > 0) {
      ByteBuffer byteBuffer = ByteBuffer.allocate(size);
      gl.glGetShaderInfoLog(id, size, intBuffer, byteBuffer);
      String errorMessage = "";
      for (byte b : byteBuffer.array()) {
        errorMessage += (char) b;
      }
      return errorMessage;
    }
    return null;
  }

  /**
   * Check if a compile error (vertex or fragment shader) occurred?
   */
  private static boolean checkCompileError(int id, GL2 gl) {
    IntBuffer intBuffer = IntBuffer.allocate(1);
    gl.glGetShaderiv(id, GL2.GL_COMPILE_STATUS, intBuffer);
    return intBuffer.get(0) != COMPILE_STATUS_OK;
  }

  /**
   * Activate the shader
   */
  public static void use(CgGlslShader shader, GL2 gl) {
    if (!shader.isCompiled()) {
      compileAndLink(shader, gl);
    }
    gl.glUseProgram(shader.getShaderProgram());
  }

  /**
   * Use no shader - fixed function pipeline instead.
   */
  public static void disableShader(GL2 gl) {
    gl.glUseProgram(0);
  }
}
