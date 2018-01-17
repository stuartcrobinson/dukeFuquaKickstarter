import json
from pprint import pprint

from os import listdir
from os.path import isfile, join

import gender_guesser.detector as gender

d = gender.Detector()
print(d.get_gender(u"Bob"))

mypath = '.'

onlyfiles = [f for f in listdir(mypath) if isfile(join(mypath, f))]

for f in listdir(mypath):
    data = json.load(open(f))
    pitch = data['pitch'].replace("\n", " ").replace("  ", " ").replace("  ", " ")
    pitchLen = len(pitch.split(" "))
    print("pitchLen")
    print(pitchLen)
    data['pitchWordCount'] = pitchLen
    firstName = data['creator']['name'].split(" ")[0]
    gender = d.get_gender(firstName)
    data['genderGuess'] = gender
    with open('data.txt', 'w') as outfile:
        json.dump(data,  f)
    print(pitchLen, firstName, gender)
    






data = json.load(open('data.json'))



#TODO - 
#

ssh -i "eastKeyPairPem.pem" ec2-user@ec2-52-91-244-246.compute-1.amazonaws.com

scp -i "eastKeyPairPem.pem" ec2-user@ec2-52-91-244-246.compute-1.amazonaws.com:~/jsons.zip ~/beccakickstartblubs