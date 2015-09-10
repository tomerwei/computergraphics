varying vec4 color;

/**
 * Vertex shader: Phong lighting model, Gouraud shading, one light
 * source taken from GL. 
 */
void main()
{
   	// Eye position
    vec3 v = vec3(gl_ModelViewMatrix * gl_Vertex);
    // Surface normal
    vec3 N = normalize(gl_NormalMatrix * gl_Normal);
    // Transformed vertex
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    // Light position
    vec3 lightPosition = gl_NormalMatrix * gl_LightSource[0].position.xyz;
    
    // Helping vectors for lighting model
    vec3 L = normalize(lightPosition - v);
    vec3 E = normalize(-v);
    vec3 R = normalize(reflect(-L,N));

	// Phong lighting model
    vec4 ambient = gl_FrontMaterial.ambient;
    vec4 diffuse = clamp( gl_FrontMaterial.diffuse * abs(dot(N,L))  , 0.0, 1.0 ) ;
    vec4 spec = clamp ( gl_FrontMaterial.specular * pow(abs(dot(R,E)), gl_FrontMaterial.shininess) , 0.0, 1.0 );
   	color = ambient + diffuse + spec;
   	
   	// Compute position in 3-space.
   	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}