precision mediump float;
uniform sampler2D uTextures;
varying vec2 vTextureCoords;
void main() {

    gl_FragColor = texture2D(uTextures,vTextureCoords);

}
