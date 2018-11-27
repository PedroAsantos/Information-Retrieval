/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexer;

/**
 *
 * @author rute
 */
public class EntryTermPost implements Comparable<EntryTermPost>{
    
    private String term;
    private String postinglist;
    private int blockNumber;
    public EntryTermPost(String term, String postingList,int blockNumber){
        this.term = term;
        this.postinglist = postingList;
        this.blockNumber = blockNumber;
    }
    
    public String getTerm(){
        return term;
    }
    public String getPostingList(){
        return postinglist;
    }
    public int getBlockNumber(){
        return blockNumber;
    }
    @Override
    public int compareTo(EntryTermPost t) {
        if(term.compareTo(t.getTerm())>0){
            return 1;
        }
         if(term.compareTo(t.getTerm())<0){
            return -1;
        }
         return 0;
    }
    
}
