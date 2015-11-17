varying vec3 N;
varying vec3 p;
varying vec4 reflectionAmbient;
varying vec4 reflectionDiffuse;
varying vec4 reflectionSpecular;
varying vec4 color;
uniform vec3 camera_position;
varying vec3 cam_pos;

/**
 * Vertex shader: Phong lighting model, Phong shading.
 */
void main(void)
{
    p  = vec3(gl_Vertex);
    N = gl_Normal;//normalize(gl_NormalMatrix * gl_Normal);
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    color = gl_Color;
    reflectionAmbient = gl_FrontMaterial.ambient;
    reflectionDiffuse = gl_FrontMaterial.diffuse;
    reflectionSpecular = gl_FrontMaterial.specular;
    cam_pos = camera_position;
}