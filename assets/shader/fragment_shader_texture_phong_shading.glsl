// These vectors need to be provided by the vertex shader
// Normal direction
varying vec3 N;
// Viewer direction
varying vec3 v;
varying vec2 texture_coordinate;
uniform sampler2D my_color_texture;
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
    
    vec4 texColor = texture2D(my_color_texture, texture_coordinate);
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
    		if ( dot(N,L) > 0.0 ){
    			vec4 diffuse;
                diffuse.x = texColor.x * gl_LightSource[i].diffuse.x * 2.0;
    			diffuse.y = texColor.y * gl_LightSource[i].diffuse.y * 2.0;
    			diffuse.z = texColor.z * gl_LightSource[i].diffuse.z * 2.0;
    			diffuse.w = 0.0;
    			diffuse = diffuse * dot(N,L) / float(numberOfLights);
    			gl_FragColor += diffuse;
    		}
    	
    		// Specular
    		if ( dot(R,E) > 0.0 ){
    			vec4 specular;
    			specular.x = texColor.x * gl_LightSource[i].specular.x;
    			specular.y = texColor.y * gl_LightSource[i].specular.y;
    			specular.z = texColor.z * gl_LightSource[i].specular.z;
    			specular.w = 0.0;
    			specular = specular * pow(dot(R,E), gl_FrontMaterial.shininess) / float(numberOfLights);
    			gl_FragColor += specular;
    		}
    	}
    }
    
    gl_FragColor = clamp( gl_FragColor, 0.0, 1.0 );
}
