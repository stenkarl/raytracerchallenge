
class World(val light:PointLight, val shapes:List<Shape>) {


    constructor() : this (PointLight.none(), ArrayList<Shape>())

    fun intersect(r:Ray):List<Intersection> {
        val intersections = shapes.flatMap { s -> s.intersect(r)}

        //return intersections.sortedBy { i -> i.t}

        return intersections.sortedWith(object : Comparator<Intersection> {
            override fun compare(i1: Intersection, i2: Intersection): Int = when {
                i1.t < 0 -> 1
                i2.t < 0 -> -1
                i1.t > i2.t -> 1
                i1.t == i2.t -> 0
                else -> -1
            }
        })
    }

    fun shade(comps:Computations, remaining:Int):Color {
        //println("shade $comps")
        val litColor = comps.shape.material.lighting(light, comps.shape, comps.point, comps.eye, comps.normal, isShadowed(comps.point))

        //println("litColor " + litColor)
        return litColor + reflectedColor(comps, remaining) + refractedColor(comps, remaining)
    }

    fun colorAt(ray:Ray, remaining:Int):Color {
        val xs = intersect(ray)
        if (xs.isNotEmpty()) {
            val hit = Intersection.hit(xs)
            if (hit != null) {
                val comps = hit.prepareComputations(ray)
                return shade(comps, remaining)
            }
        }
        return Color(0, 0, 0)
    }

    fun isShadowed(p:Point):Boolean {
        val distance = light.point - p
        val r = Ray(p, distance.normalize())
        val hit = Intersection.hit(intersect(r))

        return hit != null && hit.t < distance.magnitude()
    }

    fun reflectedColor(comps:Computations, remaining:Int):Color {
        if (comps.shape.material.reflective == 0.0 || remaining <= 0) {
            return Color(0, 0, 0)
        }
        val ray = Ray(comps.point, comps.reflect)
        return colorAt(ray, remaining - 1) * comps.shape.material.reflective

    }

    fun refractedColor(comp:Computations, remaining:Int):Color {
        if (comp.shape.material.transparency == 0.0 || remaining == 0) {
            return Color(0, 0, 0)
        }
        val ratio = comp.n1 / comp.n2
        val cosI = comp.eye.dot(comp.normal)
        val sin2t = ratio * ratio * (1 - cosI * cosI)
        if (sin2t > 1.0) {
            return Color(0, 0, 0)
        }

        val cost = Math.sqrt(1.0 - sin2t)
        val direction = comp.normal * (ratio * cosI - cost) - (comp.eye * ratio)
        val refractRay = Ray(comp.underPoint, direction)

        return colorAt(refractRay, remaining - 1) * comp.shape.material.transparency

    }

    companion object {

        fun default():World = default(PointLight(Point(-10, 10, -10), Color(1, 1, 1)))

        fun default(light:PointLight):World {
            val s1 = Sphere()
            s1.material = Material(Color(0.8, 1.0, 0.6), 0.1, 0.7, 0.2, 200)

            val s2 = Sphere()
            s2.transform = Matrix.scaling(0.5, 0.5, 0.5)

            return World(light, listOf(s1, s2))
        }

    }
}
