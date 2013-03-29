#!/bin/bash 
	echo $1
	cmd="cd /home/honza/Documents/FJFI/Renyi/JAmde/; drawDistances('$1')"
	echo $cmd
	matlab -nodisplay -r "$cmd"
    
    # " C = load('$1'); f = figure('visible','off'); imagesc(C); colorbar; saveas(f,'$1.png','png'); quit"
