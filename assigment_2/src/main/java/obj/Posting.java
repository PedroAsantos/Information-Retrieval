/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obj;

import java.io.Serializable;
import java.util.Locale;

/**
 *
 * @author rute
 */
public class Posting implements Serializable, Comparable<Posting>{
    
    private int id;
    private int nOcurrences;
    private double logFreqNormal; 
    public Posting(int id, int nOcurrences){
        this.id = id;
        this.nOcurrences=nOcurrences;
    }
    public Posting(int id, double logFreqNormal){
        this.id = id;
        this.logFreqNormal=logFreqNormal;
    }
    
    public double getLogFreq(){
        return logFreqNormal;
    }

    public int getId(){
        return id;
    }
    public int getNOcurrences(){
        return nOcurrences;
    }
    @Override
    public String toString() {
        return id + ":" + String.format(Locale.US,"%.2f", logFreqNormal); 
    }  
    @Override
    public int compareTo(Posting t) {
        if(this.id < t.getId()){
            return -1;
        }
        if(this.id> t.getId()){
            return 1;
        }
        
        return 0;
    }
    
    
    
}
