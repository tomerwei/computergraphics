varying vec3 vBC;
attribute vec3 barycentric;
uniform mat4 proj, view;
void main(){
    vBC = barycentric;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    float offset = - 0.001;
    gl_Position.z = gl_Position.z + offset;
}
