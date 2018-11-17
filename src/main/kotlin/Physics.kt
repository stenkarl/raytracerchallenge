
class Physics {

    val gravity = Vector(0.0, -0.1, 0.0)
    val wind = Vector(0.0, 0.0, 0.0)

    var position = Point(0.0, 1.0, 0.0)
    var velocity = Vector(1.0, 1.8, 0.0).normalize() * 11.25


    fun tick() {
        position = position + velocity
        velocity = velocity + gravity + wind
    }

    fun run() {
        while (position.y > 0) {
            tick()
            println(position)
        }
    }

    fun plot() {
        val c = Canvas(900, 550)
        val color = Color(1.0, 0.0, 0.0)
        while (position.y > 0) {
            tick()
            c.setPixelAt(position.x.toInt(), c.height - position.y.toInt(), color)
            println(position)

        }
        //println(c.toPPM())
        c.saveToFile()
    }

}

fun main(args: Array<String>) {
    Physics().plot()
}
