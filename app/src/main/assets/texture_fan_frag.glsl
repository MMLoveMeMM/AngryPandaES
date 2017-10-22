precision mediump float;
uniform sampler2D uTexture;
varying vec2 vCoordTexture;

void main() {
    gl_FragColor = texture2D(uTexture,vCoordTexture);
}
