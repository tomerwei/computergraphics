varying vec3 normal;
varying vec3 E;

/**
 * Vertex shader used for texturing. No color computations
 * are required here, the information is passed to the
 * corresponding fragment shader.
 */
void main()
{
    // Compute the viewing direction.
    E = vec3(gl_ModelViewMatrix * gl_Vertex);
    // Compute the normal direction.
    normal = normalize(gl_NormalMatrix * gl_Normal);
    
    // Position in 3-space
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}