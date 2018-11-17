import java.io.File

class Canvas(val width:Int, val height:Int) {

    val pixels:Array<Array<Color>> = Array(height,
            {Array(width, { Color(0.0, 0.0, 0.0)})})


    fun setPixelAt(x:Int, y:Int, color:Color) {
        if (x < 0 || y < 0 || x > width - 1 || y > height - 1) {
            return
        }
        pixels[y][x] = color
    }

    fun pixelAt(x:Int, y:Int):Color { return pixels[y][x]}

    override fun toString():String {
        var str = ""
        for (row in pixels) {
            str += "["
            for (color in row) {
                str += color.toString() + " "
            }
            str += "]\n"
        }
        return str
    }

    fun toPPM():String {
        val str = StringBuilder("P3\n$width $height\n255\n")
        var rowCount = 0
        for (row in pixels) {
            for (col in 0 until row.size) {
                str.append(row[col].toIntString())
                if (col < row.size - 1) {
                    str.append(" ")
                }
            }
            rowCount++
            str.append("\n")
        }
        return str.toString()
    }

    fun saveToFile() {
        File("out.ppm").bufferedWriter().use { out ->
            out.write(toPPM())
        }
    }
}

fun main(args:Array<String>) {
    println(Canvas(5, 2).saveToFile())
}
