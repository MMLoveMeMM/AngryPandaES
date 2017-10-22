precision mediump float;
uniform sampler2D uTexture;
varying vec2 vTextureCoord; // 用于传递给片元着色器的变量
void main() {
    gl_FragColor = texture2D(uTexture,vTextureCoord);
}
