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
    # Get the list of mutually rated items
    si={}
    for item in prefs[person1]:
        for item in prefs[person2]:
            si[item] = 1

    # Find the number of elements
    n = len(si)
    
    # If they have no ratings in common, return 0
    if n == 0:
        return 0
        
    # Add p all the preferences
    sum1 = sum([prefs[person1][it] for it in si])
    sum2 = sum([prefs[person2][it] for it in si])

    # Sum up the squares
    sum1Sq = sum([pow(prefs[person1][it], 2) for it in si])
    sum2Sq = sum([pow(prefs[person2][it], 2) for it in si])
    
    # Sum up the products
    pSum = sum([prefs[person1][it] * prefs[person2][it] for it in si])
    
    # Calculate Pearson Score
    num = pSum - (sum1 * sum2 / n)
    den = sqrt((sum1Sq - pow(sum1, 2) / n) * (sum2Sq - pow(sum2, 2) / n))
    res = num / den
    
    return res