#!/bin/bash 
	# echo $1
	cmd="drawDistances('$1',1)"
	# echo $cmd
	matlab -nodisplay -r "$cmd"
