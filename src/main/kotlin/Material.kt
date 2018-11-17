
data class Material(val color:Color, val ambient:Double, val diffuse:Double, val specular:Double, val shininess:Int,
                    val reflective:Double, val transparency: Double, val refractiveIndex:Double) {

    var pattern:Pattern? = null

    constructor(reflective:Double) : this(Color(1, 1, 1), 0.1, 0.9, 0.9, 200, reflective, 0.0, 1.0)

    constructor(color:Color, ambient:Double, diffuse:Double, specular:Double, shininess: Int) :
                this(color, ambient, diffuse, specular, shininess, 0.0, 0.0, 1.0)

    constructor() : this(Color(1, 1, 1), 0.1, 0.9, 0.9, 200, 0.0, 0.0, 1.0)

    fun lighting(light:PointLight, obj:Shape, position:Point, eye:Vector, normal:Vector) =
                                    lighting(light, obj, position, eye, normal, false)

    fun lighting(light:PointLight, obj:Shape, position:Point, eye:Vector, normal:Vector, inShadow:Boolean):Color {
        val c:Color = pattern?.patternAtShape(obj, position) ?: color
        val effectiveColor = c * light.intensity
        val lightV = (light.point - position).normalize()
        val effectiveAmbient = effectiveColor * ambient
        val lightDotNormal = lightV.dot(normal)

        if (lightDotNormal < 0.0 || inShadow) {
            return effectiveAmbient
        }
        val effectiveDiffuse = effectiveColor * diffuse * lightDotNormal
        val reflectV = (lightV * -1.0).reflect(normal)
        val reflectDotEye = reflectV.dot(eye)
        //val reflectDotEye = Math.pow(reflectV.dot(eye), shininess.toDouble())

        val effectiveSpecular = if (reflectDotEye <= 0) {
            Color(0, 0, 0)
        } else {
            val factor = Math.pow(reflectDotEye, shininess.toDouble())
            light.intensity * specular * factor
        }

        return effectiveAmbient + effectiveDiffuse + effectiveSpecular
    }
}
