import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class Lights : FeatureSpec({

    feature("Lights") {
        scenario("A point light has a position and intensity") {
            val intensity = Color(1, 1, 1)
            val point = Point(0,0, 0)

            val light = PointLight(point, intensity)

            light.intensity.shouldBe(intensity)
            light.point.shouldBe(point)
        }
    }
})
