import kotlin.math.roundToInt

const val epsilon = 0.00001

private fun closeEnough(value:Double, other:Double) =
        Math.abs(value - other) < epsilon

open class Tuple(val x:Double, val y:Double, val z:Double, val w:Double) {

    operator fun plus(other:Tuple):Tuple =
            Tuple(x + other.x, y + other.y, z + other.z, w + other.w)

    operator fun minus(other:Tuple):Tuple =
            Tuple(x - other.x, y - other.y, z - other.z, w - other.w)

    open operator fun unaryMinus():Tuple = Tuple(-x, -y, -z, -w)

    open operator fun times(scalar:Double):Tuple = Tuple(x * scalar, y * scalar, z * scalar, w * scalar)

    operator fun div(scalar:Double):Tuple = Tuple(x / scalar, y / scalar, z / scalar, w / scalar)

    fun asArray():Array<Double> = arrayOf(x, y, z, w)

    override fun equals(other: Any?): Boolean {
        if (other is Tuple) {
            return closeEnough(x, other.x) && closeEnough(y, other.y) && closeEnough(z, other.z) && closeEnough(w, other.w)
        }
        return false
    }

    override fun toString(): String = "[Tuple x:$x, y:$y, z:$z, w:$w]"

    fun asPoint() = Point(x, y, z)

    fun asVector() = Vector(x, y, z)
}

class Point(x:Double, y:Double, z:Double) : Tuple(x, y, z, 1.0) {

    constructor(x:Int, y:Int, z:Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun minus(other:Point):Vector =
            Vector(x - other.x, y - other.y, z - other.z)

    operator fun minus(other:Vector):Point =
            Point(x - other.x, y - other.y, z - other.z)

    operator fun plus(other:Vector):Point =
            Point(x + other.x, y + other.y, z + other.z)

}

class Vector(x:Double, y:Double, z:Double) : Tuple(x, y, z, 0.0) {

    constructor(x:Int, y:Int, z:Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun plus(other:Vector):Vector =
            Vector(x + other.x, y + other.y, z + other.z)

    operator fun minus(other:Vector):Vector =
            Vector(x - other.x, y - other.y, z - other.z)

    override operator fun unaryMinus():Vector = Vector(-x, -y, -z)

    override operator fun times(scalar:Double):Vector = Vector(x * scalar, y * scalar, z * scalar)

    fun magnitude() = Math.sqrt(x*x + y*y + z*z)

    fun normalize():Vector {
        val mag = magnitude()

        return Vector(x / mag, y / mag, z / mag)
    }

    fun dot(other:Vector):Double = x * other.x + y * other.y + z * other.z

    fun cross(other:Vector):Vector = Vector(y * other.z - z * other.y,
                                        z * other.x - x * other.z,
                                        x * other.y - y * other.x)

    fun reflect(other:Vector):Vector {
        return this - other * 2.0 * dot(other)
    }
}

data class Color(val red:Double, val green:Double, val blue:Double) {

    constructor(red:Int, green:Int, blue:Int) : this(red.toDouble(), green.toDouble(), blue.toDouble())

    operator fun plus(other:Color):Color =
            Color(red + other.red, green + other.green, blue + other.blue)

    operator fun minus(other:Color):Color =
            Color(red - other.red, green - other.green, blue - other.blue)

    operator fun times(scalar:Double):Color =
            Color(red * scalar, green * scalar, blue * scalar)

    operator fun times(other:Color):Color =
            Color(red * other.red, green * other.green, blue * other.blue)

    private fun clamp(value:Int):Int =
            when {
                value > 255 -> 255
                value < 0   -> 0
                else        -> value
            }

    private fun doubleToInt(c:Double):Int = clamp((c * 255).roundToInt())

    fun toIntString():String = "${doubleToInt(red)} ${doubleToInt(green)} ${doubleToInt(blue)}"

    override fun equals(other: Any?): Boolean {
        if (other is Color) {
            return closeEnough(red, other.red) && closeEnough(green, other.green) && closeEnough(blue, other.blue)
        }
        return false
    }
}
