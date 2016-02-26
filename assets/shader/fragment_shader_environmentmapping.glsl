uniform sampler2D environmentmap;
varying vec3 normal;
varying vec3 E;

/**
 * Fragment shader used for texture mapping.
 */
void main()
{
	// Compute the required vectors
    vec3 R = normalize(E - normal * 2.0 * dot(E, normal)); 
    float PI = 3.1415;
    
    vec2 environmentMapCoords;
    environmentMapCoords.x = ( PI + atan( R.y, R.x ) ) / (2.0 * PI);
    environmentMapCoords.y = atan( sqrt( R.x * R.x + R.y * R.y ), R.z) / PI;
    
    // Sampling the texture and passing it to the frame buffer
    gl_FragColor = texture2D(environmentmap, environmentMapCoords);
}