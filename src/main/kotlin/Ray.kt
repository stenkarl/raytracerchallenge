class Ray(val origin:Point, val direction:Vector) {

    fun position(t:Int) = position(t.toDouble())

    fun position(t:Double):Point = origin + direction * t

    fun transform(m:Matrix):Ray {
        val newOrigin = m * origin
        val newDir = m * direction

        return Ray(Point(newOrigin.x, newOrigin.y, newOrigin.z), Vector(newDir.x, newDir.y, newDir.z))
    }

    override fun toString(): String = "[Ray, origin:$origin, direction:$direction]"
}
