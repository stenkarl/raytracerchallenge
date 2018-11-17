import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class Shapes : FeatureSpec ({

    class TestShape : Shape() {

        var localRay:Ray = Ray(Point(0,0,0), Vector(0,1,0))
        var localNormal:Vector = Vector(0.0, 0.0, 0.0)

        override fun localIntersect(r:Ray):List<Intersection> {
            localRay = r

            return listOf()
        }

        override fun localNormalAt(point: Tuple): Vector {
            localNormal = Vector(point.x, point.y, point.z)

            return localNormal
        }

    }

    feature("Shape Defaults") {
        scenario("The default transformation") {
            val s = TestShape()

            s.transform.shouldBe(Matrix.identity(4, 4))
        }

        scenario("Assigning a transformation") {
            val s = TestShape()
            val t = Matrix.translation(2,3, 4)

            s.transform = t

            s.transform.shouldBe(Matrix.translation(2, 3, 4))
        }

        scenario("The default material") {
            val s = TestShape()
            val m = s.material

            m.shouldBe(Material())
        }

        scenario("Assigning a material") {
            val s = TestShape()
            val m = Material(Color(1, 1, 1), 1.0, 0.7, 0.3, 200)
            s.material = m

            m.ambient.shouldBe(1.0)

        }
    }

    feature("Shape intersection") {
        scenario("Intersecting a scaled shape with a ray") {
            val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val s = TestShape()

            s.transform = Matrix.scaling(2, 2, 2)

            s.intersect(r)

            s.localRay.origin.shouldBe(Point(0.0, 0.0, -2.5))
            s.localRay.direction.shouldBe(Vector(0.0, 0.0, 0.5))
        }

        scenario("Intersecting a translated shape with a ray") {
            val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val s = TestShape()

            s.transform = Matrix.translation(5, 0, 0)

            s.intersect(r)

            s.localRay.origin.shouldBe(Point(-5, 0, -5))
            s.localRay.direction.shouldBe(Vector(0, 0, 1))
        }
    }

    feature("Shape normals") {
        scenario("Computing the normal on a translated shape") {
            val s = TestShape()
            s.transform = Matrix.translation(0, 1, 0)

            val n = s.normalAt(Point(0.0, 1.70711, -0.70711))

            n.shouldBe(Vector(0.0, 0.70711, -0.70711))
        }

        scenario("Computing the normal on a scaled shape") {
            val s = TestShape()
            s.transform = Matrix.scaling(1.0, 0.5, 1.0)

            val root2Over2 = Math.sqrt(2.0) / 2.0

            val n = s.normalAt(Point(0.0, root2Over2, -root2Over2))

            n.shouldBe(Vector(0.0, 0.97014, -0.24254))
        }
    }

})
