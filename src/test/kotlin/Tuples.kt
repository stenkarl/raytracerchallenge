import io.kotlintest.matchers.plusOrMinus
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class Tuples : FeatureSpec ( {

    val epsilon = 0.00001

    feature ("tuples") {
        scenario("A tuple with w=1.0 is a point") {
            val t = Tuple(1.0, 2.0, 3.0, 1.0)
            t.x.shouldBe(1.0 plusOrMinus epsilon)
            t.y.shouldBe(2.0 plusOrMinus epsilon)
            t.z.shouldBe(3.0 plusOrMinus epsilon)
            t.w.shouldBe(1.0 plusOrMinus epsilon)
        }

        scenario("'point' describes tuples with w=1") {
            val p = Point(1.0, 2.0, 3.0)

            p.w.shouldBe(1.0 plusOrMinus epsilon)
        }

        scenario("'vector' describes tuples with w=0") {
            val v = Vector(1.0, 2.0, 3.0)

            v.w.shouldBe(0.0 plusOrMinus epsilon)
        }

        scenario ("Adding two tuples") {
            val a = Tuple(3.0, -2.0, 5.0, 1.0)
            val b = Tuple(-2.0, 3.0, 1.0, 0.0)

            val sum = a + b

            sum.x.shouldBe(1.0 plusOrMinus epsilon)
            sum.y.shouldBe(1.0 plusOrMinus epsilon)
            sum.z.shouldBe(6.0 plusOrMinus epsilon)

        }

        scenario ("Subtracting two points") {
            val a = Point(3.0, 2.0, 1.0)
            val b = Point(5.0, 6.0, 7.0)

            val diff = Vector (-2.0, -4.0, -6.0)

            diff.shouldBe(a - b)
        }

        scenario ("Subtracting two vectors") {
            val a = Vector(3.0, 2.0, 1.0)
            val b = Vector(5.0, 6.0, 7.0)

            val diff = Vector(-2.0, -4.0, -6.0)

            diff.shouldBe(a - b)
        }

        scenario("Negating a tuple") {
            val a = Tuple(1.0, -2.0, 3.0, -4.0)

            val negate = -a

            negate.shouldBe(Tuple(-1.0, 2.0, -3.0, 4.0))
        }

        scenario("Multiplying a tuple by a scalar") {
            val a = Tuple(1.0, -2.0, 3.0, -4.0)

            val product = a * 3.5

            product.shouldBe(Tuple(3.5, -7.0, 10.5, -14.0))
        }

        scenario("Multiplying a tuple by a fraction") {
            val a = Tuple(1.0, -2.0, 3.0, -4.0)

            val product = a * 0.5

            product.shouldBe(Tuple(0.5, -1.0, 1.5, -2.0))
        }

        scenario ("Dividing a tuple by a scalar") {
            val a = Tuple(1.0, -2.0, 3.0, -4.0)

            val quotient = a / 2.0

            quotient.shouldBe(Tuple(0.5, -1.0, 1.5, -2.0))
        }

    }

    feature("Vectors") {
        scenario ("Magnitude of vector(1, 0, 0)") {
            val a = Vector(1.0, 0.0, 0.0)

            a.magnitude().shouldBe(1.0)
        }

        scenario ("Magnitude of vector(, 1, 0)") {
            val a = Vector(0.0, 1.0, 0.0)

            a.magnitude().shouldBe(1.0)
        }

        scenario ("Magnitude of vector(0, 0, 1)") {
            val a = Vector(0.0, 0.0, 1.0)

            a.magnitude().shouldBe(1.0)
        }

        scenario ("Magnitude of vector(1, 2, 3)") {
            val a = Vector(1.0, 2.0, 3.0)

            a.magnitude().shouldBe(Math.sqrt(14.0))
        }

        scenario ("Magnitude of vector(-1, -2, -3)") {
            val a = Vector(-1.0, -2.0, -3.0)

            a.magnitude().shouldBe(Math.sqrt(14.0))
        }

        scenario("Normalizing vector(4, 0, 0) gives (1, 0, 0)") {
            val a = Vector(4.0, 0.0, 0.0)

            a.normalize().shouldBe(Vector(1.0, 0.0, 0.0))
        }

        scenario("Normalizing vector(1, 2, 3)") {
            val a = Vector(1.0, 2.0, 3.0)

            val root14 = Math.sqrt(14.0)

            a.normalize().shouldBe(Vector(1 / root14, 2 / root14, 3 / root14))
        }

        scenario("The dot product of two tuples") {
            val a = Vector(1.0, 2.0, 3.0)
            val b = Vector(2.0, 3.0, 4.0)

            a.dot(b).shouldBe(20.0)
        }

        scenario("Cross product of two vectors") {
            val a = Vector(1.0, 2.0, 3.0)
            val b = Vector(2.0, 3.0, 4.0)

            a.cross(b).shouldBe(Vector(-1.0, 2.0, -1.0))
            b.cross(a).shouldBe(Vector(1.0, -2.0, 1.0))

        }
    }

    feature("Colors") {
        scenario("Colors are (red, green, blue) Tuples") {
            val c = Color(-0.5, 0.4, 1.7)

            c.red.shouldBe(-0.5)
            c.green.shouldBe(0.4)
            c.blue.shouldBe(1.7)
        }

        scenario("Adding colors") {
            val c1 = Color(0.9, 0.6, 0.75)
            val c2 = Color(0.7, 0.1, 0.25)

            val sum = c1 + c2

            sum.shouldBe(Color(1.6, 0.7, 1.0))
        }

        scenario("Subtracting colors") {
            val c1 = Color(0.9, 0.6, 0.75)
            val c2 = Color(0.7, 0.1, 0.25)

            val diff = c1 - c2

            diff.red.shouldBe(0.2 plusOrMinus epsilon)
            diff.green.shouldBe(0.5 plusOrMinus epsilon)
            diff.blue.shouldBe(0.5 plusOrMinus epsilon)
        }

        scenario("Multiplying a color by a scalar") {
            val c = Color(0.2,0.3, 0.4)

            val product = c * 2.0

            product.shouldBe(Color(0.4, 0.6, 0.8))
        }

        scenario("Multiplying colors") {
            val c1 = Color(1.0, 0.2, 0.4)
            val c2 = Color(0.9, 1.0, 0.1)

            val product = c1 * c2

            product.red.shouldBe(0.9 plusOrMinus epsilon)
            product.green.shouldBe(0.2 plusOrMinus epsilon)
            product.blue.shouldBe(0.04 plusOrMinus epsilon)
        }
    }

    feature("Reflection") {
        scenario("Reflecting a vector approaching at 45 degrees") {
            val v = Vector(1, -1, 0)
            val n = Vector(0, 1, 0)

            val r = v.reflect(n)

            r.shouldBe(Vector(1, 1, 0))
        }

        scenario("Reflecting a vector off a slanted surface") {
            val v = Vector(0, -1, 0)

            val root2Over2 = Math.sqrt(2.0) / 2.0
            val n = Vector(root2Over2, root2Over2, 0.0)

            val r = v.reflect(n)

            r.shouldBe(Vector(1, 0, 0))
        }
    }

})
