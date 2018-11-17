import arrow.core.identity
import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.FeatureSpec

class MatrixTest : FeatureSpec ({

    feature("Matrices") {

        scenario("Constructing and inspecting a 4x4 matrix") {
            val content = "|1|2|3|4| | 5.5| 6.5| 7.5| 8.5| | 9 | 10 | 11 | 12 | | 13.5 | 14.5 | 15.5 | 16.5 |"
            val m = Matrix(4, 4, content)

            m[0, 0].shouldBe(1.0)
            m[0, 3].shouldBe(4.0)
            m[1, 0].shouldBe(5.5)
            m[1, 2].shouldBe(7.5)
            m[2, 2].shouldBe(11.0)
            m[3, 0].shouldBe(13.5)
            m[3, 2].shouldBe(15.5)
        }

        scenario("A 2x2 matrix ought to be representable") {
            val twoByTwo = "| -3 | 5 | | 1|-2|"
            val m = Matrix(2, 2, twoByTwo)

            m[0, 0].shouldBe(-3.0)
            m[0, 1].shouldBe(5.0)
            m[1, 0].shouldBe(1.0)
            m[1, 1].shouldBe(-2.0)
        }

        scenario("A 3x3 matrix ought to be representable") {
            val threeByThree = "|-3| 5| 0| | 1|-2|-7| |0|1|1|"
            val m = Matrix(3, 3, threeByThree)

            m[0, 0].shouldBe(-3.0)
            m[1, 1].shouldBe(-2.0)
            m[2, 2].shouldBe(1.0)
        }

        scenario("Matrix equality with identical matrices") {
            val aStr = "|1|2|3|4| |2|3|4|5| |3|4|5|6| |4|5|6|7|"
            val bStr = "|1|2|3|4| |2|3|4|5| |3|4|5|6| |4|5|6|7|"
            val a = Matrix(4, 4, aStr)
            val b = Matrix(4, 4, bStr)

            a.shouldBe(b)
        }

        scenario("Matrix equality with different matrices") {
            val aStr = "|1|2|3|4| |2|3|4|5| |3|4|5|6| |4|5|6|7|"
            val bStr = "|0|2|3|4| |2|3|4|5| |3|4|5|6| |4|5|6|7|"
            val a = Matrix(4, 4, aStr)
            val b = Matrix(4, 4, bStr)

            a.shouldNotBe(b)
        }
    }

    feature("Matrix Multiplication") {

        scenario ("Multiplying two matrices") {
            val a = Matrix(4, 4, "|1|2|3|4| |2|3|4|5| |3|4|5|6| |4|5|6|7|")
            val b = Matrix(4, 4, "|0|1|2|4| |1|2|4|8| |2|4|8|16| |4|8|16|32|")
            val product = Matrix(4, 4,
                            "|24|49| 98|196| | 31 | 64 | 128 | 256 | | 38 | 79 | 158 | 316 | | 45 | 94 | 188 | 376|")

            (a * b).shouldBe(product)
        }

        scenario ("A matrix multiplied by a tuple") {
            val m = Matrix(4, 4, "|1|2|3|4| |2|4|4|2| |8|6|4|1| |0|0|0|1|")
            val t = Tuple(1.0, 2.0, 3.0, 1.0)

            (m * t).shouldBe(Tuple(18.0, 24.0, 33.0, 1.0))
        }

    }

    feature("Matrix Identity") {
        scenario("Multiplying a matrix by the identity") {
            val a = Matrix(4, 4, "|0|1| 2| 4| |1|2| 4| 8| |2|4| 8|16| | 4 | 8 | 16 | 32 |")
            val identity = Matrix(4, 4, "|1|0|0|0| |0|1|0|0| |0|0|1|0| |0|0|0|1|")

            (a * identity).shouldBe(a)
        }

        scenario("Multiplying the identity by the matrix") {
            val a = Matrix(4, 4, "|0|1| 2| 4| |1|2| 4| 8| |2|4| 8|16| | 4 | 8 | 16 | 32 |")
            val identity = Matrix(4, 4, "|1|0|0|0| |0|1|0|0| |0|0|1|0| |0|0|0|1|")

            (identity * a).shouldBe(a)
        }

        scenario("Multiplying identity by a tuple") {
            val a = Tuple(1.0, 2.0, 3.0, 4.0)
            val identity = Matrix(4, 4, "|1|0|0|0| |0|1|0|0| |0|0|1|0| |0|0|0|1|")

            (identity * a).shouldBe(a)
        }

        scenario("Identity is commutative") {
            val a = Matrix(4, 4, "|1.0|0.0|0.0|0.0| " +
                    "|0.0|1.0|0.0|0.0| " +
                    "|0.0|0.0|1.0|-8.0| " +
                    "|0.0|0.0|0.0|1.0|")

            val identity = Matrix.identity(4, 4)

            (a * identity).shouldBe(identity * a)
        }
    }

    feature ("Matrix Transposition") {
        scenario ("Transposing a matrix") {
            val a = Matrix(4, 4, "|0|9|3|0| |9|8|0|8| |1|8|5|3| |0|0|5|8|")
            val t = Matrix(4, 4, "|0|9|1|0| |9|8|8|0| |3|0|5|5| |0|8|3|8|")

            a.transpose().shouldBe(t)
        }

        scenario("Transposing the identity matrix") {
            val identity = Matrix(4, 4, "|1|0|0|0| |0|1|0|0| |0|0|1|0| |0|0|0|1|")

            identity.transpose().shouldBe(identity)
        }
    }

    feature("Getting rows and columns") {
        scenario("row() should return the specified row") {
            val a = Matrix(4, 4, "|1|2|3|4| |8|3|4|5| |9|4|5|6| |4|5|6|7|")

            a.row(0).shouldBe(doubleArrayOf(1.0, 2.0, 3.0, 4.0))
        }

        scenario("col() should return the specified column") {
            val a = Matrix(4, 4, "|1|2|3|4| |2|3|4|5| |3|4|5|6| |4|5|6|7|")

            a.col(1).shouldBe(doubleArrayOf(2.0, 3.0, 4.0, 5.0))
        }

        scenario("col() should return the specified column when rows and cols are different") {
            val a = Matrix(4, 4, "|1|2|3|4| |5|6|7|8| |9|10|11|12| |13|14|15|16|")

            a.col(1).shouldBe(doubleArrayOf(2.0, 6.0, 10.0, 14.0))
        }
    }

    feature("Inverse") {
        scenario("Calculating the determinant of a 2x2 matrix") {
            val a = Matrix(2, 2, "| 1|5| | -3 | 2 |")

            a.determinant().shouldBe(17.0)
        }

        scenario("A submatrix of a 3x3 matrix is a 2x2 matrix") {
            val a = Matrix(3, 3, "| 1|5| 0| |-3|2| 7| | 0|6|-3|")
            val sub = Matrix(2, 2, "| -3 | 2 | | 0|6|")

            a.submatrix(0, 2).shouldBe(sub)
        }

        scenario("A submatrix of a 4x4 matrix is a 3x3 matrix") {
            val a = Matrix(4, 4, "|-6| 1| 1| 6| |-8| 5| 8| 6| |-1| 0| 8| 2| |-7| 1|-1| 1|")
            val sub = Matrix(3, 3, "|-6| 1|6| |-8| 8|6| | -7 | -1 | 1 |")

            a.submatrix(2, 1).shouldBe(sub)
        }

        scenario("Calculating a minor of a 3x3 matrix") {
            val a = Matrix(3, 3, "|3|5|0| | 2|-1|-7| | 6|-1| 5|")
            val b = a.submatrix(1, 0)

            b.determinant().shouldBe(25.0)

            a.minor(1, 0).shouldBe(25.0)
        }

        scenario("Calculating a cofactor of a 3x3 matrix") {
            val a = Matrix(3, 3, "|3|5|0| | 2|-1|-7| | 6|-1| 5|")

            a.minor(0, 0).shouldBe(-12.0)
            a.cofactor(0, 0).shouldBe(-12.0)
            a.minor(1, 0).shouldBe(25.0)
            a.cofactor(1, 0).shouldBe(-25.0)
        }

        scenario("Calculating the determinant of a 3x3 matrix") {
            val a = Matrix(3, 3, "|1|2|6| |-5| 8|-4| |2|6|4|")

            a.cofactor(0, 0).shouldBe(56.0)
            a.cofactor(0, 1).shouldBe(12.0)
            a.cofactor(0, 2).shouldBe(-46.0)

            a.determinant().shouldBe(-196.0)
        }

        scenario("Calculating the determinant of a 4x4 matrix") {
            val a = Matrix(4, 4, "|-2|-8| 3| 5| |-3| 1| 7| 3| | 1| 2|-9| 6| |-6| 7| 7|-9|")

            a.cofactor(0, 0).shouldBe(690.0)
            a.cofactor(0, 1).shouldBe(447.0)
            a.cofactor(0, 2).shouldBe(210.0)
            a.cofactor(0, 3).shouldBe(51.0)
            a.determinant().shouldBe(-4071.0)
        }

        scenario("Testing an invertible matrix for invertibility") {
            val a = Matrix(4, 4, "|6|4|4|4| |5|5|7|6| | 4|-9| 3|-7| | 9| 1| 7|-6|")

            a.determinant().shouldBe(-2120.0)
            a.invertible().shouldBeTrue()
        }

        scenario("Testing a non-invertible matrix for invertibility") {
            val a = Matrix(4, 4, "|-4| 2|-2|-3| |9|6|2|6| | 0|-5| 1|-5| |0|0|0|0|")

            a.determinant().shouldBe(0.0)
            a.invertible().shouldBeFalse()
        }

        scenario("Calculating the inverse of a matrix") {
            val a = Matrix(4, 4, "|-5| 2| 6|-8| | 1|-5| 1| 8| | 7| 7|-6|-7| | 1|-3| 7| 4|")
            val b = a.inverse()
            val det = a.determinant()
            val aInv = Matrix(4, 4,
                        "| 0.21805 | 0.45113 | 0.24060 | -0.04511 | " +
                            "| -0.80827 | -1.45677 | -0.44361 | 0.52068 | " +
                            "| -0.07895 | -0.22368 | -0.05263 | 0.19737 | " +
                            "| -0.52256 | -0.81391 | -0.30075 | 0.30639 |")

            det.shouldBe(532.0)
            a.cofactor(2,3).shouldBe(-160.0)
            b[3, 2].shouldBe(-160.0/ det)
            a.cofactor(3, 2).shouldBe(105.0)
            b[2, 3].shouldBe(105.0 / det)

            b.shouldBe(aInv)
        }

        scenario("Calculating the inverse of another matrix") {
            val a = Matrix(4, 4, "| 8|-5| 9| 2| |7|5|6|1| |-6| 0| 9| 6| |-3| 0|-9|-4|")
            val aInv = Matrix(4, 4,
                            "| -0.15385 | -0.15385 | -0.28205 | -0.53846 | " +
                                    "| -0.07692 | 0.12308 | 0.02564 | 0.03077 | " +
                                    "| 0.35897 | 0.35897 | 0.43590 | 0.92308 | " +
                                    "| -0.69231 | -0.69231 | -0.76923 | -1.92308 |")

            a.inverse().shouldBe(aInv)
        }

        scenario("Calculating the inverse of a third matrix") {
            val a = Matrix(4, 4, "|9|3|0|9| | -5 | -2 | -6 | -3 | |-4| 9| 6| 4| |-7| 6| 6| 2|")
            val aInv = Matrix(4, 4,
                    "| -0.04074 | -0.07778 | 0.14444 | -0.22222 | " +
                            "| -0.07778 | 0.03333 | 0.36667 | -0.33333 | " +
                            "| -0.02901 | -0.14630 | -0.10926 | 0.12963 | " +
                            "|  0.17778 |  0.06667 | -0.26667 |  0.33333 |")

            a.inverse().shouldBe(aInv)
        }

        scenario("Multiplying a product by its inverse") {
            val a = Matrix(4, 4, "| 3|-9| 7| 3| | 3|-8| 2|-9| |-4| 4| 4| 1| |-6| 5|-1| 1|")
            val b = Matrix(4, 4, "|8|2|2|2| | 3|-1| 7| 0| |7|0|5|4| | 6|-2| 0| 5|")

            val c = a * b

            (c * b.inverse()).shouldBe(a)
        }
    }

    feature("View Transforms") {
        scenario("The transformation matrix for the default orientation") {
            val from = Point(0, 0, 0)
            val to = Point(0, 0, -1)
            val up = Vector(0, 1, 0)

            val t = Matrix.viewTransform(from, to, up)

            t.shouldBe(Matrix.identity(4, 4))
        }

        scenario("A view transformation matrix looking in positive z direction") {
            val from = Point(0, 0, 0)
            val to = Point(0, 0, 1)
            val up = Vector(0, 1, 0)

            val t = Matrix.viewTransform(from, to, up)

            t.shouldBe(Matrix.scaling(-1, 1, -1))
        }

        scenario("The view transformation moves the world") {
            val from = Point(0, 0, 8)
            val to = Point(0, 0, 0)
            val up = Vector(0, 1, 0)

            val t = Matrix.viewTransform(from, to, up)

            t.shouldBe(Matrix.translation(0, 0, -8))
        }

        scenario("An arbitrary view transformation") {
            val from = Point(1, 3, 2)
            val to = Point(4, -2, 8)
            val up = Vector(1, 1, 0)

            val view = Matrix(4, 4, "| -0.50709 | 0.50709 |  0.67612 | -2.36643 | " +
                    "|  0.76772 | 0.60609 |  0.12122 | -2.82843 | " +
                    "| -0.35857 | 0.59761 | -0.71714 |  0.00000 | " +
                    "|  0.00000 | 0.00000 |  0.00000 |  1.00000 |")

            val t = Matrix.viewTransform(from, to, up)

            t.shouldBe(view)
        }
    }

})
