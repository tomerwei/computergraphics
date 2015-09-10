// These vectors need to be provided by the vertex shader
// Normal direction
varying vec3 N;
// Viewer direction
varying vec3 v;

varying vec2 texture_coordinate;
uniform sampler2D my_color_texture;
varying vec4 spotLightPos;
varying vec3 spotLightDir;

float PI = 3.14159265358979323846264;

/**
 * Fragment shader used fror phong shading. The lighting color 
 * value is computed for each pixel here.
 */
void main (void)
{
   vec3 light2Pixel = normalize(v - spotLightPos.xyz);

    // Define material
    vec4 ambientMat = vec4(0, 0, 0, 1);
    vec4 diffuseMat = texture2D(my_color_texture, texture_coordinate);
    vec4 specMat = vec4(0.3,0.3,0.3,1); 
    float specPow = 1.0;
        
    // Phong lighting model 
    vec3 L = normalize(gl_LightSource[0].position.xyz - v);
    vec3 E = normalize(-v);
    vec3 R = normalize(reflect(-L,N));
    vec4 ambient = ambientMat;
    vec4 diffuse = clamp( diffuseMat * abs(dot(N,L)), 0.0, 1.0 ) ;
    vec4 spec = clamp ( specMat * pow(abs(dot(R,E)),0.3*specPow), 0.0, 1.0 );
    

    float spotLightInner = 15.0;
    float spotLightOuter = 20.0;
    float spotLightAmbientFactor = 0.2;
    
    float angle = acos(dot(light2Pixel,spotLightDir.xyz)) * 180.0 / PI;
    float scale = 1.0;
    if ( angle < spotLightInner ){  
        scale = 1.0;
    } else if ( angle < spotLightOuter ){
        scale = (1.0-(angle-spotLightInner)/(spotLightOuter-spotLightInner))*(1.0-spotLightAmbientFactor) + spotLightAmbientFactor;
    } else {
        scale = spotLightAmbientFactor;
    }
    
    // Result
    gl_FragColor = (ambient + diffuse + spec) * scale;
}
