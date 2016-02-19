/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.material;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Material information about all scene graph nodes below. Contains color and
 * (optionally shader).
 * 
 * @author Philipp Jenke
 *
 */
public class Material extends Observable {

  /**
   * Use face normals for rendering (flat shading)
   */
  public enum Normals {
    PER_FACET, PER_VERTEX
  };

  /**
   * Ids for the default shaders.
   */
  public static final String SHADER_TEXTURE = "SHADER_TEXTURE";
  public static final String SHADER_ENVIRONMENT_MAPPING = "SHADER_ENVIRONMENT_MAPPING";
  public static final String SHADER_PHONG_SHADING = "SHADER_PHONG_SHADING";
  public static final String SHADER_COMIC = "SHADER_COMIC";
  public static final String SHADER_SILHOUETTE = "SHADER_SILHOUETTE";
  public static final String SHADER_BLACK = "SHADER_BLACK";
  public static final String SHADER_COLOR = "SHADER_COLOR";
  public static final String SHADER_WIREFRAME = "SHADER_WIREFRAME";
  public static final String SHADER_GOURAUD_SHADING = "SHADER_GOURAUD_SHADING";

  public static final Vector PALETTE0_COLOR0 = VectorMatrixFactory.newVector(215 / 255.0, 129 / 255.0, 36 / 255.0);
  public static final Vector PALETTE0_COLOR1 = VectorMatrixFactory.newVector(226 / 255.0, 227 / 255.0, 163 / 255.0);
  public static final Vector PALETTE0_COLOR2 = VectorMatrixFactory.newVector(182 / 255.0, 176 / 255.0, 131 / 255.0);
  public static final Vector PALETTE0_COLOR3 = VectorMatrixFactory.newVector(55 / 255.0, 78 / 255.0, 75 / 255.0);
  public static final Vector PALETTE0_COLOR4 = VectorMatrixFactory.newVector(7 / 255.0, 19 / 255.0, 40 / 255.0);

  public static final Vector PALETTE1_COLOR0 = VectorMatrixFactory.newVector(68 / 255.0, 37 / 255.0, 42 / 255.0);
  public static final Vector PALETTE1_COLOR1 = VectorMatrixFactory.newVector(65 / 255.0, 71 / 255.0, 93 / 255.0);
  public static final Vector PALETTE1_COLOR2 = VectorMatrixFactory.newVector(88 / 255.0, 119 / 255.0, 122 / 255.0);
  public static final Vector PALETTE1_COLOR3 = VectorMatrixFactory.newVector(136 / 255.0, 171 / 255.0, 129 / 255.0);
  public static final Vector PALETTE1_COLOR4 = VectorMatrixFactory.newVector(236 / 255.0, 209 / 255.0, 142 / 255.0);

  public static final Vector PALETTE2_COLOR0 = VectorMatrixFactory.newVector(73 / 255.0, 10 / 255.0, 61 / 255.0);
  public static final Vector PALETTE2_COLOR1 = VectorMatrixFactory.newVector(189 / 255.0, 21 / 255.0, 80 / 255.0);
  public static final Vector PALETTE2_COLOR2 = VectorMatrixFactory.newVector(233 / 255.0, 127 / 255.0, 2 / 255.0);
  public static final Vector PALETTE2_COLOR3 = VectorMatrixFactory.newVector(248 / 255.0, 202 / 255.0, 0 / 255.0);
  public static final Vector PALETTE2_COLOR4 = VectorMatrixFactory.newVector(138 / 255.0, 155 / 255.0, 15 / 255.0);

  public static final Vector COLOR_BLACK = VectorMatrixFactory.newVector(0, 0, 0);

  /**
   * IDs for the default textures
   */
  public static String TEXTURE_DRAGON = "TEXTURE_DRAGON";
  public static String TEXTURE_WORLD = "TEXTURE_WORLD";
  public static String TEXTURE_WALL = "TEXTURE_WALL";

  /**
   * Id of the shader. Use null to indicate no shader use.
   */
  private List<String> shaderIds = new ArrayList<String>();

  /**
   * Currently set render mode.
   */
  private Normals renderMode;

  /**
   * Renders the mesh in a sophisticated way - attention: expensive!
   */
  private boolean showSophisticatesMesh;

  /**
   * Id of the texture. Use null to indicate no shader use.
   */
  private String textureId;

  /**
   * Diffuse reflection
   */
  private Vector reflectionDiffuse = null;

  /**
   * Ambient reflection
   */
  private Vector reflectionAmbient = null;

  /**
   * Specular reflection
   */
  private Vector reflectionSpecular = null;

  /**
   * Shininess of the material (used in phong lighting model)
   */
  private float specularShininess;

  /**
   * Reflection factor of the material. Must be in [0,1]. reflection +
   * refraction must not exceed 1.
   */
  private double reflection = 0;

  /**
   * Reflection factor of the material. Must be in [0,1]. reflection +
   * refraction must not exceed 1.
   */
  private double refraction = 0;

  /**
   * Defines if the object throws a shadow
   */
  private boolean throwsShadow = false;

  /**
   * Transparency of the object, 0 = completely transparent, 1 = completely
   * opaque.
   */
  private double transparency = 1;

  /**
   * Constructor
   */
  public Material() {
    renderMode = Normals.PER_VERTEX;
    reflectionAmbient = VectorMatrixFactory.newVector(0.1, 0.1, 0.1);
    reflectionDiffuse = VectorMatrixFactory.newVector(PALETTE1_COLOR4);
    reflectionSpecular = VectorMatrixFactory.newVector(1, 1, 1);
    showSophisticatesMesh = false;
    textureId = null;
    specularShininess = 20;
  }

  /**
   * Getter.
   */
  public Normals getRenderMode() {
    return renderMode;
  }

  /**
   * Setter.
   */
  public void setRenderMode(Normals renderMode) {
    this.renderMode = renderMode;

    setChanged();
    notifyObservers();
  }

  /**
   * Return true, if the material has a vertex AND fragment shader.
   */
  public boolean hasShader() {
    return shaderIds.size() > 0;
  }

  /**
   * Getter.
   */
  public String getShaderId(int index) {
    return shaderIds.get(index);
  }

  /**
   * Setter
   */
  public void setShaderId(String shaderId) {
    shaderIds.clear();
    addShaderId(shaderId);
  }

  /**
   * Setter
   */
  public void addShaderId(String shaderId) {
    shaderIds.add(shaderId);
  }

  /**
   * Return true, if the material has a texture
   */
  public boolean hasTexture() {
    return textureId != null;
  }

  /**
   * Getter.
   */
  public String getTextureId() {
    return textureId;
  }

  /**
   * Setter
   */
  public void setTextureId(String textureId) {
    this.textureId = textureId;
  }

  /**
   * Getter.
   */
  public boolean isShowSophisticatesMesh() {
    return showSophisticatesMesh;
  }

  /**
   * Setter
   */
  public void setShowSophisticatesMesh(boolean b) {
    showSophisticatesMesh = b;

    setChanged();
    notifyObservers();
  }

  /**
   * (Deep) copy content.
   */
  public void copyFrom(Material other) {
    renderMode = other.renderMode;
    shaderIds.clear();
    for (String shaderId : other.shaderIds) {
      addShaderId(shaderId);
    }
    textureId = other.textureId;
    showSophisticatesMesh = other.showSophisticatesMesh;
  }

  /**
   * Setter: no texture
   */
  public void setNoTexture() {
    textureId = null;
  }

  /**
   * Getter.
   */
  public int getNumberOfShaders() {
    return shaderIds.size();
  }

  /**
   * Getter. Deprecated: Use getReflectionDiffuse instead
   */
  @Deprecated
  public Vector getColor() {
    return reflectionDiffuse;
  }

  /**
   * Setter. Deprecated: Use getReflectionDiffuse instead
   */
  @Deprecated
  public void setColor(Vector color) {
    this.reflectionDiffuse = color;
  }

  /**
   * Setter.
   */
  public void setReflectionDiffuse(Vector color) {
    this.reflectionDiffuse.copy(color);
  }

  /**
   * Getter.
   */
  public Vector getReflectionDiffuse() {
    return reflectionDiffuse;
  }

  /**
   * Setter.
   */
  public void setReflectionAmbient(Vector color) {
    this.reflectionAmbient.copy(color);
  }

  /**
   * Getter.
   */
  public Vector getReflectionAmbient() {
    return reflectionAmbient;
  }

  /**
   * Setter.
   */
  public void setReflectionSpecular(Vector color) {
    this.reflectionSpecular.copy(color);
  }

  /**
   * Getter.
   */
  public Vector getReflectionSpecular() {
    return reflectionSpecular;
  }

  /**
   * Getter.
   */
  public float getSpecularShininess() {
    return specularShininess;
  }

  /**
   * Setter.
   */
  public void setSpecularShininess(float specularShininess) {
    this.specularShininess = specularShininess;

    setChanged();
    notifyObservers();
  }

  /**
   * Getter
   */
  public double getReflection() {
    return reflection;
  }

  /**
   * Getter.
   */
  public double getRefraction() {
    return refraction;
  }

  /**
   * Setter.
   */
  public void setReflection(double newValue) {
    reflection = newValue;
  }

  /**
   * Setter.
   */
  public void setRefraction(double newValue) {
    refraction = newValue;
  }

  /**
   * Getter
   */
  public boolean isThrowingShadow() {
    return throwsShadow;
  }

  /**
   * Setter
   */
  public void setThrowsShadow(boolean throwsShadow) {
    this.throwsShadow = throwsShadow;
  }

  public void setTransparency(double transparency) {
    this.transparency = transparency;
  }

  public double getTransparency() {
    return transparency;
  }
}
