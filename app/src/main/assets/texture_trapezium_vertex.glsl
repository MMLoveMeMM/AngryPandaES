uniform mat4 uMVPMatrix;
attribute vec3 aPosition;
attribute vec2 aTextureCoords;
varying vec2 vTextureCoords;
void main() {

    gl_Position = uMVPMatrix * vec4(aPosition,1);
    vTextureCoords = aTextureCoords;

}
