module Recommendations

    CRITICS = { :'Lisa Rose' =>  {:'Lady in the Water' => 2.5,
                                :'Snakes on a Plane' =>3.5,
                                :'Just My Luck' => 3.0,
                                :'Superman Returns' => 3.5,
                                :'You, Me and Dupree' => 2.5, 
                                :'The Night Listener' => 3.0},
                :'Gene Seymour' => {:'Lady in the Water' => 3.0,
                                :'Snakes on a Plane' => 3.5,
                                :'Just My Luck' => 1.5,
                                :'Superman Returns' => 5.0,
                                :'The Night Listener' => 3.0,
                                :'You, Me and Dupree' => 3.5}, 
                :'Michael Phillips' => {:'Lady in the Water' => 2.5,
                                :'Snakes on a Plane' => 3.0,
                                :'Superman Returns' => 3.5,
                                :'The Night Listener' => 4.0},
                :'Claudia Puig' => {:'Snakes on a Plane' => 3.5,
                                :'Just My Luck' => 3.0,
                                :'The Night Listener' => 4.5,
                                :'Superman Returns' => 4.0,
                                :'You, Me and Dupree' => 2.5},
                :'Mick LaSalle' => {:'Lady in the Water' => 3.0,
                                :'Snakes on a Plane' => 4.0, 
                                :'Just My Luck' => 2.0,
                                :'Superman Returns' => 3.0,
                                :'The Night Listener' => 3.0,
                                :'You, Me and Dupree' => 2.0}, 
                :'Jack Matthews' => {:'Lady in the Water' => 3.0,
                                :'Snakes on a Plane' => 4.0,
                                :'The Night Listener' => 3.0,
                                :'Superman Returns' => 5.0,
                                :'You, Me and Dupree' => 3.5},
                :'Toby' => {:'Snakes on a Plane' => 4.5,
                                :'You, Me and Dupree' => 1.0,
                                :'Superman Returns' => 4.0}}

    def sim_distance(prefs, person1, person2)
        sum_of_squares = 0
        prefs[person1].each_key do |item|
            if prefs[person2].has_key? item 
                sum_of_squares += (prefs[person1][item] - prefs[person2][item]) ** 2
            end
        end
        if sum_of_squares == 0
            0
        else
            1 / (1 + Math.sqrt(sum_of_squares))
        end
    end

    def sim_pearson(prefs, person1, person2)
        cntr = sum1 = sum2 = sum1Sq = sum2Sq = pSum = 0
        
        prefs[person1].each_key do |item|
            if prefs[person2].has_key? item
                cntr += 1
            
                p1Item = prefs[person1][item]
                p2Item = prefs[person2][item]

                # Add up all the preferences
                sum1 += p1Item
                sum2 += p2Item

                # Sum up the squares
                sum1Sq += p1Item ** 2
                sum2Sq += p2Item ** 2

                # Sum up the products
                pSum += p1Item * p2Item
            end
        end
        
        if cntr == 0
            0 # If they have no ratings in common, return 0
        else
            # Calculate Pearson Score
            num = pSum - (sum1 * sum2 / cntr)
            den = Math.sqrt((sum1Sq - (sum1 ** 2) / cntr) * (sum2Sq - (sum2 ** 2) / cntr))
            num / den
        end
    end

    def top_matches(prefs, person, n=5, similarity=method(:sim_pearson))
        scores = []
        prefs.each_key do |other|
            scores << [similarity.call(prefs, person, other), other] if other != person
        end
        scores.sort!
        scores.reverse!
        return scores.slice(0...n)
    end
end