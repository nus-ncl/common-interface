#!/usr/bin/python
# Script to randomly generate a Deterlab User ID
# Created by Te Ye & Desmond
# 13 June 2016

import time
import sys
import os
import subprocess
import string
import random

# need time delay to allow subprocess to finish processing
# in seconds
TIME_DELAY = 1
USER_ID_EXISTS = 1

def generateRandomString(N):
	return ''.join(random.SystemRandom().choice(string.digits) for _ in range(N))

def generate(first_name, last_name):
	# NOTE: Deterlab can only take in lowercases
	isDeterUserIDExists = USER_ID_EXISTS
	deterOrigUserID = (first_name[0] + last_name[0:6]).lower()

	while isDeterUserIDExists == USER_ID_EXISTS:

		random_number = generateRandomString(8 - len(deterOrigUserID))

		deterUserID = deterOrigUserID + random_number
		
		cmd = "mysql -e \"select COUNT(uid) from tbdb.users where uid='" + deterUserID + "'" + "\""
		q = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE)
		out, err = q.communicate()
		time.sleep(TIME_DELAY)
		# mysql stmt either returns a 0 or 1 (if match)
		output_list = out.split('\n')
		# print output_list
		isDeterUserIDExists = int(output_list[1])

	return deterUserID

if __name__ == "__main__":
	# NOTE: Need to think how many iterations to reloop until break? if generated uid keep colliding
	print generate(sys.argv[1], sys.argv[2])