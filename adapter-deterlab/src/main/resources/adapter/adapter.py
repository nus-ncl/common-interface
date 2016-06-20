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
from ConfigParser import SafeConfigParser
import generateDeterUserId
import json
import web
import time
import sys
import os
import subprocess

CONFIG_FILE = "config.ini"
CONFIG_DETER_URI_TITLE = "DETER_URI"
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
		my_dict = {}
		my_dict['first_name'] = parsed_json['firstName']
		my_dict['last_name'] = parsed_json['lastName']
		my_dict['job_title'] = parsed_json['jobTitle']
		my_dict['password'] = parsed_json['password']
		my_dict['email'] = parsed_json['email']
		my_dict['phone'] = parsed_json['phone']
		my_dict['institution'] = parsed_json['institution']
		my_dict['institution_abbrev'] = parsed_json['institutionAbbreviation']
		my_dict['institution_web'] = parsed_json['institutionWeb']
		my_dict['address1'] = parsed_json['address1']
		my_dict['address2'] = parsed_json['address2']
		my_dict['country'] = parsed_json['country']
		my_dict['region'] = parsed_json['region']
		my_dict['city'] = parsed_json['city']
		my_dict['zip_code'] = parsed_json['zipCode']

		# call a python module to generate a unique deterlab userid
		uid = generateDeterUserId.generate(my_dict['first_name'], my_dict['last_name'])

		print uid

		# join project (create user account in tbdb)
		join_project(uid, my_dict)
		resultJSON = create_user(uid, my_dict['password'])
		return resultJSON

def join_project(uid, my_dict):

	parser = SafeConfigParser()
	parser.read(CONFIG_FILE)
	deter_uri = parser.get('DETER_URI', 'uri')

	# need to parse in the form fields
	# set pid as ncl for now
	# uid, email address must be unique
	cmd = "curl --silent -k -d \"submit=Submit" + \
	 "&formfields[pid]=ncl" + \
	 "&formfields[uid]=" + uid + \
	 "&formfields[usr_name]=" + my_dict['last_name'] + " " + my_dict['first_name'] + \
	 "&formfields[usr_title]=" + my_dict['job_title'] + \
	 "&formfields[usr_affil]=" + my_dict['institution'] + \
	 "&formfields[usr_affil_abbrev]=" + my_dict['institution_abbrev'] + \
	 "&formfields[usr_email]=" + my_dict['email'] + \
	 "&formfields[usr_addr]=" + my_dict['address1'] + \
	 "&formfields[usr_city]=" + my_dict['city'] + \
	 "&formfields[usr_state]=" + my_dict['region'] + \
	 "&formfields[usr_zip]=" + my_dict['zip_code'] + \
	 "&formfields[usr_country]=" + my_dict['country'] + \
	 "&formfields[usr_phone]=" + my_dict['phone'] + \
	 "&formfields[password1]=" + my_dict['password'] + \
	 "&formfields[password2]=" + my_dict['password'] + "\" " + \
	 deter_uri + "joinproject.php > /dev/null"

	# print cmd
	p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE)
	time.sleep(TIME_DELAY)

def create_user(uid, password):

	resultJSON = "{"

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
		resultJSON = resultJSON + "\"msg\": \"no user created\""

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

		resultJSON = resultJSON + "\"msg\": \"user is created\""
	else:
		resultJSON = resultJSON + "\"msg\": \"user not found\""

	# append uid to json
	# need comma
	resultJSON = resultJSON + ",\"uid\": \"" + uid + "\"}"
	return resultJSON

if __name__ == "__main__":
	app.run()