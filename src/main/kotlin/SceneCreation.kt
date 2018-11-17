
fun createScene() {
    val floor = Sphere()
    floor.transform = Matrix.scaling(10.0, 0.01, 10.0)
    floor.material = Material(Color(1.0, 0.9, 0.9), 0.1, 0.9, 0.0, 200)

    val leftWall = Sphere()
    leftWall.transform = Matrix.translation(0, 0, 5) * Matrix.rotationY(-Math.PI / 4.0) *
                        Matrix.rotationX(Math.PI / 2.0) * Matrix.scaling(10.0, 0.01, 10.0)
    leftWall.material = floor.material

    val rightWall = Sphere()
    rightWall.transform = Matrix.translation(0, 0, 5) * Matrix.rotationY(Math.PI / 4.0) *
            Matrix.rotationX(Math.PI / 2.0) * Matrix.scaling(10.0, 0.01, 10.0)
    rightWall.material = floor.material

    val middle = Sphere()
    middle.transform = Matrix.translation(-0.5, 1.0, 0.5)
    middle.material = Material(Color(0.1, 1.0, 0.5), 0.1, 0.7, 0.3, 200)

    val right = Sphere()
    right.transform = Matrix.translation(1.5, 0.5, -0.5) * Matrix.scaling(0.5, 0.5, 0.5)
    right.material = Material(Color(0.5, 1.0, 0.1), 0.1, 0.5, 0.0, 100)

    val left = Sphere()
    left.transform = Matrix.translation(-1.5, 0.33, -0.75) * Matrix.scaling(0.33, 0.33, 0.33)
    left.material = Material(Color(1.0, 0.8, 0.1), 0.1, 0.7, 0.3, 200)

    val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)),
                        listOf(floor, leftWall, rightWall, middle, right, left))

    val camera = Camera(1000, 500, Math.PI / 3.0)
    camera.transform = Matrix.viewTransform(Point(0.0, 1.5, -5.0), Point(0, 1, 0), Vector(0, 1, 0))

    val image = camera.render(world)

    image.saveToFile()

    println("Done")
}

fun justAPlane() {
    val floor = Plane()
    floor.material = Material(Color(0.2, 0.6, 1.0), 0.3, 0.9, 0.0, 200)

    val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)),
            listOf(floor))

    val camera = Camera(1000, 500, Math.PI / 3.0)
    camera.transform = Matrix.viewTransform(Point(0.0, 1.5, -5.0), Point(0, 1, 0), Vector(0, 1, 0))

    val image = camera.render(world)

    image.saveToFile()

    println("Done")
}

fun justASphere() {
    val sphere = Sphere()
    //sphere.transform = Matrix.translation(2, 2, 2)

    val white = Color(1, 1,1 )
    val black = Color(0, 0, 0)
    val p = CheckersPattern(white, black)
    p.transform = Matrix.rotationX(Math.PI / 5.0) * Matrix.scaling(0.1, 0.1, 0.1)
    val m = Material()
    m.pattern = p
    sphere.material = m

    val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)),
            listOf(sphere))

    val camera = Camera(1000, 500, Math.PI / 3.0)
    camera.transform = Matrix.viewTransform(Point(0.0, 0.0, -5.0), Point(0, 0, 0), Vector(0, 1, 0))

    val image = camera.render(world)

    image.saveToFile()

    println("Done")
}

fun createSceneWithPlanes() {
    val floor = Plane()
    val white = Color(1, 1,1 )
    val black = Color(0, 0, 0)
    val p = CheckersPattern(white, black)
    val m = Material(Color(0.2, 0.6, 1.0), 0.3, 0.9, 0.0, 200, 0.6, 0.0, 1.0)
    m.pattern = p
    floor.material = m

    val spherePattern = StripePattern(Color(0, 0, 0), Color(1, 1, 1))
    spherePattern.transform = Matrix.scaling(0.4, 0.4, 0.4)
    val sphereTinyPattern = StripePattern(Color(1, 1, 1), Color(0, 0, 1))
    sphereTinyPattern.transform = Matrix.scaling(0.2, 0.2, 0.2)

    val middle = Sphere()
    middle.transform = Matrix.translation(-0.5, 1.0, 0.5) * Matrix.scaling(0.5, 1.0, 0.4)
    val middleP = CheckersPattern(Color(0.0, 0.4, 0.6), Color(0.8, 0.0, 0.2))
    middleP.transform = Matrix.scaling(0.3, 0.3, 0.3)
    val middleM = Material(0.3)
    middleM.pattern = middleP
    middle.material = middleM

    val right = Sphere()
    right.transform = Matrix.translation(1.5, 0.5, 2.0) * Matrix.scaling(0.5, 0.5, 0.5)
    right.material = Material(0.2)
    right.material.pattern = sphereTinyPattern

    val left = Sphere()
    left.transform = Matrix.translation(-1.5, 0.33, -0.75) * Matrix.scaling(0.33, 0.33, 0.33)
    left.material = Material(Color(1.0, 0.8, 0.1), 0.1, 0.7, 0.3, 200, 0.2, 0.0, 1.0)
    left.material.pattern = spherePattern

    val tinySphere = Sphere()
    tinySphere.material = Material(Color(0.3, 0.8, 1.0), 0.1, 0.7, 0.3, 200, 0.0, 0.0, 1.0)
    tinySphere.transform = Matrix.translation(0.1, 0.1, -0.6) * Matrix.scaling(0.1, 0.1, 0.1)

    val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)),
            listOf(floor, middle, right, left, tinySphere))

    val camera = Camera(2200, 1100, Math.PI / 2.0)
    camera.transform = Matrix.viewTransform(Point(1.0, 1.5, 5.0), Point(0, 1, 0), Vector(0, 1, 0))

    val image = camera.render(world)

    image.saveToFile()

    println("Done")
}

fun eclipse() {

    val middle = Sphere()
    middle.transform = Matrix.translation(0.0, 0.5, 0.0)
    middle.material = Material(Color(0.1, 1.0, 0.5), 0.1, 0.7, 0.3, 200)

    val left = Sphere()
    left.transform = Matrix.translation(-1.0, 2.0, -2.0) * Matrix.scaling(0.33, 0.33, 0.33)
    left.material = Material(Color(1.0, 0.8, 0.1), 0.1, 0.7, 0.3, 200)

    val right = Sphere()
    right.transform = Matrix.translation(4.0, -2.0, 4.0) * Matrix.scaling(2.0, 2.0, 2.0)
    right.material = Material(Color(0.3, 0.8, 0.9), 0.1, 0.7, 0.3, 200)

    val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)),
            listOf(middle, left, right))

    val camera = Camera(2000, 2000, Math.PI / 3.0)
    camera.transform = Matrix.viewTransform(Point(0.0, 1.5, -5.0), Point(0, 1, 0), Vector(0, 1, 0))

    val image = camera.render(world)

    image.saveToFile()

    println("Done")
}

fun planets() {

    val mercury = Sphere()
    mercury.transform = Matrix.translation(-8.5, 0.0, 0.0) * Matrix.scaling(0.3383, 0.3383, 0.3383)
    mercury.material = Material(Color(1.0, 1.0, 0.0), 0.1, 0.7, 0.3, 200)

    val venus = Sphere()
    venus.transform = Matrix.translation(-6.0, 0.0, 0.0) * Matrix.scaling(0.949, 0.949, 0.949)
    venus.material = Material(Color(1.0, 0.8, 0.1), 0.1, 0.7, 0.3, 200)

    val earth = Sphere()
    earth.transform = Matrix.translation(-2.0, -1.0, 0.0) * Matrix.scaling(1.0, 1.0, 1.0)
    earth.material = Material(Color(0.3, 0.8, 0.9), 0.1, 0.7, 0.3, 200)

    val mars = Sphere()
    mars.transform = Matrix.translation(2.0, 2.0, 0.0) * Matrix.scaling(0.532, 0.532, 0.532)
    mars.material = Material(Color(1.0, 0.3, 0.0), 0.1, 0.7, 0.3, 200)

    val neptune = Sphere()
    neptune.transform = Matrix.translation(8.0, 0.0, 0.0) * Matrix.scaling(3.88, 3.88, 3.88)
    neptune.material = Material(Color(0.1, 0.4, 1.0), 0.1, 0.7, 0.1, 100)

    val pluto = Sphere()
    pluto.transform = Matrix.translation(5.2, 3.2, -1.6) * Matrix.scaling(0.186, 0.186, 0.186)
    pluto.material = Material(Color(0.8, 1.0, 1.0), 0.1, 0.7, 0.1, 100)

    val world = World(PointLight(Point(-40.0, 0.5, -0.2), Color(1, 1, 1)),
            listOf(mercury, venus, earth, mars, neptune, pluto))

    val camera = Camera(200, 100, Math.PI / 2.0)
    camera.transform = Matrix.viewTransform(Point(0.0, 0.0, -10.0), Point(0, 1, 0), Vector(0, 1, 0))

    val image = camera.render(world)

    image.saveToFile()

    println("Done")
}

fun reflective() {
    val shape = Plane()
    shape.material = Material(0.5)
    shape.transform = Matrix.translation(0, -1, 0)
    val s1 = Sphere()
    s1.material = Material(Color(0.8, 1.0, 0.6), 0.1, 0.7, 0.2, 200)

    val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)), listOf(s1, shape))

    val camera = Camera(1000, 500, Math.PI / 3.0)
    camera.transform = Matrix.viewTransform(Point(0.0, 0.0, -10.0), Point(0, 1, 0), Vector(0, 1, 0))

    val image = camera.render(world)

    image.saveToFile()
}

private fun createFloor():Shape {
    val floor = Plane()
    val white = Color(1, 1,1 )
    val black = Color(0, 0, 0)
    val p = CheckersPattern(white, black)
    val m = Material(Color(1, 1, 1), 0.3, 0.9, 0.0, 200, 0.0, 0.0, 1.0)
    m.pattern = p
    floor.material = m
    floor.transform = Matrix.translation(0, -1, 0)

    return floor
}

private fun createBack():Shape {
    val floor = Plane()
    val white = Color(1, 1,1 )
    val black = Color(0, 0, 0)
    val p = StripePattern(white, black)
    val m = Material(Color(1, 1, 1), 0.3, 0.9, 0.0, 200, 0.0, 0.0, 1.0)
    p.transform = Matrix.rotationY(Math.PI / 4.0)
    m.pattern = p
    floor.material = m
    floor.transform = Matrix.translation(0, 0, 20) * Matrix.rotationX(Math.PI / 2.0)

    return floor
}

val wallDist = 15

private fun createBack(c:Color):Shape = createWall(c, Matrix.translation(0, 0, wallDist) * Matrix.rotationX(Math.PI / 2.0))

private fun createFront(c:Color):Shape = createWall(c, Matrix.translation(0, 0, -wallDist) * Matrix.rotationX(Math.PI / 2.0))

private fun createLeft(c:Color):Shape = createWall(c, Matrix.translation(-wallDist, 0, 0) * Matrix.rotationZ(Math.PI / 2.0))

private fun createRight(c:Color):Shape = createWall(c, Matrix.translation(wallDist, 0, 0) * Matrix.rotationZ(Math.PI / 2.0))

private fun createLid(c:Color):Shape {
    val floor = Plane()
    val white = Color(1, 1,1 )
    val black = Color(0, 0, 0)
    val p = CheckersPattern(white, black)
    val m = Material(Color(1, 1, 1), 0.3, 0.9, 0.0, 200, 0.0, 0.0, 1.0)
    m.pattern = p
    floor.material = m
    floor.transform = Matrix.translation(0, wallDist, 0)

    return floor
}

private fun createWall(c:Color, transform:Matrix):Shape {
    val plane = Plane()

    val m = Material(c, 0.3, 0.9, 0.0, 200, 0.0, 0.0, 1.0)
    plane.material = m
    plane.transform = transform

    return plane
}

private fun createCenterSphere():Shape {
    val s1 = Sphere()
    s1.material = Material(Color(1.0, 1.0, 1.0), 0.1, 0.7, 0.2, 200, 1.0, 0.0, 1.0)
    val scale = 1.6
    s1.transform = Matrix.translation(0.0, 0.6, 0.0) * Matrix.scaling(scale, scale, scale)
    return s1
}

private fun createMirrorSphere(p:Point, scale:Double):Shape {
    val s1 = Sphere()
    s1.material = Material(Color(1.0, 1.0, 1.0), 0.1, 0.7, 0.2, 200, 1.0, 0.0, 1.0)
    s1.transform = Matrix.translation(p.x, p.y, p.z) * Matrix.scaling(scale, scale, scale)
    return s1
}

private fun createMarble(x:Double, z:Double, c:Color):Shape {
    //println("$x, $z, $c")
    val s = Sphere()
    s.material = Material(c, 0.1, 0.7, 0.2, 200, 0.0, 0.0, 1.0)
    s.transform = Matrix.translation(x, -0.9, z) * Matrix.scaling(0.1, 0.1, 0.1)

    return s
}

private fun createMarbles(num:Int):List<Shape> {
    val list = mutableListOf<Shape>()
    for (i in 0 until num) {
        val x = Math.random() * 12 - 6
        val z = Math.random() * 30 - 10
        val s = createMarble(x, z, Color(Math.random(), Math.random(), Math.random()))
        list.add(s)
    }

    return list
}

fun lotsOfBalls() {
    val floor = createFloor()
    val back = createBack()
    val center = createCenterSphere()

    val marbles = createMarbles(200)
    val shapes = mutableListOf(floor, back)
    shapes.addAll(marbles)
    shapes.add(center)

    val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)), shapes)

    //val camera = Camera(2000, 1750, Math.PI / 4.0)
    val camera = Camera(1000, 875, Math.PI / 4.0)

    //camera.transform = Matrix.viewTransform(Point(0.0, 1.0, -10.0), Point(0, 0, 0), Vector(0, 1, 0))
    camera.transform = Matrix.viewTransform(Point(10.0, 1.0, 0.0), Point(0, 0, 0), Vector(0, 1, 0))

    val image = camera.render(world)

    image.saveToFile()
}

fun mirrorBalls() {
    val floor = createFloor()
    val frontBackColor = Color(0.2, 0.5, 1.0)
    val leftRightColor = Color(155.0 / 255, 26.0 / 255, 27.0 / 255)
    val back = createBack(frontBackColor)
    val front = createFront(frontBackColor)
    val left = createLeft(leftRightColor)
    val right = createRight(leftRightColor)
    val lid = createLid(Color(0, 0, 0))

    val shapes = mutableListOf(floor, back, front, left, right, lid)
    shapes.add(createMirrorSphere(Point(0.0, 3.0, 6.0), 4.0))
    shapes.add(createMirrorSphere(Point(-2.8, 1.0, -1.5), 2.0))
    shapes.add(createMirrorSphere(Point(3.0, 1.0, -1.0), 2.0))

    shapes.add(createMirrorSphere(Point(-1.6, -0.5, -3.7), 0.5))
    shapes.add(createMirrorSphere(Point(-0.4, -0.4, -6.2), 0.6))

    shapes.add(createMirrorSphere(Point(2.5, -0.55, -4.8), 0.45))
    shapes.add(createMirrorSphere(Point(-0.8, -0.7, 2.0), 0.3))


    shapes.add(createMirrorSphere(Point(-7.0, 0.0, -1.0), 1.0))
    shapes.add(createMirrorSphere(Point(-4.0, -0.5, -4.5), 0.5))

    shapes.add(createMirrorSphere(Point(6.0, 0.7, -4.0), 1.7))

    shapes.add(createMirrorSphere(Point(1.0, -0.4, -2.0), 0.6))
    shapes.add(createMirrorSphere(Point(4.0, -0.6, -5.0), 0.4))

    shapes.add(createMirrorSphere(Point(0.5, -0.7, -4.0), 0.3))
    shapes.add(createMirrorSphere(Point(1.0, -0.4, -2.0), 0.6))


    shapes.add(createMirrorSphere(Point(4.0, 0.0, -13.0), 1.0))
    shapes.add(createMirrorSphere(Point(-2.3, -0.2, -11.0), 0.8))


    val world = World(PointLight(Point(0, 10, -10), Color(1, 1, 1)), shapes)

    //val camera = Camera(2000, 1750, Math.PI / 4.0)
    //val camera = Camera(1920, 1080, 2.0)
    //val camera = Camera(1920, 1080, Math.PI/8.0)
    val camera = Camera(3840, 2160, 1.6)

    //camera.transform = Matrix.viewTransform(Point(0.0, 1.0, -10.0), Point(0, 0, 0), Vector(0, 1, 0))
    camera.transform = Matrix.viewTransform(Point(0.0, 1.0, -9.0), Point(0, 0, 0), Vector(0, 1, 0))

    val image = camera.render(world)

    image.saveToFile()
}

fun refractionTest() {
    val floor = Plane()
    floor.transform = Matrix.translation(0, -1, 0)
    floor.material = Material(Color(1.0, 1.0, 1.0), 0.3, 0.3, 0.2, 200, 0.0, 0.0, 1.0)

    val ball = Sphere()
    ball.transform = Matrix.translation(0.0, -1.5, -0.5)
    ball.material = Material(Color(1, 0, 0), 0.1, 0.3, 0.2, 200, 0.0, 0.0, 1.5)
    val shapes = listOf(floor, ball)

    val world = World(PointLight(Point(-10, 10, -10), Color(1, 1, 1)), shapes)

    val camera = Camera(400, 300, 1.6)

    camera.transform = Matrix.viewTransform(Point(0.0, 1.0, -9.0), Point(0.0, -3.0, 0.0), Vector(0, 1, 0))

    val image = camera.render(world)

    image.saveToFile()
}

fun main(args: Array<String>) {
    refractionTest()
}
