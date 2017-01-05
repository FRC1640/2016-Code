#!/bin/bash

while true
do
	ping -w 1 10.0.0.111

	if [ $? -eq 0 ]; then
		echo "pinged"
		break;
	fi
done
env LD_LIBRARY_PATH=/home/pi/Desktop:LD_LIBRARY_PATH java -jar /home/pi/Desktop/core-1.1.1-all.jar /home/pi/Desktop/project.grip
