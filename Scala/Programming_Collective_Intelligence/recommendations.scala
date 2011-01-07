
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

    import scala.math.{pow, sqrt}

    def main(args: Array[String])
    {
        val person1: String = args match {
            case Array(first, _*) => first
            case _ => "Lisa Rose"
        }
        val person2: String = args match {
            case Array(_, second, _*) => second
            case _ => "Gene Seymour"
        }

        print("This is a test of \"sim_distance\" for %s and %s: ".format(person1, person2))
        val d1: Double = sim_distance(critics, person1, person2)
        println(d1)

        print("This is a test of \"sim_pearson\" for %s and %s: ".format(person1, person2))
        val d2: Double = sim_pearson(critics, person1, person2)
        println(d2)
    }

    def sim_distance(prefs: Map[String, Map[String, Double]], person1: String, person2: String): Double = {
        val sum_of_squares =
            prefs(person1).filterKeys(prefs(person2).isDefinedAt(_)).foldLeft[Double](0.0){
                (sum, entry) => sum + pow(entry._2 - prefs(person2)(entry._1), 2.0)
            }
        if (sum_of_squares == 0.0) {
            0.0
        } else {
            1.0 / (1.0 + sqrt(sum_of_squares))
        }
    }

    def sim_pearson(prefs: Map[String, Map[String, Double]], person1: String, person2: String): Double = {
        val items = prefs(person1).keySet.intersect(prefs(person2).keySet)
        val iSize = items.size
        if (iSize == 0) {
            0.0
        } else {
            var sum1, sum2, sum1Sq, sum2Sq, pSum, p1Item, p2Item = 0.0
            for (item <- items) {
                p1Item = prefs(person1)(item)
                p2Item = prefs(person2)(item)
                sum1 += p1Item
                sum2 += p2Item
                sum1Sq += pow(p1Item, 2)
                sum2Sq += pow(p2Item, 2)
                pSum += p1Item * p2Item
            }
            val num = pSum - ((sum1 * sum2) / iSize)
            val den = sqrt((sum1Sq - (pow(sum1, 2) / iSize)) * (sum2Sq - (pow(sum2, 2) / iSize)))
            num / den
        }
    }
}