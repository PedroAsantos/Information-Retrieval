/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrieval;

import java.util.List;
import obj_indexer.Posting;

/**
 *
 * @author rute
 */
public class ScoreRetrievalPhrase {
    private Posting posting;
    private int tokenId;
   
    public ScoreRetrievalPhrase(int tokenId, Posting posting){
        this.tokenId = tokenId;
        this.posting=posting;
    }
    
    public int getId(){
        return tokenId;
    }
    
    public Posting getPosting(){
        return posting;
    }
}
