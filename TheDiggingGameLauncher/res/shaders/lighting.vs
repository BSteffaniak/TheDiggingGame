varying vec4	fragScreenPos;
varying vec4	fragWorldPos;
varying vec4	fragTestPos;
varying vec3	fragNormal;

attribute vec2	vPosition;

void main()
{
	fragScreenPos  = gl_ModelViewMatrix * gl_Vertex;
	fragWorldPos   = gl_Vertex;
	
	gl_TexCoord[0] = gl_MultiTexCoord0;
	
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}