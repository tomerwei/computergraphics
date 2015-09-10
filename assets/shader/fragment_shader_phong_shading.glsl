// Normal direction
varying vec3 N;
// Viewer direction
varying vec3 v;
// Reflection properties
varying vec4 reflectionAmbient;
varying vec4 reflectionDiffuse;
varying vec4 reflectionSpecular;

/**
 * Fragment shader used for phong shading. The lighting color 
 * value is computed for each pixel here.
 */
void main (void)
{
    // Determine number of active lights
    int numberOfLights = 0;
    for ( int i = 0; i < gl_MaxLights; i++ ){
    	if ( gl_LightSource[i].diffuse.x > -0.1 ){
    		numberOfLights++;
    	}
    }
    
    gl_FragColor = vec4(0,0,0,1);
   
   	// Ambient color
   	vec4 ambient;
    ambient.x = reflectionAmbient.x;
    ambient.y = reflectionAmbient.y;
    ambient.z = reflectionAmbient.z;
    ambient.w = 0.0;
    gl_FragColor += ambient;
   
    // Add diffuse and specular for each light
    for ( int i = 0; i < numberOfLights; i++ ){
    	if ( gl_LightSource[i].diffuse.x > -0.1 ){
    		// Phong lighting model 
    		vec3 L;
    		if ( abs(gl_LightSource[i].position.w) < 0.00001 ){
    			// Directional light
    			L =  normalize( gl_LightSource[i].position.xyz );
    		} else {
    			// Point light
    			vec3 lightPos = gl_NormalMatrix * gl_LightSource[i].position.xyz;
    			L = normalize(lightPos - v);
    		}
    		vec3 E = normalize(-v);
    		vec3 R = normalize(reflect(-L,N));
    	
    		// Diffuse
    		vec4 diffuse;
    		diffuse.x = reflectionDiffuse.x * gl_LightSource[i].diffuse.x;
    		diffuse.y = reflectionDiffuse.y * gl_LightSource[i].diffuse.y;
    		diffuse.z = reflectionDiffuse.z * gl_LightSource[i].diffuse.z;
    		diffuse.w = 0.0;
    		diffuse = diffuse * abs(dot(N,L)) / float(numberOfLights);
    		gl_FragColor += diffuse;
    	
    		// Specular
    		vec4 specular;
    		specular.x = reflectionSpecular.x * gl_LightSource[i].specular.x;
    		specular.y = reflectionSpecular.y * gl_LightSource[i].specular.y;
    		specular.z = reflectionSpecular.z * gl_LightSource[i].specular.z;
    		specular.w = 0.0;
    		specular = specular * pow(abs(dot(R,E)), gl_FrontMaterial.shininess) / float(numberOfLights);
    		gl_FragColor += specular;
    	}
    }
    
    gl_FragColor = clamp( gl_FragColor, 0.0, 1.0 );
}
