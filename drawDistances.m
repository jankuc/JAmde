function drawDistances(pathToFilesList, quitBool)
    % Loads file with paths to all distanceFiles.dist
    % Then one by one (parallel) loads these files and prints their distance picture.

    fileList = textscan(fopen(pathToFilesList), '%s');

    fileList = fileList{1};
    n = length(fileList);
    
    
    if (n > 20) % If n is smaller, configuration of threads takes longer, than the parallelization saves 
        matlabpool open local 
    end
    %parallel for
    
    parfor k = 1:length(fileList)
        drawDistance(fileList{k});
        disp('Distance picture successfully generated.')
        disp(num2str(k))
    end
    
    if (n > 20)
        matlabpool close
    end
    
    if quitBool
        quit;
    end
end

function drawDistance(pathToFile)
	C = load(pathToFile);
	f = figure('visible','off');
	imagesc(C);
	colorbar;
    
    yRange = 8; % parameter xigma  (in figure it is x-axis)
    xRange = 8; % parameter mu (in figure it is y-axis)
    
    set(gca,'XTick',1:499/yRange:500)
    set(gca,'XTickLabel',{'0.001','1','2','3','4','5','6','7','8'})
    set(gca,'YTick',1:499/xRange:500)
    set(gca,'YTickLabel',{'4','3','2','1','0','-1','-2','-3','-4'})
    xlabel('\sigma')
    ylabel('\mu')
    
    % saveas(f,strcat(pathToFile,'.eps'),'eps2c') 
    saveas(f,strcat(pathToFile,'.png'),'png') 
end