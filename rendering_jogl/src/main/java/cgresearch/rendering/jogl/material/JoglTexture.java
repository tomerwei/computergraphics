package cgresearch.rendering.jogl.material;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.graphics.material.CgTexture;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

/**
 * Representation of a Jogl texture.
 *
 */
public class JoglTexture {

  /**
   * Jogl texture object, mapped to the CgTextures
   */
  private static Map<CgTexture, Texture> joglTextures = new HashMap<CgTexture, Texture>();

  private static boolean doMipMap = true;

  /**
   * Enable the texture and load it (if reauired).
   */
  public static void use(CgTexture texture, GL2 gl) {
    if (texture == null) {
      Logger.getInstance().error("Invalid texure in JoglTexture.use().");
      return;
    }
    if (!texture.isLoaded()) {
      // Free existing texture
      Texture joglTexture = joglTextures.get(texture);
      if (joglTexture != null) {
        joglTexture.destroy(gl);
        Logger.getInstance().message("Successfully destroided texture.");
      }

      if (texture.getTextureImage() != null) {
        joglTextures.put(texture, createTexture(gl.getGLProfile(), texture.getTextureImage()));
      } else if (texture.getTextureFilename() != null) {
        joglTextures.put(texture, createTexture(texture.getTextureFilename()));
      }
      texture.setIsLoaded(true);
    }

    if (joglTextures.get(texture) != null) {
      gl.glEnable(GL2.GL_TEXTURE_2D);
      gl.glBindTexture(GL2.GL_TEXTURE_2D, joglTextures.get(texture).getTextureObject(gl));
      gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
      gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
      gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
      gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);

      if (doMipMap) {
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
      }
    }

  }

  /**
   * Create the texture from image.
   */
  public static Texture createTexture(String textureFilename) {
    Texture texture = null;

    String absoluteTextureFilename = ResourcesLocator.getInstance().getPathToResource(textureFilename);
    if (absoluteTextureFilename == null) {
      Logger.getInstance().error("Failed to read texture file " + textureFilename);
      return null;
    }
    try {
      texture = TextureIO.newTexture(new File(absoluteTextureFilename), doMipMap);
    } catch (GLException e) {
      Logger.getInstance().exception("Failed to assign texture file " + absoluteTextureFilename, e);
    } catch (IOException e) {
      Logger.getInstance().exception("Failed to assign texture file " + absoluteTextureFilename, e);
    }
    Logger.getInstance().debug("Successfully loaded texture " + absoluteTextureFilename);
    return texture;
  }

  /**
   * Create the texture from image.
   */
  public static Texture createTexture(GLProfile gp, BufferedImage textureImage) {
    Texture texture = null;

    if (textureImage == null) {
      Logger.getInstance().error("Texture image is null");
      return null;
    }
    try {
      texture = AWTTextureIO.newTexture(gp, textureImage, doMipMap);
    } catch (GLException e) {
      Logger.getInstance().exception("Failed to assign texture image", e);
    }
    Logger.getInstance().debug("Successfully loaded texture image");
    return texture;
  }
}
