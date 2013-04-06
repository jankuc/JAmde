#!/bin/bash 
	# echo $1
	cmd="cd /home/honza/Documents/FJFI/Renyi/JAmde/; drawDistances('$1',1)"
	# echo $cmd
	matlab -nodisplay -r "$cmd"
