class Matrix(val width:Int, val height:Int) {

    constructor(width:Int, height:Int, str:String) : this(width, height) {
        val rows = str.split("| |")

        for (row in 0 until height) {
            val rowStr = if (rows[row].startsWith("|")) rows[row].substring(1, rows[row].length) else rows[row]
            val cells = rowStr.trim().split("|")
            for (col in 0 until width) {
                this[row, col] = cells[col].trim().toDouble()
            }
        }

    }

    private val epsilon = 0.00001

    private val matrix:Array<Array<Double>> = Array(height,
            {Array(width, { 0.0 })})


    operator fun get(x:Int, y:Int) = matrix[x][y]

    operator fun set(x:Int, y:Int, value:Double) {
        matrix[x][y] = if (value == -0.0) Math.abs(value) else value
    }

    override fun equals(other: Any?): Boolean {
        if (other is Matrix) {
            if (width != other.width || height != other.height) {
                return false
            }
            for (i in 0 until height)
                for (j in 0 until width)
                    if (!closeEnough(this[i, j], other[i, j]))
                        return false

            return true
        }
        return false
    }

    private fun closeEnough(value:Double, other:Double) =
           Math.abs(value - other) < epsilon


    override fun toString():String {
        var str = ""
        for (i in 0 until width) {
            str += "|"
            for (j in 0 until height) {
                str += "${this[i, j]} "
            }
            str += "|\n"
        }
        return str
    }

    private fun arrayToString(array:Array<Double>):String {
        var str = "["

        array.forEach { i -> str += "$i "}

        str += "]"

        return str
    }

    operator fun times(other:Matrix):Matrix {
        val p = Matrix(width, height)

        for (i in 0 until height) {
            for (j in 0 until width) {
                p[i, j] = rowColProduct(row(i), other.col(j))
            }
        }

        return p
    }

    operator fun times(other:Tuple):Tuple {
        val col = other.asArray()

        return Tuple(rowColProduct(row(0), col), rowColProduct(row(1), col),
                        rowColProduct(row(2), col), rowColProduct(row(3), col))
    }

    private fun rowColProduct(r:Array<Double>, c: Array<Double>):Double {
        val zipped = r.zip(c)

        return zipped.fold(0.0, {
            sum, element -> sum + (element.first * element.second)
        })
    }

    fun row(which:Int) = matrix[which]

    fun col(which:Int):Array<Double> {
        val c = Array(height, { 0.0 })
        for (j in 0 until height) {
            c[j] = this[j, which]
        }
        return c
    }

    fun transpose():Matrix {
        val t = Matrix(width, height)

        for (j in 0 until height) {
            val row = row(j)
            for (i in 0 until row.size) {
                t[i, j] = row[i]
            }
        }

        return t
    }

    fun determinant():Double {
        if (width == 2 && height == 2) {
            return this[0, 0] * this[1, 1] - this[1, 0] * this[0, 1]
        }
        val row = row(0)
        val foldSum = row.foldIndexed(0.0, {
            index, acc, item -> acc + item * cofactor(0, index)
        })
        return foldSum
    }

    fun submatrix(row:Int, col:Int):Matrix {
        val sub = Matrix(width - 1, height - 1)
        var subI = 0
        var subJ = 0
        for (j in 0 until width) {
            subI = 0
            if (j == row) continue
            for (i in 0 until height) {
                if (i != col) {
                    sub[subJ, subI] = this[j, i]
                    subI++
                }
            }
            subJ++
        }
        return sub
    }

    fun minor(row:Int, col:Int) = submatrix(row, col).determinant()

    fun cofactor(row:Int, col:Int) = if ((row + col) % 2 == 0) minor(row, col) else -minor(row, col)

    fun invertible() = determinant() != 0.0

    fun inverse():Matrix {
        val cofactors = Matrix(width, height)

        for (i in 0 until height) {
            for (j in 0 until width) {
                cofactors[i, j] = cofactor(i, j)
            }
        }
        val transposed = cofactors.transpose()
        val det = determinant()

        val inv = Matrix(width, height)
        for (i in 0 until height) {
            for (j in 0 until width) {
                inv[i, j] = transposed[i, j] / det
            }
        }
        return inv
    }

    companion object {

        fun identity(w:Int, h:Int):Matrix {
            val m = Matrix(w, h)
            for(i in 0 until h)
                m[i, i] = 1.0

            return m
        }

        fun identity():Matrix = identity(4, 4)

        fun translation(x:Int, y:Int, z:Int) = translation(x.toDouble(), y.toDouble(), z.toDouble())

        fun translation(x:Double, y:Double, z:Double):Matrix {
            val identity = identity(4, 4)

            identity[0, 3] = x
            identity[1, 3] = y
            identity[2, 3] = z

            return identity
        }

        fun scaling(x:Int, y:Int, z:Int) = scaling(x.toDouble(), y.toDouble(), z.toDouble())

        fun scaling(x:Double, y:Double, z:Double):Matrix {
            val identity = identity(4, 4)

            identity[0, 0] = x
            identity[1, 1] = y
            identity[2, 2] = z

            return identity
        }

        fun rotationX(radians:Double):Matrix {
            val identity = identity(4, 4)

            val cosR = Math.cos(radians)
            val sinR = Math.sin(radians)

            identity[1, 1] = cosR
            identity[2, 2] = cosR
            identity[2, 1] = sinR
            identity[1, 2] = -sinR

            return identity
        }

        fun rotationY(radians:Double):Matrix {
            val identity = identity(4, 4)

            val cosR = Math.cos(radians)
            val sinR = Math.sin(radians)

            identity[0, 0] = cosR
            identity[2, 2] = cosR
            identity[0, 2] = sinR
            identity[2, 0] = -sinR

            return identity
        }

        fun rotationZ(radians:Double):Matrix {
            val identity = identity(4, 4)

            val cosR = Math.cos(radians)
            val sinR = Math.sin(radians)

            identity[0, 0] = cosR
            identity[1, 1] = cosR
            identity[0, 1] = -sinR
            identity[1, 0] = sinR

            return identity
        }

        fun shearing(xy:Int, xz:Int, yx:Int, yz:Int, zx:Int, zy:Int) =
                shearing(xy.toDouble(), xz.toDouble(), yx.toDouble(), yz.toDouble(), zx.toDouble(), zy.toDouble())

        fun shearing(xy:Double, xz:Double, yx:Double, yz:Double, zx:Double, zy:Double):Matrix {
            val identity = identity(4, 4)

            identity[0, 1] = xy
            identity[0, 2] = xz
            identity[1, 0] = yx
            identity[1, 2] = yz
            identity[2, 0] = zx
            identity[2, 1] = zy

            return identity
        }

        fun viewTransform(from:Point, to:Point, up:Vector):Matrix {
            val forward = (to - from).normalize()
            val normalUp = up.normalize()
            val left = forward.cross(normalUp)
            val trueUp = left.cross(forward)
            val orientation = Matrix.identity(4, 4)
            orientation[0, 0] = left.x
            orientation[0, 1] = left.y
            orientation[0, 2] = left.z

            orientation[1, 0] = trueUp.x
            orientation[1, 1] = trueUp.y
            orientation[1, 2] = trueUp.z

            orientation[2, 0] = -forward.x
            orientation[2, 1] = -forward.y
            orientation[2, 2] = -forward.z

            val translation = Matrix.translation(-from.x, -from.y, -from.z)

            return orientation * translation
        }

    }
}
