varying vec4 color;

/**
 * Fragment shader: Phong lighting model, Gouraud shading, one light
 * source taken from GL. 
 */
void main()
{
	// No operation required, just passing the color value from the vertex shader.
  	gl_FragColor = color;
}