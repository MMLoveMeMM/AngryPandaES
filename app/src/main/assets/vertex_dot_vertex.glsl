uniform mat4 uMVPMatrix;
uniform float aDelta;
uniform float aAngle;
attribute vec3 aPosition;
attribute vec4 aColor;
varying vec4 vColor;
#define PI 3.14159
float xAngle=0.0;
float gAngle=0.0;
void main() {

    xAngle=18.0;
    gAngle=float(PI*xAngle/180.0);

    float x=aPosition.x+aPosition.x*cos(aAngle);
    float y=aPosition.y+aPosition.y*sin(aAngle);

    gl_Position=uMVPMatrix*vec4(x,y,aPosition.z+aDelta,1);
    gl_PointSize=20.0;
    vColor=aColor;

}
