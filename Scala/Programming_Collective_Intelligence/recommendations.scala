
object Recommendations {

    val critics = Map(
        "Lisa Rose" -> Map(
            "Lady in the Water" -> 2.5,
            "Snakes on a Plane" -> 3.5,
            "Just My Luck" -> 3.0,
            "Superman Returns" -> 3.5,
            "You, Me and Dupree" -> 2.5,
            "The Night Listener" -> 3.0
        ),
        "Gene Seymour" -> Map(
            "Lady in the Water" -> 3.0,
            "Snakes on a Plane" -> 3.5,
            "Just My Luck" -> 1.5,
            "Superman Returns" -> 5.0,
            "The Night Listener" -> 3.0,
            "You, Me and Dupree" -> 3.5
        ),
        "Michael Phillips" -> Map(
            "Lady in the Water" -> 2.5,
            "Snakes on a Plane" -> 3.0,
            "Superman Returns" -> 3.5,
            "The Night Listener" -> 4.0
        ),
        "Claudia Puig" -> Map(
            "Snakes on a Plane" -> 3.5,
            "Just My Luck" -> 3.0,
             "The Night Listener" -> 4.5,
             "Superman Returns" -> 4.0,
             "You, Me and Dupree" -> 2.5
        ),
        "Mick LaSalle" -> Map(
            "Lady in the Water" -> 3.0,
            "Snakes on a Plane" -> 4.0,
            "Just My Luck" -> 2.0,
            "Superman Returns" -> 3.0,
            "The Night Listener" -> 3.0,
            "You, Me and Dupree" -> 2.0
        ),
        "Jack Matthews" -> Map(
            "Lady in the Water" -> 3.0,
            "Snakes on a Plane" -> 4.0,
            "The Night Listener" -> 3.0,
            "Superman Returns" -> 5.0,
            "You, Me and Dupree" -> 3.5
        ),
        "Toby" -> Map(
            "Snakes on a Plane" -> 4.5,
            "You, Me and Dupree" -> 1.0,
            "Superman Returns" -> 4.0
        )
    )

    import scala.math

    def main(args: Array[String])
    {
        val person1 = "Lisa Rose"
        val person2 = "Toby"
        print("This is a test of \"sim_distance\" for %s and %s: ".format(person1, person2))
        val distance: Double = sim_distance(critics, "Lisa Rose", "Toby")
        println(distance)
    }

    def sim_distance(prefs: Map[String, Map[String, Double]], person1: String, person2: String): Double = {
        val sum_of_squares =
            prefs(person1).filterKeys(prefs(person2).isDefinedAt(_)).foldLeft[Double](0.0){
                (sum, entry) => sum + math.pow(entry._2 - prefs(person2)(entry._1), 2.0)
            }
        if (sum_of_squares == 0.0) {
            0.0
        } else {
            1.0 / (1.0 + math.sqrt(sum_of_squares))
        }
    }
}