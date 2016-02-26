// These vectors need to be provided by the vertex shader
// Normal direction
varying vec3 N;
// Viewer direction
varying vec3 v;
varying vec3 lightPosition;

/**
 * Fragment shader: Comic-style rendering. Phong model is used to compute a brightness value which is 
 * scaled by the color and discretized to a fixed number of steps. 
 */
void main (void)
{
    // Helping vectors to compute phong lighting model
    vec3 L = normalize(lightPosition - v);
    vec3 E = normalize(-v);
    vec3 R = normalize(reflect(-L,N));
    
    // Number of different brightness steps
    float numberOfSteps = 3.0;
    
    // Black is not allowed
    float brightnessDiffuse = clamp( abs(dot(N,L)) , 0.0, 1.0 );
    float brightnessSpecular = clamp( pow(abs(dot(R,E)), gl_FrontMaterial.shininess*5.0), 0.0, 1.0 );
    float brightness = brightnessDiffuse;// + brightnessSpecular;
    float delta = 1.0 / (numberOfSteps + 1.0);
    brightness = brightness * delta * numberOfSteps + delta;
    
    if ( brightness > 0.99 ){
    	// Special case: white for very bright spots
    	gl_FragColor = vec4(1,1,1,1);
    } else { 
    	// Use brightness to scale diffuse color to closest step
    	int intBrightness = int(floor(brightness * numberOfSteps+0.5));
    	brightness = float(intBrightness) / numberOfSteps;  
    	gl_FragColor = gl_FrontMaterial.diffuse * brightness;
    }
}
