/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author rute
 */
public class Indexer {
    private Map<String,List<Integer>> invertedIndex;
    //dicionario in memory and localization of the postinglist
    public Indexer(){
        invertedIndex = new HashMap<String,List<Integer>>();
    }
    
    public boolean addToPostingList(List<String> documentString,int documentId){
        //add words of this document to index. 
        
        List<Integer> tempPostingList = new LinkedList<Integer>(Arrays.asList(documentId));
        
        List<Integer> asd = new LinkedList<Integer>();
        
        asd.add(documentId);
        documentString.stream().forEach(termo -> invertedIndex.put(termo,asd));
        
        return false;
    }
        
}
