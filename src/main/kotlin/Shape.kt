
abstract class Shape {

    var transform = Matrix.identity(4, 4)
    var material = Material()

    fun intersect(r:Ray):List<Intersection> {
        val ray = r.transform(transform.inverse())

        return localIntersect(ray)
    }

    fun normalAt(point:Point):Vector {
        val objPoint = transform.inverse() * point

        val objNormal = localNormalAt(objPoint)

        val worldNormalTuple = transform.inverse().transpose() * objNormal

        val worldNormal = Vector(worldNormalTuple.x, worldNormalTuple.y, worldNormalTuple.z)

        return worldNormal.normalize()
    }

    abstract fun localNormalAt(point:Tuple):Vector

    abstract fun localIntersect(ray:Ray):List<Intersection>

}
