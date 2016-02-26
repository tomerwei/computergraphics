varying vec3 vBC;

float edgeFactor(){
    vec3 d = fwidth(vBC);
    vec3 a3 = smoothstep(vec3(0.0), d*1.5, vBC);
    return min(min(a3.x, a3.y), a3.z);
}

/**
 * Fragment shader: Use the color attribute. 
 */
void main (void)
{
	//gl_FragColor.rgb = mix(vec3(0.0), vec3(1), edgeFactor());
	gl_FragColor.rgb = vec3(0,0,0);
}
