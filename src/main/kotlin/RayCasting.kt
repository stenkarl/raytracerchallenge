

val WIDTH = 1000
val HEIGHT = 1000
val COLOR = Color(1.0, 0.0 ,0.0)

fun castRays() {
    val s = Sphere()
    val m = Material(Color(4.0, 0.6, 1.0), 0.01, 0.3, 0.9, 50)
    s.material = m

    val scale = 200
    s.transform = Matrix.translation(WIDTH / 2, HEIGHT / 2, 0) * Matrix.scaling(scale, scale, scale)

    val lightPosition = Point(-10, 10, -10)
    val lightColor = Color(1, 1, 1)
    val light = PointLight(lightPosition, lightColor)

    val c = Canvas(WIDTH, HEIGHT)

    for (x in 0 until WIDTH) {
        for (y in 0 until HEIGHT) {
            val r = Ray(Point(x, y, -5), Vector(0, 0, 1))

            val xs = s.intersect(r)
            if (xs.isNotEmpty()) {
                val hit = Intersection.hit(xs)
                if (hit != null) {
                    val point = r.position(hit.t)
                    val normal = hit.obj.normalAt(point)
                    val eye = r.direction * -1.0

                    val color = hit.obj.material.lighting(light, hit.obj, point, eye, normal)
                    c.setPixelAt(x, y, color)
                }
            }
        }
    }

    c.saveToFile()
}

fun main(args: Array<String>) {
    castRays()
}
