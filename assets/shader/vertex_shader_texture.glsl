varying vec3 N; // Normal vector
varying vec3 p; // Surface point
uniform vec3 camera_position; // Set in Java application
varying vec2 texture_coordinate; // Texture coordinate

/**
 * Vertex shader: textured surface, lighting: Phong shading with Phong model.
 */
void main()
{
    p  = vec3(gl_Vertex);
    N = gl_Normal;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    texture_coordinate = vec2(gl_MultiTexCoord0);
}