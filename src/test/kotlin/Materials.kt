import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class Materials : FeatureSpec({

    feature("Materials") {
        scenario("The default material") {
            val m = Material()

            m.color.shouldBe(Color(1, 1, 1))
            m.ambient.shouldBe(0.1)
            m.diffuse.shouldBe(0.9)
            m.specular.shouldBe(0.9)
            m.shininess.shouldBe(200)
        }
    }

    feature("Lighting") {
        scenario("Lighting with the eye between the light and the surface") {
            val m = Material()
            val position = Point(0, 0, 0)

            val eye = Vector(0, 0, -1)
            val normal = Vector(0, 0, -1)
            val light = PointLight(Point(0, 0, -10), Color(1, 1, 1))

            val result = m.lighting(light, Sphere(), position, eye, normal)

            result.shouldBe(Color(1.9, 1.9, 1.9))
        }

        scenario("Lighting with the eye between light and surface, eye offset 45 degrees") {
            val m = Material()
            val position = Point(0, 0, 0)
            val root2Over2 = Math.sqrt(2.0) / 2.0

            val eye = Vector(0.0, root2Over2, -root2Over2)
            val normal = Vector(0, 0, -1)
            val light = PointLight(Point(0, 0, -10), Color(1, 1, 1))

            val result = m.lighting(light, Sphere(), position, eye, normal)

            result.shouldBe(Color(1.0, 1.0, 1.0))
        }

        scenario("Lighting with eye opposite surface, light offset 45 degrees") {
            val m = Material()
            val position = Point(0, 0, 0)

            val eye = Vector(0, 0, -1)
            val normal = Vector(0, 0, -1)
            val light = PointLight(Point(0, 10, -10), Color(1, 1, 1))

            val result = m.lighting(light, Sphere(), position, eye, normal)
            val resultColor = 0.7364

            result.shouldBe(Color(resultColor, resultColor, resultColor))
        }

        scenario("Lighting with eye in the path of the reflection vector") {
            val m = Material()
            val position = Point(0, 0, 0)
            val root2Over2 = Math.sqrt(2.0) / 2.0

            val eye = Vector(0.0, -root2Over2, -root2Over2)
            val normal = Vector(0, 0, -1)
            val light = PointLight(Point(0, 10, -10), Color(1, 1, 1))

            val result = m.lighting(light, Sphere(), position, eye, normal)
            val resultColor = 1.6364

            result.shouldBe(Color(resultColor, resultColor, resultColor))
        }

        scenario("Lighting with the light behind the surface") {
            val m = Material()
            val position = Point(0, 0, 0)

            val eye = Vector(0, 0, -1)
            val normal = Vector(0, 0, -1)
            val light = PointLight(Point(0, 10, 10), Color(1, 1, 1))

            val result = m.lighting(light, Sphere(), position, eye, normal)
            val resultColor = 0.1

            result.shouldBe(Color(resultColor, resultColor, resultColor))
        }
    }

    feature("Shadows") {
        scenario("Lighting with the surface in shadow") {
            val m = Material()
            val position = Point(0, 0, 0)

            val eyev = Vector(0, 0, -1)
            val normalv = Vector(0, 0, -1)
            val light = PointLight(Point(0, 0, -10), Color(1, 1, 1))

            val result = m.lighting(light, Sphere(), position, eyev, normalv, true)

            result.shouldBe(Color(0.1, 0.1, 0.1))
        }

    }

    feature("Patterns") {
        scenario("Lighting with a pattern applied") {
            val white = Color(1, 1,1 )
            val black = Color(0, 0, 0)
            val p = StripePattern(white, black)
            val m = Material(Color(0, 0, 0), 1.0, 0.0, 0.0, 0, 0.0, 0.0, 1.0)
            m.pattern = p
            val eyeV = Vector(0, 0, -1)
            val normalV = Vector(0, 0, -1)
            val light = PointLight(Point(0, 0, -10), Color(1, 1, 1))

            val c1 = m.lighting(light, Sphere(), Point(0.9, 0.0, 0.0), eyeV, normalV, false)
            val c2 = m.lighting(light, Sphere(), Point(1.1, 0.0, 0.0), eyeV, normalV, false)

            c1.shouldBe(white)
            c2.shouldBe(black)
        }
    }

    feature("Reflections") {
        scenario("Reflectivity for the default material") {
            val m = Material()

            m.reflective.shouldBe(0.0)
        }

        scenario("Precomputing the reflection vector") {
            val shape = Plane()

            val root2Over2 = Math.sqrt(2.0) / 2.0
            val ray = Ray(Point(0, 1, -1), Vector(0.0, -root2Over2, root2Over2))
            val hit = Intersection(Math.sqrt(2.0), shape)

            val comps = hit.prepareComputations(ray)

            comps.reflect.shouldBe(Vector(0.0, root2Over2, root2Over2))
        }
    }

    feature("Refraction") {
        scenario("Transparency and Refractive Index for the default material") {
            val m = Material()

            m.transparency.shouldBe(0.0)
            m.refractiveIndex.shouldBe(1.0)
        }
    }
})
