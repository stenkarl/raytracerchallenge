class Camera(val hsize:Int, val vsize:Int, val fov:Double) {

    var transform = Matrix.identity(4, 4)

    private val halfView = Math.tan(fov / 2.0)
    private val aspectRatio = hsize.toDouble() / vsize.toDouble()

    private val halfWidth = if (aspectRatio >= 1) halfView else halfView * aspectRatio
    private val halfHeight = if (aspectRatio >= 1) halfView / aspectRatio else halfView

    val pixelSize = (halfWidth * 2.0) / hsize

    fun rayForPixel(px:Int, py:Int):Ray {
        val xOffset = (px + 0.5) * pixelSize
        val yOffset = (py + 0.5) * pixelSize

        val worldX = halfWidth - xOffset
        val worldY = halfHeight - yOffset

        val pixel = (transform.inverse() * Point(worldX, worldY, -1.0)).asPoint()
        val origin = (transform.inverse() * Point(0, 0, 0)).asPoint()
        val direction = (pixel - origin).normalize()

        return Ray(origin, direction)
    }

    fun render(w:World):Canvas {
        val image = Canvas(hsize, vsize)
        for (y in 0 until vsize) {
            for (x in 0 until hsize) {
                val r = rayForPixel(x, y)
                val color = w.colorAt(r, 4)
                image.setPixelAt(x, y, color)
            }
            println ("${(y / (vsize * 1.0)) * 100.0} percent complete")
        }
        return image
    }
}
