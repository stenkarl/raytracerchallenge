class Sphere : Shape() {

    override fun equals(other:Any?):Boolean {
        if (other is Sphere) {
            return this.transform == other.transform && this.material == other.material
        }
        return false
    }

    override fun toString(): String = "[Sphere $material]"

    override fun localIntersect(ray:Ray):List<Intersection> {
        val sphereToRay = ray.origin - Point(0, 0, 0)
        val a = ray.direction.dot(ray.direction)
        val b = 2 * ray.direction.dot(sphereToRay)
        val c = sphereToRay.dot(sphereToRay) - 1
        val discriminant = b * b - 4 * a * c

        if (discriminant < 0) {
            return Intersection.intersections()
        }

        val t1 = (-b - Math.sqrt(discriminant)) / (2 * a)
        val t2 = (-b + Math.sqrt(discriminant)) / (2 * a)
        if (t1 > t2) {
            return Intersection.intersections(Intersection(t2, this), Intersection(t1, this))
        }

        return Intersection.intersections(Intersection(t1, this), Intersection(t2, this))
    }

    override fun localNormalAt(point:Tuple):Vector {
        val tuple = point - Point(0, 0, 0)

        return Vector(tuple.x, tuple.y, tuple.z)
    }

}
