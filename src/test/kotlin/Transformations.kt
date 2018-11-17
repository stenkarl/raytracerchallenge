import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class Transformations : FeatureSpec ({


    feature ("Translations") {
        scenario ("Multiplying by a translation matrix") {
            val transform = Matrix.translation(5.0, -3.0, 2.0)
            val p = Point(-3.0, 4.0, 5.0)

            (transform * p).shouldBe(Point(2.0, 1.0, 7.0))
        }

        scenario("Multiplying by the inverse of a translation matrix") {
            val transform = Matrix.translation(5.0, -3.0, 2.0)
            val inv = transform.inverse()
            val p = Point(-3, 4, 5)

            (inv * p).shouldBe(Point(-8.0, 7.0, 3.0))

        }

        scenario("Translation does not affect vectors") {
            val transform = Matrix.translation(5, -3, 2)
            val v = Vector(-3.0, 4.0, 5.0)

            (transform * v).shouldBe(v)
        }
    }

    feature("Scalings") {
        scenario("A scaling matrix applied to a point") {
            val transform = Matrix.scaling(2, 3, 4)
            val p = Point(-4, 6, 8)

            (transform * p).shouldBe(Point(-8, 18, 32))
        }

        scenario("A scaling matrix applied to a vector") {
            val transform = Matrix.scaling(2, 3, 4)
            val v = Vector(-4, 6, 8)

            (transform * v).shouldBe(Vector(-8, 18, 32))
        }

        scenario("Multiplying by the inverse of a scaling matrix") {
            val transform = Matrix.scaling(2, 3, 4)
            val inv = transform.inverse()
            val v = Vector(-4, 6, 8)

            (inv * v).shouldBe(Vector(-2, 2, 2))
        }

        scenario("Reflection is scaling by a negative value") {
            val transform = Matrix.scaling(-1, 1, 1)
            val p = Point(2, 3, 4)

            (transform * p).shouldBe(Point(-2, 3, 4))
        }
    }

    feature("Rotations") {
        scenario("Rotating a point around the x axis") {
            val p = Point(0, 1, 0)
            val halfQuarter = Matrix.rotationX(Math.PI / 4.0)
            val fullQuarter = Matrix.rotationX(Math.PI / 2.0)

            val root2Over2 = Math.sqrt(2.0) / 2.0

            (halfQuarter * p).shouldBe(Point(0.0, root2Over2, root2Over2))
            (fullQuarter * p).shouldBe(Point(0, 0, 1))
        }

        scenario("The inverse of an x-rotation rotates in the opposite direction") {
            val p = Point(0, 1, 0)
            val halfQuarter = Matrix.rotationX(Math.PI / 4.0)
            val inv = halfQuarter.inverse()

            val root2Over2 = Math.sqrt(2.0) / 2.0

            (inv * p).shouldBe(Point(0.0, root2Over2, -root2Over2))
        }

        scenario("Rotating a point around the y axis") {
            val p = Point(0, 0, 1)
            val halfQuarter = Matrix.rotationY(Math.PI / 4.0)
            val fullQuarter = Matrix.rotationY(Math.PI / 2.0)

            val root2Over2 = Math.sqrt(2.0) / 2.0

            (halfQuarter * p).shouldBe(Point(root2Over2, 0.0, root2Over2))
            (fullQuarter * p).shouldBe(Point(1, 0, 0))
        }

        scenario("Rotating a point around the z axis") {
            val p = Point(0, 1, 0)
            val halfQuarter = Matrix.rotationZ(Math.PI / 4.0)
            val fullQuarter = Matrix.rotationZ(Math.PI / 2.0)

            val root2Over2 = Math.sqrt(2.0) / 2.0

            (halfQuarter * p).shouldBe(Point(-root2Over2, root2Over2, 0.0))
            (fullQuarter * p).shouldBe(Point(-1, 0, 0))
        }
    }

    feature("Shearing") {

        scenario("Shearing transformation moves x in proportion to y") {
            val transform = Matrix.shearing(1, 0, 0, 0, 0, 0)
            val p = Point(2, 3, 4)

            (transform * p).shouldBe(Point(5, 3, 4))
        }

        scenario("Shearing transformation moves x in proportion to z") {
            val transform = Matrix.shearing(0, 1, 0, 0, 0, 0)
            val p = Point(2, 3, 4)

            (transform * p).shouldBe(Point(6, 3, 4))
        }

        scenario("Shearing transformation moves y in proportion to x") {
            val transform = Matrix.shearing(0, 0, 1, 0, 0, 0)
            val p = Point(2, 3, 4)

            (transform * p).shouldBe(Point(2, 5, 4))
        }

        scenario("Shearing transformation moves y in proportion to z") {
            val transform = Matrix.shearing(0, 0, 0, 1, 0, 0)
            val p = Point(2, 3, 4)

            (transform * p).shouldBe(Point(2, 7, 4))
        }

        scenario("Shearing transformation moves z in proportion to x") {
            val transform = Matrix.shearing(0, 0, 0, 0, 1, 0)
            val p = Point(2, 3, 4)

            (transform * p).shouldBe(Point(2, 3, 6))
        }

        scenario("Shearing transformation moves z in proportion to y") {
            val transform = Matrix.shearing(0, 0, 0, 0, 0, 1)
            val p = Point(2, 3, 4)

            (transform * p).shouldBe(Point(2, 3, 7))
        }
    }

    feature("Chaining") {

        scenario("Individual transformations are applied in sequence") {
            val p = Point(1, 0, 1)
            val a = Matrix.rotationX(Math.PI / 2.0)
            val b = Matrix.scaling(5, 5, 5)
            val c = Matrix.translation(10, 5, 7)

            val p2 = a * p
            p2.shouldBe(Point(1, -1, 0))

            val p3 = b * p2
            p3.shouldBe(Point(5, -5, 0))

            val p4 = c * p3
            p4.shouldBe(Point(15, 0, 7))
        }

        scenario("Chained transformations must be applied in reverse order") {
            val p = Point(1, 0, 1)
            val a = Matrix.rotationX(Math.PI / 2.0)
            val b = Matrix.scaling(5, 5, 5)
            val c = Matrix.translation(10, 5, 7)

            val t = c * (b * a)
            (t * p).shouldBe(Point(15, 0, 7))
        }
    }

})
