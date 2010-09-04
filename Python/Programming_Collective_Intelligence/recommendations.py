# A dictionary of movie critics and their ratings of a small
# set of movies
critics={'Lisa Rose': {'Lady in the Water': 2.5, 'Snakes on a Plane': 3.5,
 'Just My Luck': 3.0, 'Superman Returns': 3.5, 'You, Me and Dupree': 2.5, 
 'The Night Listener': 3.0},
'Gene Seymour': {'Lady in the Water': 3.0, 'Snakes on a Plane': 3.5, 
 'Just My Luck': 1.5, 'Superman Returns': 5.0, 'The Night Listener': 3.0, 
 'You, Me and Dupree': 3.5}, 
'Michael Phillips': {'Lady in the Water': 2.5, 'Snakes on a Plane': 3.0,
 'Superman Returns': 3.5, 'The Night Listener': 4.0},
'Claudia Puig': {'Snakes on a Plane': 3.5, 'Just My Luck': 3.0,
 'The Night Listener': 4.5, 'Superman Returns': 4.0, 
 'You, Me and Dupree': 2.5},
'Mick LaSalle': {'Lady in the Water': 3.0, 'Snakes on a Plane': 4.0, 
 'Just My Luck': 2.0, 'Superman Returns': 3.0, 'The Night Listener': 3.0,
 'You, Me and Dupree': 2.0}, 
'Jack Matthews': {'Lady in the Water': 3.0, 'Snakes on a Plane': 4.0,
 'The Night Listener': 3.0, 'Superman Returns': 5.0, 'You, Me and Dupree': 3.5},
'Toby': {'Snakes on a Plane':4.5,'You, Me and Dupree':1.0,'Superman Returns':4.0}}


from math import sqrt

def sim_distance(prefs, person1, person2):
    si = dict()
    sum_of_squares = 0
    for item in prefs[person1]:
        if item in prefs[person2]:
            sum_of_squares += pow(prefs[person1][item] - prefs[person2][item], 2)
    if sum_of_squares == 0:
        return 0
    else:
        return 1 / (1 + sqrt(sum_of_squares))


def sim_pearson(prefs, person1, person2):
    cntr = 0
    sum1 = sum2 = sum1Sq = sum2Sq = pSum = 0

    for item in prefs[person1]:   
        if item in prefs[person2]:
            cntr += 1

            p1Item = prefs[person1][item]
            p2Item = prefs[person2][item]

            # Add up all the preferences
            sum1 += p1Item
            sum2 += p2Item

            # Sum up the squares
            sum1Sq += pow(p1Item, 2)
            sum2Sq += pow(p2Item, 2)

            # Sum up the products
            pSum += p1Item * p2Item
    
    # If they have no ratings in common, return 0
    if cntr == 0:
        return 0
    
    # Calculate Pearson Score
    num = pSum - (sum1 * sum2 / cntr)
    den = sqrt((sum1Sq - pow(sum1, 2) / cntr) * (sum2Sq - pow(sum2, 2) / cntr))
    res = num / den
    
    return res