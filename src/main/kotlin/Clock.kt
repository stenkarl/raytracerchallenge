
val canvas = Canvas(100, 100)
val color = Color(0.0, 0.8, 0.4)

fun addHour(rot:Double) {
    val p = Point(0, 0, 0)
    val r = Matrix.rotationZ(rot)
    val t = Matrix.translation(20, 0, 0)

    val p2 = r * (t * p)

    val p3 = Matrix.translation(50, 50, 0) * p2


    canvas.setPixelAt(p3.x.toInt(), p3.y.toInt(), color)
}

fun main(args: Array<String>) {

    for (i in 0 until 12) {
        addHour(i * (Math.PI / 6.0))
    }

    canvas.saveToFile()
}
