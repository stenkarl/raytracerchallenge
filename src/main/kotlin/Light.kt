

class PointLight(val point:Point, val intensity:Color) {

    override fun equals(other:Any?):Boolean {
        if (other is PointLight) {
            return this.point == other.point && this.intensity == other.intensity
        }
        return false
    }


    companion object {

        fun none() = PointLight(Point(0,0,0), Color(0,0,0))
    }
}
