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
    float offsetSize = 0.002;
    vec4 offsetVector = vec4(0,0,0,0);
    offsetVector.xyz = gl_Normal * offsetSize;
    gl_Position = gl_ModelViewProjectionMatrix * (gl_Vertex + offsetVector) + vec4(0,0,offsetSize*2.0,0);
}
