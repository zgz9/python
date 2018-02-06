#encoding: utf-8
# critics={'Lisa':{'a':2.5,'b':3.5,'c':3.0,'d':3.5,'e':2.5,'f':3.0},
# 'Gene':{'a':3.0,'b':3.5,'c':1.5,'d':5.0,'e':3.5,'f':3.0},
# 'Michael':{'a':2.5,'b':3.0,'d':3.5,'f':4.0},
# 'Claudia':{'b':3.5,'c':3.0,'d':4.0,'e':2.5,},
# 'Mick':{'a':3.0,'b':4.0,'c':2.0,'d':3.0,'e':2.0,'f':3.0},
# 'Jack':{'a':3.0,'b':4.0,'d':5.0,'e':3.5,'f':3.0},
# 'Toby':{'b':4.5,'d':3.0,'e':3.5},
# }

critics={'Lisa': {'Lady in the Water': 2.5, 'Snakes on a Plane': 3.5,
 'Just My Luck': 3.0, 'Superman Returns': 3.5, 'You, Me and Dupree': 2.5, 
 'The Night Listener': 3.0},
'Gene': {'Lady in the Water': 3.0, 'Snakes on a Plane': 3.5, 
 'Just My Luck': 1.5, 'Superman Returns': 5.0, 'The Night Listener': 3.0, 
 'You, Me and Dupree': 3.5}, 
'Michael': {'Lady in the Water': 2.5, 'Snakes on a Plane': 3.0,
 'Superman Returns': 3.5, 'The Night Listener': 4.0},
'Claudia': {'Snakes on a Plane': 3.5, 'Just My Luck': 3.0,
 'The Night Listener': 4.5, 'Superman Returns': 4.0, 
 'You, Me and Dupree': 2.5},
'Mick': {'Lady in the Water': 3.0, 'Snakes on a Plane': 4.0, 
 'Just My Luck': 2.0, 'Superman Returns': 3.0, 'The Night Listener': 3.0,
 'You, Me and Dupree': 2.0}, 
'Jack': {'Lady in the Water': 3.0, 'Snakes on a Plane': 4.0,
 'The Night Listener': 3.0, 'Superman Returns': 5.0, 'You, Me and Dupree': 3.5},
'Toby': {'Snakes on a Plane':4.5,'You, Me and Dupree':1.0,'Superman Returns':4.0}}

from math import sqrt
	
def sim_distance(prefs, person1, person2):
# 欧几里得距离
	si={}
	for item in prefs[person1]:
		if item in prefs[person2]:
			si[item]=1

	if len(si) == 0: return 0;

	sum_of_squares = sum([pow(prefs[person1][item]-prefs[person2][item],2) 
		for item in prefs[person1] 
		if item in prefs[person2]
		])
	#print(sum_of_squares)
	return 1/(1+sqrt(sum_of_squares))
	
#皮尔逊相关系数
def sim_person(prefs, p1, p2):
	si={}
	for item in prefs[p1]:
		if item in prefs[p2]:
			si[item] = 1
	n = len(si)
	#如果两者没有共同之处，返回1
	if n==0: return 1;

	#对所有偏好求和
	sum1=sum([prefs[p1][it] for it in si])
	sum2=sum([prefs[p2][it] for it in si])
	#求平方和
	sum1Sq=sum([pow(prefs[p1][it], 2) for it in si])
	sum2Sq=sum([pow(prefs[p2][it], 2) for it in si])
	#求乘积之和
	pSum=sum([prefs[p1][it]*prefs[p2][it] for it in si])

	#计算皮尔逊评价值
	num=pSum-(sum1*sum2/n)
	den=sqrt((sum1Sq-pow(sum1,2)/n)*(sum2Sq-pow(sum2,2)/n))
	if den==0: return 0
	r=num/den

	return r

# 从反映偏好的字典中返回最为匹配者
# 返回结果的个数和相似度函数均为可选参数
def topMatches(prefs, person, n=5, similarity=sim_person):
	scores=[(similarity(prefs,person,other), other)
		for other in prefs if other!=person]
	#对列表进行排序，评价值最高者排在最前面
	scores.sort()
	scores.reverse()
	return scores[0:n]

# 利用所有其他人评价值的加权平均，为某人提供建议
def getRecommendations(prefs, person, similarity=sim_person):
	totals={}
	simSums={}
	for other in prefs:
		#不要和自己做比较
		if other==person: continue
		sim=similarity(prefs,person,other)
		
		#忽略评价值为0或小于0的情况
		if sim <=0: continue
		for item in prefs[other]:
			#只对自己还未曾看过的影片进行评价
			if item not in prefs[person] or prefs[person][item]==0:
				#相似度*评价值
				totals.setdefault(item, 0)
				totals[item]+=prefs[other][item]*sim
				#相似度之和
				simSums.setdefault(item,0)
				simSums[item]+=sim
		#建立一个归一化的列表
		rankings=[(total/simSums[item], item) for item,total in totals.items()]

		# 返回经过排序的列表
		rankings.sort()
		rankings.reverse()
		return rankings

#互换
def transformPrefs(prefs):
	result={}
	for person in prefs:
		for item in prefs[person]:
			result.setdefault(item,{})

			#将物品和人员对调
			result[item][person]=prefs[person][item]
	return result
#reload(recommendations)
#recommendations.sim_distance(recommendations.critics,'lisa Rose','Jene')
#print('over')