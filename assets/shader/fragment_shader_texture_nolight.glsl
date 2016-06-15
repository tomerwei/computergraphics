varying vec3 N; // Normal vector
varying vec3 p; // Surface point
varying vec2 texture_coordinate; // Texture coordinate
uniform sampler2D my_color_texture; // Texture object
uniform float transparency; 

/**
 * Fragment shader: textured surface, no lighting
 */
void main (void)
{
  	vec4 textureColor = texture2D(my_color_texture, texture_coordinate);
   	gl_FragColor = textureColor;
}