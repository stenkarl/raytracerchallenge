import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class Patterns : FeatureSpec ({

    feature("Stripe Pattern") {

        scenario("Creating a stripe pattern") {
            val white = Color(1, 1,1 )
            val black = Color(0, 0, 0)
            val p = StripePattern(white, black)

            p.c1.shouldBe(white)
            p.c2.shouldBe(black)
        }

        scenario("A stripe pattern is constant in y") {
            val white = Color(1, 1,1 )
            val black = Color(0, 0, 0)
            val p = StripePattern(white, black)

            p.patternAt(Point(0, 0, 0)).shouldBe(white)
            p.patternAt(Point(0, 1, 0)).shouldBe(white)
            p.patternAt(Point(0, 2, 0)).shouldBe(white)
        }

        scenario("A stripe pattern is constant in z") {
            val white = Color(1, 1,1 )
            val black = Color(0, 0, 0)
            val p = StripePattern(white, black)

            p.patternAt(Point(0, 0, 0)).shouldBe(white)
            p.patternAt(Point(0, 0, 1)).shouldBe(white)
            p.patternAt(Point(0, 0, 2)).shouldBe(white)
        }

        scenario("A stripe pattern alternates in x") {
            val white = Color(1, 1,1 )
            val black = Color(0, 0, 0)
            val p = StripePattern(white, black)

            p.patternAt(Point(0, 0, 0)).shouldBe(white)
            p.patternAt(Point(0.9, 0.0, 0.0)).shouldBe(white)
            p.patternAt(Point(1, 0, 0)).shouldBe(black)
            p.patternAt(Point(-0.1, 0.0, 0.0)).shouldBe(black)
            p.patternAt(Point(-1, 0, 0)).shouldBe(black)
            p.patternAt(Point(-1.1, 0.0, 0.0)).shouldBe(white)
        }

    }

    feature("Default Pattern") {
        scenario("The default pattern transformation") {
            val p = TestPattern()

            p.transform.shouldBe(Matrix.identity(4, 4))
        }

        scenario("Assigning a transformation") {
            val p = TestPattern()
            p.transform = Matrix.translation(1, 2, 3)

            p.transform.shouldBe(Matrix.translation(1, 2, 3))
        }

        scenario("Pattern with an object transformation") {
            val s = Sphere()
            s.transform = Matrix.scaling(2, 2, 2)

            val p = TestPattern()

            val c = p.patternAtShape(s, Point(2, 3, 4))

            c.shouldBe(Color(1.0, 1.5, 2.0))
        }

        scenario("Pattern with a pattern transformation") {
            val s = Sphere()

            val p = TestPattern()
            p.transform = Matrix.scaling(2, 2, 2)

            val c = p.patternAtShape(s, Point(2, 3, 4))

            c.shouldBe(Color(1.0, 1.5, 2.0))
        }

        scenario("Pattern with both an object and a pattern transformation") {
            val s = Sphere()
            s.transform = Matrix.scaling(2, 2, 2)

            val p = TestPattern()
            p.transform = Matrix.translation(0.5, 1.0, 1.5)

            val c = p.patternAtShape(s, Point(2.5, 3.0, 3.5))

            c.shouldBe(Color(0.75, 0.5, 0.25))
        }
    }

    feature("Gradient Pattern") {
        scenario("Gradient linearly interpolates between colors") {
            val white = Color(1, 1,1)
            val black = Color(0, 0,0)
            val pattern = GradientPattern(black, white)

            pattern.patternAt(Point(0, 0, 0)).shouldBe(black)
            pattern.patternAt(Point(0.25, 0.0, 0.0)).shouldBe(Color(0.25, 0.25, 0.25))
            pattern.patternAt(Point(0.5, 0.0, 0.0)).shouldBe(Color(0.5, 0.5, 0.5))
            pattern.patternAt(Point(0.75, 0.0, 0.0)).shouldBe(Color(0.75, 0.75, 0.75))

        }
    }

    feature("Ring Pattern") {
        scenario("Ring should extend in both x and z") {
            val white = Color(1, 1,1)
            val black = Color(0, 0,0)
            val pattern = RingPattern(black, white)

            pattern.patternAt(Point(0, 0, 0)).shouldBe(black)
            pattern.patternAt(Point(1, 0, 0)).shouldBe(white)
            pattern.patternAt(Point(0, 0, 1)).shouldBe(white)
            pattern.patternAt(Point(0.708, 0.0, 0.708)).shouldBe(white)

        }
    }

    feature("Checkers Pattern") {
        scenario("Checkers should repeat in x") {
            val white = Color(1, 1,1)
            val black = Color(0, 0,0)
            val pattern = CheckersPattern(black, white)

            pattern.patternAt(Point(0, 0, 0)).shouldBe(black)
            pattern.patternAt(Point(0.99, 0.0, 0.0)).shouldBe(black)
            pattern.patternAt(Point(1.01, 0.0, 0.0)).shouldBe(white)
        }

        scenario("Checkers should repeat in y") {
            val white = Color(1, 1,1)
            val black = Color(0, 0,0)
            val pattern = CheckersPattern(black, white)

            pattern.patternAt(Point(0, 0, 0)).shouldBe(black)
            pattern.patternAt(Point(0.0, 0.99, 0.0)).shouldBe(black)
            pattern.patternAt(Point(0.0, 1.01, 0.0)).shouldBe(white)
        }

        scenario("Checkers should repeat in z") {
            val white = Color(1, 1,1)
            val black = Color(0, 0,0)
            val pattern = CheckersPattern(black, white)

            pattern.patternAt(Point(0, 0, 0)).shouldBe(black)
            pattern.patternAt(Point(0.0, 0.0, 0.99)).shouldBe(black)
            pattern.patternAt(Point(0.0, 0.0, 1.01)).shouldBe(white)
        }
    }

})

class TestPattern : Pattern() {

    override fun patternAt(p:Point):Color {
        val c = Color(p.x, p.y, p.z)
        println("patternAt $c")

        return c
    }

}
