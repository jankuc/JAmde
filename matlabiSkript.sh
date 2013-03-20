#!/bin/bash
	echo $1
	cmd="C = load('$1'); f = figure('visible','off'); imagesc(C); colorbar; saveas(f,'$1.png','png'); quit"
	#echo $cmd
	matlab -nodisplay -r "$cmd"
    
                    
