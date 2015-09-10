varying vec3 N;
varying vec3 v;
varying vec2 texture_coordinate;
varying vec4 reflectionAmbient;
varying vec4 reflectionDiffuse;
varying vec4 reflectionSpecular;

/**
 * Vertex shader used for Phong shading. No color computations are 
 * required here, information is passed to the fragment shader.
 */
void main(void)
{
    // Compute the viewing direction.
    v = vec3(gl_ModelViewMatrix * gl_Vertex);
    // Compute the normal direction.
    N = normalize(gl_NormalMatrix * gl_Normal);
    // Compute the position in 3-space.
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    /// Texture coordinates
    texture_coordinate = vec2(gl_MultiTexCoord0);
    // Reflection properties
    reflectionAmbient = gl_FrontMaterial.ambient;
    reflectionDiffuse = gl_FrontMaterial.diffuse;
    reflectionSpecular = gl_FrontMaterial.specular;
}
