uniform sampler2D	texture;

varying vec4		fragWorldPos;
varying vec4		fragScreenPos;

uniform vec4 lights[12 * 3];

void main()
{
	gl_FragColor = texture2D(texture, gl_TexCoord[0].st);
	
	vec4 finalColor = gl_FragColor;
	
	float tileX = fragWorldPos.x / (16 * 16);
	
	float remainder = int(tileX) - tileX;
	
	float xOff = int(tileX) % 16;//int(fragWorldPos.x) % 1;
	
	float test = fragScreenPos.x / 16;
	
	float r = int(test);
	
	finalColor *= vec4(1);
	
	gl_FragColor = finalColor;
}