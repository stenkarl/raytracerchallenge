import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class RayTest : FeatureSpec ({

    feature("Rays") {
        scenario("Creating and querying a ray") {
            val origin = Point(1,2, 3)
            val dir = Vector(4, 5, 6)
            val r = Ray(origin, dir)

            r.origin.shouldBe(origin)
            r.direction.shouldBe(dir)
        }

        scenario("Computing a point from a distance") {
            val r = Ray(Point(2, 3, 4), Vector(1, 0, 0))

            r.position(0).shouldBe(Point(2, 3, 4))
            r.position(1).shouldBe(Point(3, 3, 4))
            r.position(-1).shouldBe(Point(1, 3, 4))
            r.position(2.5).shouldBe(Point(4.5, 3.0, 4.0))
        }

        scenario("Translating a ray") {
            val r = Ray(Point(1, 2, 3), Vector(0, 1, 0))
            val m = Matrix.translation(3, 4, 5)

            val r2 = r.transform(m)

            r2.origin.shouldBe(Point(4, 6, 8))
            r2.direction.shouldBe(Vector(0, 1, 0))
        }

        scenario("Scaling a ray") {
            val r = Ray(Point(1, 2, 3), Vector(0, 1, 0))
            val m = Matrix.scaling(2, 3, 4)

            val r2 = r.transform(m)

            r2.origin.shouldBe(Point(2, 6, 12))
            r2.direction.shouldBe(Vector(0, 3, 0))
        }
    }
})
