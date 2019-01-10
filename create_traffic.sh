#!/bin/bash

RANDOM=$$$(date +%s)

dest=("brazil" "rome" "thailand")
for i in {1..50000};
do
	url="http://localhost:8080/destination?user=Paul&dest=${dest[$RANDOM % 3]}"
	curl -s $url > /dev/null
	url="http://localhost:8081/ad/dynamic?user=Paul"
	curl -s $url > /dev/null
done;
