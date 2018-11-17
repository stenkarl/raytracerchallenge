class Intersection(val t:Double, val obj:Shape) {

    fun prepareComputations(r:Ray) = prepareComputations(r, listOf())

    fun prepareComputations(r:Ray, xs:List<Intersection>) : Computations {
        val rawPoint = r.position(t)
        val eye = r.direction * -1.0
        var normal = obj.normalAt(rawPoint)

        val point = rawPoint + normal * epsilon
        val inside = normal.dot(eye) < 0.0
        if (inside) {
            normal = -normal
        }
        val underPoint = rawPoint - normal * epsilon

        val reflect = r.direction.reflect(normal)

        val nValues = processRefraction(xs)

        return Computations(t, obj, point, underPoint, eye, normal, inside, reflect, nValues.first, nValues.second)
    }

    private fun processRefraction(xs:List<Intersection>):Pair<Double, Double> {
        val containers = mutableListOf<Shape>()
        val hit = hit(xs)
        var n1 = 0.0
        var n2 = 0.0
        xs.forEach { i ->
            if (i == hit) {
                n1 = if (containers.isEmpty()) 1.0 else containers.last().material.refractiveIndex
            }
            if (containers.contains(i.obj)) {
                containers.remove(i.obj)
            } else {
                containers.add(i.obj)
            }
            if (i == hit) {
                n2 = if (containers.isEmpty()) 1.0 else containers.last().material.refractiveIndex
            }
        }
        return Pair(n1, n2)
    }

    override fun toString(): String = "[Intersection t:$t, obj:$obj]"

    companion object {

        fun intersections(vararg intersections:Intersection) = intersections.asList()

        fun hit(intersections:List<Intersection>):Intersection? {
            if (intersections.find {i -> i.t > 0} == null) {
                return null
            }
            val positiveIntersections = intersections.filter { i -> i.t > 0}

            return positiveIntersections.minWith(object : Comparator<Intersection> {
                override fun compare(i1: Intersection, i2: Intersection): Int = when {
                    i1.t > i2.t -> 1
                    i1.t < 0 -> 1
                    i1.t == i2.t -> 0
                    else -> -1
                }
            })

        }
    }

}

data class Computations(val t:Double, val shape:Shape, val point:Point, val underPoint:Point, val eye:Vector, val normal:Vector, val inside:Boolean,
                        val reflect:Vector, val n1:Double, val n2:Double)

