import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.matchers.doubles.shouldBeBetween
import io.kotlintest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotlintest.matchers.doubles.shouldBeLessThan
import io.kotlintest.matchers.plusOrMinus
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class Intersections : FeatureSpec({

    feature("intersections") {
        scenario("An intersection encapsulates t and object") {
            val s = Sphere()
            val i = Intersection(3.5, s)

            i.t.shouldBe(3.5)
            i.obj.shouldBe(s)
        }

        scenario("Aggregating intersections") {
            val s = Sphere()
            val i1 = Intersection(1.0, s)
            val i2 = Intersection(2.0, s)

            val intersections = Intersection.intersections(i1, i2)

            intersections.size.shouldBe(2)
            intersections[0].t.shouldBe(1.0)
            intersections[1].t.shouldBe(2.0)
        }
    }

    feature("Precompute") {
        scenario("Precomputing the state of an intersection") {
            val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val shape = Sphere()
            val i = Intersection(4.0, shape)

            val comps = i.prepareComputations(r)

            comps.t.shouldBe(i.t)
            comps.shape.shouldBe(i.obj)
            comps.point.x.shouldBe(0.0)
            comps.point.y.shouldBe(0.0)
            comps.point.z.shouldBe(-1.0 - epsilon)
            comps.eye.shouldBe(Vector(0, 0, -1))
            comps.normal.shouldBe(Vector(0, 0, -1))
        }

        scenario("The hit, when an intersection occurs on the outside") {
            val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val shape = Sphere()
            val i = Intersection(4.0, shape)

            val comps = i.prepareComputations(r)

            comps.inside.shouldBeFalse()
        }

        scenario("The hit, when an intersection occurs on the inside") {
            val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
            val shape = Sphere()
            val i = Intersection(1.0, shape)

            val comps = i.prepareComputations(r)

            comps.point.x.shouldBe(0.0)
            comps.point.y.shouldBe(0.0)
            comps.point.z.shouldBe(1.0 + epsilon)
            comps.inside.shouldBeTrue()
            comps.eye.shouldBe(Vector(0, 0, -1))
            comps.normal.shouldBe(Vector(0, 0, -1))
        }
    }

    feature("hit") {
        scenario("The hit, when all intersections have positive t") {
            val s = Sphere()
            val i1 = Intersection(1.0, s)
            val i2 = Intersection(2.0, s)

            val intersections = Intersection.intersections(i1, i2)
            val h = Intersection.hit(intersections)
            h.shouldBe(i1)
        }

        scenario("The hit, when some intersections have negative t") {
            val s = Sphere()
            val i1 = Intersection(-1.0, s)
            val i2 = Intersection(1.0, s)

            val intersections = Intersection.intersections(i1, i2)
            val h = Intersection.hit(intersections)
            h.shouldBe(i2)
        }

        scenario("The hit, when all intersections have negative t") {
            val s = Sphere()
            val i1 = Intersection(-1.0, s)
            val i2 = Intersection(-2.0, s)

            val intersections = Intersection.intersections(i1, i2)
            val h = Intersection.hit(intersections)
            h.shouldBe(null)
        }

        scenario("The hit is always the lowest non-negative intersection") {
            val s = Sphere()
            val i1 = Intersection(5.0, s)
            val i2 = Intersection(7.0, s)
            val i3 = Intersection(-3.0, s)
            val i4 = Intersection(2.0, s)

            val intersections = Intersection.intersections(i1, i2, i3, i4)
            val h = Intersection.hit(intersections)
            h.shouldBe(i4)
        }

        scenario("The point is offset") {
            val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val s = Sphere()
            val hit = Intersection(4.0, s)
            val comps = hit.prepareComputations(r)

            comps.point.z.shouldBeGreaterThanOrEqual(-1.1)
            comps.point.z.shouldBeLessThan(-1.0)
        }
    }

    fun glassSphere(refractiveIndex:Double, transform:Matrix):Sphere {
        val s = Sphere()
        val m = Material(Color(1, 1, 1), 0.1, 0.9, 0.9, 200,
                    0.0, 1.0, refractiveIndex)
        s.material = m
        s.transform = transform

        return s
    }

    feature("Refraction") {
        scenario("n1 and n2 at various intersections") {
            val a = glassSphere(1.5, Matrix.scaling(2, 2,2 ))
            val b = glassSphere(2.0, Matrix.translation(0.0, 0.0, -0.25))
            val c = glassSphere(2.5, Matrix.translation(0.0, 0.0, 0.25))

            val ray = Ray(Point(0.0, 0.0, -4.0), Vector(0.0, 0.0, 1.0))

            var xs = Intersection.intersections(Intersection(2.0, a), Intersection(2.75, b), Intersection(3.25, c),
                                        Intersection(4.75, b), Intersection(5.25, c), Intersection(6.0, a))

            val c0 = xs[0].prepareComputations(ray, xs)

            c0.n1.shouldBe(1.0)
            c0.n2.shouldBe(1.5)

            xs = Intersection.intersections(Intersection(-2.0, a), Intersection(2.75, b), Intersection(3.25, c),
                    Intersection(4.75, b), Intersection(5.25, c), Intersection(6.0, a))

            val c1 = xs[1].prepareComputations(ray, xs)

            c1.n1.shouldBe(1.5)
            c1.n2.shouldBe(2.0)

            xs = Intersection.intersections(Intersection(-2.0, a), Intersection(-1.75, b), Intersection(3.25, c),
                    Intersection(4.75, b), Intersection(5.25, c), Intersection(6.0, a))

            val c2 = xs[2].prepareComputations(ray, xs)

            c2.n1.shouldBe(2.0)
            c2.n2.shouldBe(2.5)

            xs = Intersection.intersections(Intersection(-2.0, a), Intersection(-1.75, b), Intersection(-1.25, c),
                    Intersection(4.75, b), Intersection(5.25, c), Intersection(6.0, a))

            val c3 = xs[3].prepareComputations(ray, xs)

            c3.n1.shouldBe(2.5)
            c3.n2.shouldBe(2.5)

            xs = Intersection.intersections(Intersection(-2.0, a), Intersection(-1.75, b), Intersection(-1.25, c),
                    Intersection(-1.0, b), Intersection(5.25, c), Intersection(6.0, a))

            val c4 = xs[4].prepareComputations(ray, xs)

            c4.n1.shouldBe(2.5)
            c4.n2.shouldBe(1.5)

            xs = Intersection.intersections(Intersection(-2.0, a), Intersection(-1.75, b), Intersection(-1.25, c),
                    Intersection(-1.0, b), Intersection(-0.75, c), Intersection(6.0, a))

            val c5 = xs[5].prepareComputations(ray, xs)

            c5.n1.shouldBe(1.5)
            c5.n2.shouldBe(1.0)
        }

        scenario("The under point is offset below the surface") {
            val shape = glassSphere(1.5, Matrix.identity())
            val ray = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val hit = Intersection(4.0, shape)
            val xs = Intersection.intersections(hit)

            val comps = hit.prepareComputations(ray, xs)
            comps.underPoint.z.shouldBeBetween(-1.0, -0.9, epsilon)
        }
    }

})
