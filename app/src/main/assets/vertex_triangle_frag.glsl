precision mediump float;
uniform sampler2D uTexture;
varying vec2 vCoordTexture;
varying float vType;
const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);
void main() {

    if(vType==0.0){
        lowp vec4 textureColor = texture2D(uTexture, vCoordTexture);
        float luminance = dot(textureColor.rgb, W);
        gl_FragColor = vec4(vec3(luminance), textureColor.a);
        return;
    }

    gl_FragColor = texture2D(uTexture,vCoordTexture);

}
