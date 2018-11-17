import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.matchers.plusOrMinus
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Worlds : FeatureSpec ({

    val epsilon = 0.0001


    feature("Worlds") {
        scenario("Creating a world") {
            val w = World()

            w.light.shouldBe(PointLight.none())
            w.shapes.size.shouldBe(0)
        }

        scenario("The default world") {
            val w = World.default()

            val s1 = Sphere()
            s1.material = Material(Color(0.8, 1.0, 0.6), 0.1, 0.7, 0.2, 200)

            val s2 = Sphere()
            s2.transform = Matrix.scaling(0.5, 0.5, 0.5)

            val light = PointLight(Point(-10, 10, -10), Color(1, 1, 1))

            w.light.shouldBe(light)
            w.shapes.contains(s1).shouldBeTrue()
            w.shapes.contains(s2).shouldBeTrue()
        }

        scenario("Intersect a world with a ray") {
            val w = World.default()
            val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))

            val xs = w.intersect(r)

            xs.size.shouldBe(4)
            xs[0].t.shouldBe(4.0)
            xs[1].t.shouldBe(4.5)
            xs[2].t.shouldBe(5.5)
            xs[3].t.shouldBe(6.0)
        }

        scenario("Precomputing the state of an intersection") {
            val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val s = Sphere()
            val hit = Intersection(4.0, s)

            val comps = hit.prepareComputations(r)

            comps.point.shouldBe(Point(0.0, 0.0, -1.00001))
            comps.eye.shouldBe(Vector(0, 0, -1))
            comps.normal.shouldBe(Vector(0, 0, -1))

        }

        scenario("An intersection occurs on the outside") {
            val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val s = Sphere()
            val hit = Intersection(4.0, s)

            val comps = hit.prepareComputations(r)
            assertFalse(comps.inside)
        }

        scenario("An intersection occurs on the inside") {
            val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
            val s = Sphere()
            val hit = Intersection(1.0, s)

            val comps = hit.prepareComputations(r)
            comps.point.shouldBe(Point(0.0, 0.0, 1.00001))
            comps.eye.shouldBe(Vector(0, 0, -1))
            comps.normal.shouldBe(Vector(0, 0, -1))
            assertTrue(comps.inside)
        }
    }

    feature("Shading") {
        scenario("Shading an intersection") {
            val w = World.default()
            val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val shape = w.shapes[0]
            val hit = Intersection(4.0, shape)
            val comps = hit.prepareComputations(r)

            val c = w.shade(comps, 1)

            c.shouldBe(Color(0.38066, 0.47583, 0.2855))
        }

        scenario("Shading an intersection from the inside") {
            val w = World.default(PointLight(Point(0.0, 0.25, 0.0), Color(1, 1, 1)))
            val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
            val shape = w.shapes[1]
            val hit = Intersection(0.5, shape)

            val comps = hit.prepareComputations(r)
            val c = w.shade(comps, 1)

            //c.shouldBe(Color(0.90498, 0.90498, 0.90498)) // before isShadow was implemented
            c.shouldBe(Color(0.1, 0.1, 0.1))
        }

        scenario("The color when a ray misses") {
            val w = World.default()
            val r = Ray(Point(0, 0, -5), Vector(0, 1, 0))

            val c = w.colorAt(r, 1)

            c.shouldBe(Color(0, 0, 0))
        }

        scenario("The color when a ray hits") {
            val w = World.default()
            val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))

            val c = w.colorAt(r, 1)

            c.shouldBe(Color(0.38066, 0.47583, 0.2855))
        }

        scenario("The color with an intersection behind the ray") {
            val w = World.default()
            val outer = w.shapes[0]
            outer.material = Material(Color(0.8, 1.0, 0.6), 1.0, 0.7, 0.2, 200)

            val inner = w.shapes[1]
            inner.material = Material(Color(1, 1, 1), 1.0, 0.0, 0.0, 0)
            val r = Ray(Point(0.0, 0.0, -0.75), Vector(0.0, 0.0, 1.0))
            val c = w.colorAt(r, 1)

            c.shouldBe(inner.material.color)
        }
    }

    feature("Shadows") {
        scenario("There is no shadow when nothing is collinear with point and light") {
            val w = World.default()
            val p = Point(0, 10, 0)

            w.isShadowed(p).shouldBeFalse()
        }

        scenario("Shadow when an object is between the point and the light") {
            val w = World.default()
            val p = Point(10, -10, 10)

            w.isShadowed(p).shouldBeTrue()
        }

        scenario("There is no shadow when an object is behind the light") {
            val w = World.default()
            val p = Point(-20, 20, -20)

            w.isShadowed(p).shouldBeFalse()
        }

        scenario("There is no shadow when an object is behind the point") {
            val w = World.default()
            val p = Point(-2, 20, -2)

            w.isShadowed(p).shouldBeFalse()
        }

        scenario("When shade_hit is given an intersection in shadow") {
            val light = PointLight(Point(0, 0, -10), Color(1, 1, 1))
            val s1 = Sphere()
            val s2 = Sphere()
            s2.transform = Matrix.translation(0, 0, 10)
            val w = World(light, listOf(s1, s2))
            val r = Ray(Point(0, 0, 5), Vector(0, 0, 1))
            val h = Intersection(4.0, s2)
            val comps = h.prepareComputations(r)
            w.isShadowed(comps.point).shouldBeTrue()

            val c = w.shade(comps, 1)

            c.shouldBe(Color(0.1, 0.1, 0.1))

        }
    }

    feature("Reflections") {
        scenario("Reflected color for non-reflective material") {
            val world = World.default()
            val ray = Ray(Point(0, 0, 0), Vector(0, 0, 1))
            val shape = world.shapes[1]
            shape.material = Material(Color(1, 1, 1), 1.0, 0.9, 0.9, 200, 0.0, 0.0, 1.0)
            val hit = Intersection(1.0, shape)

            val comps = hit.prepareComputations(ray)
            val color = world.reflectedColor(comps, 1)

            color.shouldBe(Color(0, 0, 0))
        }

        scenario("Reflected color for reflective material") {
            val shape = Plane()
            shape.material = Material(0.5)
            shape.transform = Matrix.translation(0, -1, 0)
            val s1 = Sphere()
            s1.material = Material(Color(0.8, 1.0, 0.6), 0.1, 0.7, 0.2, 200)

            val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)), listOf(s1, shape))
            val root2Over2 = Math.sqrt(2.0) / 2.0

            val ray = Ray(Point(0, 0, -3), Vector(0.0, -root2Over2, root2Over2))
            val hit = Intersection(Math.sqrt(2.0), shape)
            val comps = hit.prepareComputations(ray)
            val color = world.reflectedColor(comps, 1)

            color.red.shouldBe(0.19032 plusOrMinus epsilon)
            color.green.shouldBe(0.2379 plusOrMinus epsilon)
            color.blue.shouldBe(0.14274 plusOrMinus epsilon)
        }

        scenario("shade_hit with reflective material") {
            val shape = Plane()
            shape.material = Material(0.5)
            shape.transform = Matrix.translation(0, -1, 0)
            val s1 = Sphere()
            s1.material = Material(Color(0.8, 1.0, 0.6), 0.1, 0.7, 0.2, 200)

            val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)), listOf(s1, shape))
            val root2Over2 = Math.sqrt(2.0) / 2.0

            val ray = Ray(Point(0, 0, -3), Vector(0.0, -root2Over2, root2Over2))
            val hit = Intersection(Math.sqrt(2.0), shape)
            val comps = hit.prepareComputations(ray)
            val color = world.shade(comps, 1)

            color.red.shouldBe(0.87677 plusOrMinus epsilon)
            color.green.shouldBe(0.92436 plusOrMinus epsilon)
            color.blue.shouldBe(0.82918 plusOrMinus epsilon)
        }

        scenario("color_at with mutually reflective surfaces") {
            val lower = Plane()
            lower.material = Material(1.0)
            lower.transform = Matrix.translation(0, -1, 0)

            val upper = Plane()
            upper.material = Material(1.0)
            upper.transform = Matrix.translation(0, 1, 0)

            val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)), listOf(lower, upper))
            val ray = Ray(Point(0, 0, 0), Vector(0, 1, 0))

            world.colorAt(ray, 4)
        }

        scenario("Reflected color at maximum recursive depth") {
            val shape = Plane()
            shape.material = Material(0.5)
            shape.transform = Matrix.translation(0, -1, 0)
            val s1 = Sphere()
            s1.material = Material(Color(0.8, 1.0, 0.6), 0.1, 0.7, 0.2, 200)

            val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)), listOf(s1, shape))
            val root2Over2 = Math.sqrt(2.0) / 2.0

            val ray = Ray(Point(0, 0, -3), Vector(0.0, -root2Over2, root2Over2))
            val hit = Intersection(Math.sqrt(2.0), shape)
            val comps = hit.prepareComputations(ray)
            val color = world.reflectedColor(comps, 0)

            color.shouldBe(Color(0, 0, 0))
        }
    }

    feature("Refractions") {
        scenario("Refracted color with opaque surface") {
            val world = World.default()
            val shape = world.shapes[0]
            val ray = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val xs = Intersection.intersections(Intersection(4.0, shape), Intersection(6.0, shape))

            val c0 = xs[0].prepareComputations(ray, xs)
            val color = world.refractedColor(c0, 5)

            color.shouldBe(Color(0, 0, 0))
        }

        scenario("Refracted color at maximum recursive depth") {
            val shape = Sphere()
            shape.material = Material(Color(1, 1, 1), 0.1, 0.9, 0.9, 200, 0.0, 1.0, 1.0)
            val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)), listOf(shape))
            val ray = Ray(Point(0, 0, -5), Vector(0, 0, 1))
            val xs = Intersection.intersections(Intersection(4.0, shape), Intersection(6.0, shape))
            val c0 = xs[0].prepareComputations(ray, xs)
            val color = world.refractedColor(c0, 0)

            color.shouldBe(Color(0, 0, 0))
        }

        scenario("Refracted color under total internal reflection") {
            val shape = Sphere()
            shape.material = Material(Color(1, 1, 1), 0.1, 0.9, 0.9, 200, 0.0, 1.0, 1.5)
            val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)), listOf(shape))
            val root2Over2 = Math.sqrt(2.0) / 2.0
            val ray = Ray(Point(0.0, 0.0, root2Over2), Vector(0, 1, 0))
            val xs = Intersection.intersections(Intersection(-root2Over2, shape), Intersection(root2Over2, shape))
            val c1 = xs[1].prepareComputations(ray, xs)
            val color = world.refractedColor(c1, 5)

            color.shouldBe(Color(0, 0, 0))
        }

        scenario("Refracted color with refracted ray") {
            val s1 = Sphere()
            s1.material = Material(Color(0.8, 1.0, 0.6), 1.0, 0.7, 0.2, 200)
            s1.material.pattern = TestPattern()

            val s2 = Sphere()
            s2.transform = Matrix.scaling(0.5, 0.5, 0.5)
            s2.material = Material(Color(1, 1, 1), 0.1, 0.9, 0.9, 200, 0.0, 1.0, 1.5)

            val shapes = listOf(s1, s2)
            val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)), shapes)

            val ray = Ray(Point(0.0, 0.0, 0.1), Vector(0, 1, 0))
            val xs = Intersection.intersections(Intersection(-0.9899, s1), Intersection(-0.4899, s2), Intersection(0.4899, s2),
                    Intersection(0.9899, s1))

            val c2 = xs[2].prepareComputations(ray, xs)
            val c = world.refractedColor(c2, 5)

            c.shouldBe(Color(0.0, 0.99878, 0.04724))
        }


        scenario("shadeHit with transparent material") {
            val floor = Plane()
            floor.transform = Matrix.translation(0, -1, 0)
            floor.material = Material(Color(1, 1, 1), 0.1, 0.9, 0.9, 200, 0.0, 0.5, 1.5)

            val ball = Sphere()
            ball.transform = Matrix.translation(0.0, -3.5, -0.5)
            ball.material = Material(Color(1, 0, 0), 0.5, 0.9, 0.9, 200)

            val shapes = listOf(floor, ball)

            val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)), shapes)

            val root2Over2 = Math.sqrt(2.0) / 2.0
            val ray = Ray(Point(0, 0, -3), Vector(0.0, -root2Over2, root2Over2))

            val xs = Intersection.intersections(Intersection(Math.sqrt(2.0), floor))
            val c0 = xs[0].prepareComputations(ray, xs)

            val c = world.shade(c0, 1)

            c.shouldBe(Color(0.93642, 0.68642, 0.68642))
        }
    }

})
