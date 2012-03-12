/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.table;

import jamde.distribution.Distribution;

/**
 *
 * @author honza
 */
class CountThread extends Thread{
    private TableInput input;
    private int load;
    private Distribution[] estimatorArray;
    
    public CountThread(TableInput input, int load) {
        this.input = input;
        this.load = load;
        estimatorArray = new Distribution[load];
    }
    
    public        
    
    Distribution[] startCount() {
        /*
         * TODO prekopirovat sem veci a zaridit aby to fungovalo
         */
        start();
        
        return estimatorArray;
    }
    
}
