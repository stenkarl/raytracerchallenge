
abstract class Pattern {
    var transform:Matrix = Matrix.identity(4, 4)

    fun patternAtShape(shape:Shape, p:Point):Color {
        val localP = shape.transform.inverse() * p
        val patternP = transform.inverse() * localP

        return patternAt(patternP.asPoint())
    }

    abstract fun patternAt(p:Point):Color

}

class StripePattern(val c1:Color, val c2:Color) : Pattern() {
    override fun patternAt(p: Point): Color =
        if (Math.floor(p.x).toInt() % 2 == 0) c1 else c2

}

class GradientPattern(val c1:Color, val c2:Color) : Pattern() {

    override fun patternAt(p: Point): Color {
        val distance = c2 - c1
        val fraction = p.x - Math.floor(p.x)

        return c1 + distance * fraction
    }

}

class RingPattern(val c1:Color, val c2:Color) : Pattern() {

    override fun patternAt(p: Point): Color {
        val floor = Math.floor(Math.sqrt(p.x * p.x + p.z * p.z))
        return if (floor.toInt() % 2 == 0) {
            c1
        } else {
            c2
        }
    }
}

class CheckersPattern(val c1:Color, val c2:Color) : Pattern() {

    override fun patternAt(p: Point): Color = if ((Math.floor(p.x) + Math.floor(p.y) + Math.floor(p.z)) % 2 == 0.0) c1 else c2

}
