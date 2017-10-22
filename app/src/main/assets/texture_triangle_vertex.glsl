uniform mat4 uMVPMatrix;
attribute vec3 aPosition;
attribute vec2 aColorCoord; // 定点文理坐标
varying vec2 vTextureCoord; // 用于传递给片元着色器的变量
void main() {
    gl_Position = uMVPMatrix * vec4(aPosition,1);
    vTextureCoord = aColorCoord;
}
