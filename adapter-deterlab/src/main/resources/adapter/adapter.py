#!/usr/bin/python
# Created by Te Ye & Desmond
# 15 June 2016
"""
Python Web Client to invoke the following functions from a Python Web Service:
0. Create users via apply project script?
1. Create users via join project script
2. Apply for new project after login
3. Join existing project after login

Later TODO:
1. Remove users and projects
2. Create experiment
"""
import generateDeterUserId
import json
import web
import time
import sys
import os
import subprocess

# need time delay to allow subprocess to finish processing
# in seconds
TIME_DELAY = 1

urls = (
	'/addUsers', 'add_users',
)

app = web.application(urls, globals())

class add_users:
	def POST(self):
		# web.header('Content-Type', 'application/json')
		data = web.data()
		print data
		parsed_json = json.loads(data)
		
		# json key must match that defined under RegistrationService.java
		first_name = parsed_json['firstname']
		last_name = parsed_json['lastname']
		password = parsed_json['password']
		email = parsed_json['email']

		# call a python module to generate a unique deterlab userid
		uid = generateDeterUserId.generate(first_name, last_name)

		# join project (create user account in tbdb)
		join_project(uid, password, email)
		result = create_user(uid, password)
		return result

def join_project(uid, password, email):
	# need to parse in the form fields
	# set pid as ncl for now
	# uid, email address must be unique
	cmd = "curl --silent -k -d \"submit=Submit" + \
	 "&formfields[pid]=ncl" + \
	 "&formfields[uid]=" + uid + \
	 "&formfields[usr_name]=Desmond Lim One" + \
	 "&formfields[usr_title]=software engineer" + \
	 "&formfields[usr_affil]=national university" + \
	 "&formfields[usr_affil_abbrev]=nus" + \
	 "&formfields[usr_email]=" + email + \
	 "&formfields[usr_addr]=address1" + \
	 "&formfields[usr_city]=sg" + \
	 "&formfields[usr_state]=sg" + \
	 "&formfields[usr_zip]=12345678" + \
	 "&formfields[usr_country]=singapore" + \
	 "&formfields[usr_phone]=65659898" + \
	 "&formfields[password1]=" + password + \
	 "&formfields[password2]=" + password + "\" " + \
	 "https://www.test.ncl.sg/joinproject.php > /dev/null"

	# print cmd
	p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE)
	time.sleep(TIME_DELAY)

def create_user(uid, password):
	cmd = "mysql -e \"select verify_key from tbdb.users where uid='" + uid + "'" + "\""
	# print cmd
	q = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE)
	out, err = q.communicate()
	time.sleep(TIME_DELAY)
	# print "OUTPUT: " + out
	output_list = out.split('\n');

	# mysql statement returns two parameters with a newline
	print len(output_list)
	if len(output_list) < 1:
		return "no user created"

	verify_key = output_list[1]
	if verify_key:
		print verify_key

		# have to pipe to /dev/null to get rid of curl response body
		# change user status from newuser to verified to unapproved
		login_url = "curl --silent -k -H \"Cache-Control: max-age=120\" -c cookies.txt -d \"uid=" + uid + "&password=" + password + "&login=" + uid + "\" https://www.test.ncl.sg/login.php > /dev/null"
		q = subprocess.Popen(login_url, shell=True, stdout=subprocess.PIPE)
		time.sleep(TIME_DELAY)

		verify_url = "curl --silent -k -b cookies.txt https://www.test.ncl.sg/verifyusr.php?key=" + verify_key + " > /dev/null"
		q = subprocess.Popen(verify_url, shell=True, stdout=subprocess.PIPE)
		time.sleep(TIME_DELAY)

		return "user is created"
	else:
		return "user not found"

if __name__ == "__main__":
	app.run()