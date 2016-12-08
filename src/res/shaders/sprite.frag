// texture fragment shader
#version 330 core

in vec2 texCoord;

out vec4 FragColor;

uniform sampler2D texture1;
uniform vec4 colorMult = vec4(1.0);
uniform vec3 colorAdd = vec3(0.0);

void main()
{
	FragColor = texture(texture1, texCoord);
	FragColor.xyz += colorAdd;
	FragColor *= colorMult;

	if (FragColor.a == 0.0)
		discard;
}
