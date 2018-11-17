import io.kotlintest.matchers.plusOrMinus
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class Cameras : FeatureSpec ({

    val epsilon = 0.00001

    feature("Camera") {
        scenario("Constructing a camera") {
            val hsize = 160
            val vsize = 120
            val fov = Math.PI / 2.0

            val c = Camera(hsize, vsize, fov)

            c.hsize.shouldBe(hsize)
            c.vsize.shouldBe(vsize)
            c.fov.shouldBe(fov)

            c.transform.shouldBe(Matrix.identity(4, 4))
        }

        scenario("The pixel size for a horizontal canvas") {
            val c = Camera(200, 125, Math.PI / 2.0)

            c.pixelSize.shouldBe(0.01 plusOrMinus epsilon)
        }

        scenario("The pixel size for a vertical canvas") {
            val c = Camera(125, 200, Math.PI / 2.0)

            c.pixelSize.shouldBe(0.01 plusOrMinus epsilon)
        }
    }

    feature("Ray through the camera") {
        scenario("Construct a ray through the center of the canvas") {
            val c = Camera(201, 101, Math.PI / 2.0)
            val r = c.rayForPixel(100, 50)

            r.origin.shouldBe(Point(0, 0, 0))
            r.direction.shouldBe(Vector(0, 0, -1))
        }

        scenario("Construct a ray through a corner of the canvas") {
            val c = Camera(201, 101, Math.PI / 2.0)
            val r = c.rayForPixel(0, 0)

            r.origin.shouldBe(Point(0, 0, 0))
            r.direction.shouldBe(Vector(0.66519, 0.33259, -0.66851))
        }

        scenario("Construct a ray when the camera is transformed") {
            val c = Camera(201, 101, Math.PI / 2.0)
            c.transform = Matrix.rotationY(Math.PI / 4.0) * Matrix.translation(0, -2, 5)

            val r = c.rayForPixel(100, 50)

            val root2Over2 = Math.sqrt(2.0) / 2.0
            r.origin.shouldBe(Point(0, 2, -5))
            r.direction.shouldBe(Vector(root2Over2, 0.0, -root2Over2))
        }
    }

    feature("Render") {
        scenario("Rendering a world with a camera") {
            val w = World.default()
            val c = Camera(11, 11, Math.PI / 2.0)
            val from = Point(0, 0, -5)
            val to = Point(0, 0, 0)
            val up = Vector(0, 1, 0)

            c.transform = Matrix.viewTransform(from, to, up)

            val image = c.render(w)

            image.saveToFile()

            image.pixelAt(5, 5).shouldBe(Color(0.38066, 0.47583, 0.2855))
        }
    }
})
