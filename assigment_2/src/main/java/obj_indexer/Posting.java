/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obj_indexer;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 *
 * @author rute
 */
public class Posting implements Serializable, Comparable<Posting>{
    
    private int id;
    private int nOcurrences;
    private double logFreqNormal; 
    private List<Integer> positions;
    
    private String logFreqNormalString;
    private String positionsString;
    public Posting(int id, int nOcurrences){
        this.id = id;
        this.nOcurrences=nOcurrences;
    }
    public Posting(int id, double logFreqNormal){
        this.id = id;
        this.logFreqNormal=logFreqNormal;
    }
    
    public Posting(int id, double logFreqNormal, List<Integer> positions){
        this.id = id;
        this.logFreqNormal=logFreqNormal;
        this.positions = positions;
    }
    
    public Posting(int id, String logFreqNormalString, String positionsString){
        this.id = id;
        this.logFreqNormalString=logFreqNormalString;
        this.positionsString = positionsString;
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
    public List<Integer> getPositionList(){
        return positions;
    }
    @Override
    public String toString() {
       
       if(positionsString!=null){
        return id + ":" + logFreqNormalString +":"+ positionsString+";"; 
       }
       String positionsStringT = positions.stream().map(Object::toString)
                        .collect(Collectors.joining(","));
       
       return id + ":" + String.format(Locale.US,"%.2f", logFreqNormal)+":"+ positionsStringT+";"; 
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
