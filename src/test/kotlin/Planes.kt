import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class Planes : FeatureSpec ({

    feature("Normals") {
        scenario("The normal of a plane is constant everywhere") {
            val p = Plane()

            p.localNormalAt(Point(0, 0, 0)).shouldBe(Vector(0, 1, 0))
            p.localNormalAt(Point(10, 0, -10)).shouldBe(Vector(0, 1, 0))
            p.localNormalAt(Point(5, 0, 150)).shouldBe(Vector(0, 1, 0))
        }
    }

    feature("Intersections") {
        scenario("Intersect with a ray parallel to the plane") {
            val p = Plane()
            val r = Ray(Point(0, 10, 0), Vector(0, 0, 1))
            val xs = p.localIntersect(r)

            xs.isEmpty().shouldBeTrue()
        }

        scenario("Intersect with a coplanar ray") {
            val p = Plane()
            val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
            val xs = p.localIntersect(r)

            xs.isEmpty().shouldBeTrue()
        }

        scenario("A ray intersecting a plane from above") {
            val p = Plane()
            val r = Ray(Point(0, 1, 0), Vector(0, -1, 0))
            val xs = p.localIntersect(r)

            xs.size.shouldBe(1)
            xs[0].t.shouldBe(1.0)
            xs[0].obj.shouldBe(p)
        }

        scenario("A ray intersecting a plane from below") {
            val p = Plane()
            val r = Ray(Point(0, -1, 0), Vector(0, 1, 0))
            val xs = p.localIntersect(r)

            xs.size.shouldBe(1)
            xs[0].t.shouldBe(1.0)
            xs[0].obj.shouldBe(p)
        }
    }

})
