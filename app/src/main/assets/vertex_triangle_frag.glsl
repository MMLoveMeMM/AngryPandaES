precision mediump float;
uniform sampler2D uTexture;
varying vec2 vCoordTexture;
varying float vType;
//const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);

const float uD = 80.0; //旋转角度
const float uR = 0.5; //旋转半径

const vec2 TexSize = vec2(400.0, 400.0);
const vec2 mosaicSize = vec2(8.0, 8.0);

const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);


void main() {
// 0 : 灰化图片
// 1 : 颠倒图片
// 2 : 图像漩涡
// 3 : 马赛克
    if(vType==0.0){
        lowp vec4 textureColor = texture2D(uTexture, vCoordTexture);
        float luminance = dot(textureColor.rgb, W);
        gl_FragColor = vec4(vec3(luminance), textureColor.a);
        return;
    }else if(vType==1.0){
        vec4 color = texture2D(uTexture, vec2(vCoordTexture.x, 1.0 - vCoordTexture.y));
        gl_FragColor = color;
        return ;
    }else if(vType==2.0){
        ivec2 ires = ivec2(512, 512);
        float Res = float(ires.s);

        vec2 st = vCoordTexture;
        float Radius = Res * uR;
        vec2 xy = Res * st;

        vec2 dxy = xy - vec2(Res/2., Res/2.);
        float r = length(dxy);
        float beta = atan(dxy.y, dxy.x) + radians(uD) * 2.0 * (-(r/Radius)*(r/Radius) + 1.0);//(1.0 - r/Radius);

        vec2 xy1 = xy;
        if(r<=Radius)
        {
            xy1 = Res/2. + r*vec2(cos(beta), sin(beta));
        }

        st = xy1/Res;

        vec3 irgb = texture2D(uTexture, st).rgb;

        gl_FragColor = vec4( irgb, 1.0 );

        return ;
    }else if(vType==3.0){
        vec2 intXY = vec2(vCoordTexture.x*TexSize.x, vCoordTexture.y*TexSize.y);
        vec2 XYMosaic = vec2(floor(intXY.x/mosaicSize.x)*mosaicSize.x, floor(intXY.y/mosaicSize.y)*mosaicSize.y);
        vec2 UVMosaic = vec2(XYMosaic.x/TexSize.x, XYMosaic.y/TexSize.y);
        vec4 color = texture2D(uTexture, UVMosaic);
        gl_FragColor = color;
        return ;
    }else if(vType==4.0){
        const vec2 TexSize = vec2(100.0, 100.0);
        const vec4 bkColor = vec4(0.5, 0.5, 0.5, 1.0);
        vec2 tex = vCoordTexture;
        vec2 upLeftUV = vec2(tex.x-1.0/TexSize.x, tex.y-1.0/TexSize.y);
        vec4 curColor = texture2D(uTexture, vCoordTexture);
        vec4 upLeftColor = texture2D(uTexture, upLeftUV);
        vec4 delColor = curColor - upLeftColor;
        float luminance = dot(delColor.rgb, W);
        gl_FragColor = vec4(vec3(luminance), 0.0) + bkColor;
        return ;
    }

    gl_FragColor = texture2D(uTexture,vCoordTexture);

}
