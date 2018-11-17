import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class Spheres : FeatureSpec ({


    feature("Intersections") {
        scenario("A ray intersects a sphere at two points") {
            val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val s = Sphere()

            val hit = s.localIntersect(r)

            hit.size.shouldBe(2)
            hit[0].t.shouldBe(4.0)
            hit[1].t.shouldBe(6.0)
        }

        scenario("A ray intersects a sphere at a tangent") {
            val r = Ray(Point(0, 1, -5), Vector(0, 0, 1))
            val s = Sphere()

            val hit = s.localIntersect(r)

            hit.size.shouldBe(2)
            hit[0].t.shouldBe(5.0)
            hit[1].t.shouldBe(5.0)
        }

        scenario("A ray misses a sphere") {
            val r = Ray(Point(0, 2, -5), Vector(0, 0, 1))
            val s = Sphere()

            val hit = s.localIntersect(r)

            hit.size.shouldBe(0)
        }

        scenario("A ray originates inside a sphere") {
            val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
            val s = Sphere()

            val hit = s.localIntersect(r)

            hit.size.shouldBe(2)
            hit[0].t.shouldBe(-1.0)
            hit[1].t.shouldBe(1.0)
        }

        scenario("A sphere is behind a ray") {
            val r = Ray(Point(0, 0, 5), Vector(0, 0, 1))
            val s = Sphere()

            val hit = s.localIntersect(r)

            hit.size.shouldBe(2)
            hit[0].t.shouldBe(-6.0)
            hit[1].t.shouldBe(-4.0)
        }

        scenario("Intersect sets the object on the intersection") {
            val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val s = Sphere()
            val xs = s.localIntersect(r)

            xs.size.shouldBe(2)
            xs[0].obj.shouldBe(s)
            xs[1].obj.shouldBe(s)
        }
    }

    feature ("Normals") {
        scenario("The normal on a sphere at a point on the x axis") {
            val s = Sphere()

            val n = s.localNormalAt(Point(1, 0, 0))

            n.shouldBe(Vector(1, 0, 0))
        }

        scenario("The normal on a sphere at a point on the y axis") {
            val s = Sphere()

            val n = s.localNormalAt(Point(0, 1, 0))

            n.shouldBe(Vector(0, 1, 0))
        }

        scenario("The normal on a sphere at a point on the z axis") {
            val s = Sphere()

            val n = s.localNormalAt(Point(0, 0, 1))

            n.shouldBe(Vector(0, 0, 1))
        }

        scenario("The normal on a sphere at a point at a non-axial point") {
            val s = Sphere()

            val root3Over3 = Math.sqrt(3.0) / 3.0
            val n = s.localNormalAt(Point(root3Over3, root3Over3, root3Over3))

            n.shouldBe(Vector(root3Over3, root3Over3, root3Over3))
        }

        scenario("The normal is a normalized vector") {
            val s = Sphere()

            val root3Over3 = Math.sqrt(3.0) / 3.0
            val n = s.localNormalAt(Point(root3Over3, root3Over3, root3Over3))

            n.shouldBe(n.normalize())
        }
    }

    fun glassSphere():Sphere {
        val s = Sphere()
        val m = Material(Color(1, 1, 1), 0.1, 0.9, 0.9, 200, 0.0, 1.0, 1.5)
        s.material = m

        return s
    }

    feature("Refraction") {
        scenario("A helper for producing a sphere with a glassy material") {
            val s = glassSphere()

            s.transform.shouldBe(Matrix.identity(4, 4))
            s.material.transparency.shouldBe(1.0)
            s.material.refractiveIndex.shouldBe(1.5)
        }
    }
})
