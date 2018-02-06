#print('hello,world')
import recommendations
reload(recommendations)
print(recommendations.sim_distance(recommendations.critics,'Lisa','Gene'))
print(recommendations.sim_person(recommendations.critics,'Lisa','Gene'))
print(recommendations.topMatches(recommendations.critics, 'Toby', n=3))
print(recommendations.getRecommendations(recommendations.critics, 'Toby'))
print(recommendations.getRecommendations(recommendations.critics, 'Toby', similarity=recommendations.sim_distance))
movies=recommendations.transformPrefs(recommendations.critics)
print(recommendations.topMatches(movies, 'Superman Returns'))
print(recommendations.getRecommendations(movies, 'Just My Luck'))

import pydelicious
print(pydelicious.get_popular(tag='programming'))