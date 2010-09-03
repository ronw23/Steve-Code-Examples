CRITICS = {	:'Lisa Rose' =>  {'Lady in the Water' => 2.5,
							'Snakes on a Plane' =>3.5,
 						    'Just My Luck' => 3.0,
 						    'Superman Returns' => 3.5,
 						    'You, Me and Dupree' => 2.5, 
                            'The Night Listener' => 3.0},
           	:'Gene Seymour' => {'Lady in the Water' => 3.0,
                            'Snakes on a Plane' => 3.5,
                            'Just My Luck' => 1.5,
                            'Superman Returns' => 5.0,
                            'The Night Listener' => 3.0,
                            'You, Me and Dupree' => 3.5}, 
			:'Michael Phillips' => {'Lady in the Water' => 2.5,
							'Snakes on a Plane' => 3.0,
							'Superman Returns' => 3.5,
							'The Night Listener' => 4.0},
			:'Claudia Puig' => {'Snakes on a Plane' => 3.5,
							'Just My Luck' => 3.0,
							'The Night Listener' => 4.5,
							'Superman Returns' => 4.0,
							'You, Me and Dupree' => 2.5},
			:'Mick LaSalle' => {'Lady in the Water' => 3.0,
							'Snakes on a Plane' => 4.0, 
 							'Just My Luck' => 2.0,
 							'Superman Returns' => 3.0,
 							'The Night Listener' => 3.0,
 							'You, Me and Dupree' => 2.0}, 
			:'Jack Matthews' => {'Lady in the Water' => 3.0,
							'Snakes on a Plane' => 4.0,
 							'The Night Listener' => 3.0,
 							'Superman Returns' => 5.0,
 							'You, Me and Dupree' => 3.5},
			:'Toby' => {'Snakes on a Plane' => 4.5,
							'You, Me and Dupree' => 1.0,
							'Superman Returns' => 4.0}}

def sim_distance(prefs, person1, person2)
	si = Hash.new
	sum_of_squares = 0
	prefs[person1].each_key do |item|
		if prefs[person2].has_key? item	
			sum_of_squares += (prefs[person1][item] - prefs[person2][item]) ** 2
		end
	end
	if sum_of_squares == 0
		return 0
	else
		return 1 / (1 + Math.sqrt(sum_of_squares))
	end
end