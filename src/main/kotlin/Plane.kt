
class Plane : Shape() {

    private val epsilon = 0.0001

    override fun localNormalAt(point: Tuple): Vector = Vector(0, 1, 0)

    override fun localIntersect(r: Ray): List<Intersection> =
        if (Math.abs(r.direction.y) < epsilon) {
            listOf()
        } else {
            val t = -r.origin.y / r.direction.y
            listOf(Intersection(t, this))
        }

}
