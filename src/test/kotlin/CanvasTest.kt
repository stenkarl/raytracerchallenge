import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class CanvasTest : FeatureSpec({

    feature("Canvas") {

        scenario("Creating a canvas") {
            val width = 10
            val height = 20
            val c = Canvas(width, height)

            c.width.shouldBe(width)
            c.height.shouldBe(height)

            for (i in 0 until width) {
                for (j in 0 until height) {
                    c.pixelAt(i, j).shouldBe(Color(0.0, 0.0, 0.0))
                }
            }
        }

        scenario("Writing pixels to a canvas") {
            val canvas = Canvas(10, 20)
            val color = Color(1.0, 0.0, 0.0)

            canvas.setPixelAt(2, 3, color)

            canvas.pixelAt(2, 3).shouldBe(color)

        }

    }

    feature ("File Export") {
        scenario("Constructing the PPM pixel data") {
            val c = Canvas(5, 3)
            val c1 = Color(1.5, 0.0, 0.0)
            val c2 = Color(0.0, 0.5, 0.0)
            val c3 = Color(-0.5, 0.0, 1.0)

            c.setPixelAt(0, 0, c1)
            c.setPixelAt(2, 1, c2)
            c.setPixelAt(4, 2, c3)

            val ppm = c.toPPM()

            ppm.shouldBe(
                    """P3
5 3
255
255 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 128 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 255
"""
            )
        }

        scenario("Splitting long lines in PPM files") {
            val width = 10
            val height = 2
            val c = Canvas(width, height)

            for (i in 0 until width) {
                for (j in 0 until height) {
                    c.setPixelAt(i, j, Color(1.0, 0.8, 0.6))
                }
            }

            val ppm = c.toPPM()

            ppm.shouldBe("""P3
10 2
255
255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204 153
255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204 153
""")
        }

    }
})
