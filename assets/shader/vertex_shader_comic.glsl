varying vec3 N;
varying vec3 v;
varying vec3 lightPosition;

/**
 * Vertex shader: Comic-style rendering. Phong model is used to compute a brightness value which is 
 * scaled by the color and discretized to a fixed number of steps. 
 */
void main(void)
{
    // Eye position
    v = vec3(gl_ModelViewMatrix * gl_Vertex);
    // Surface normal
    N = normalize(gl_NormalMatrix * gl_Normal);
    // Transformed vertex
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    // Light position
    lightPosition = gl_NormalMatrix * gl_LightSource[0].position.xyz;
}
