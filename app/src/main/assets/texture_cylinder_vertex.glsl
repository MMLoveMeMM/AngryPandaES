uniform mat4 uMVPMatrix;
attribute vec3 aPosition;
attribute vec2 aTextCoords;
varying vec2 vTextCoords;

void main() {
    gl_Position = uMVPMatrix*vec4(aPosition,1);
    vTextCoords=aTextCoords;
}
