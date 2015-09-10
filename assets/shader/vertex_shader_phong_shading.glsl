varying vec3 N;
varying vec3 v;
varying vec4 reflectionAmbient;
varying vec4 reflectionDiffuse;
varying vec4 reflectionSpecular;
varying vec4 color;

/**
 * Vertex shader: Phong lighting model, Phong shading.
 */
void main(void)
{
    // Eye position
    v = vec3(gl_ModelViewMatrix * gl_Vertex);
    // Surface normal
    N = normalize(gl_NormalMatrix * gl_Normal);
    // Transformed vertex
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    // Vertex color
    color = gl_Color;
    reflectionAmbient = gl_FrontMaterial.ambient;
    reflectionDiffuse = gl_FrontMaterial.diffuse;
    reflectionSpecular = gl_FrontMaterial.specular;
}
