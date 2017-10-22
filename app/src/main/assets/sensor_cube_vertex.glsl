uniform mat4 uMVPMatrix;
attribute vec3 aPosition;
attribute vec4 aColors;
varying vec4 vColors;

void main() {

    gl_Position = uMVPMatrix*vec4(aPosition,1);
    vColors=aColors;

}
