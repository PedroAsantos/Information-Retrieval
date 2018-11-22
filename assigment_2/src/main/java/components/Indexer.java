/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import com.sun.xml.internal.ws.util.StringUtils;
import obj_indexer.EntryTermPost;
import obj_indexer.Posting;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 *  @author Pedro Santos, 76532 /  Beatriz Coronha 92210    
 */
public class Indexer {
    private Map<String,List<Posting>> invertedIndex;
    private int numberBlock;
    private PrintWriter writer=null;
    private String indexerName;
    private static final int MAXSIZEBLOCKINDEX=30000;
    //dicionario in memory and localization of the postinglist
    public Indexer(String indexerName){
        this.invertedIndex = new TreeMap<String,List<Posting>>();
        this.numberBlock=0;
        this.indexerName=indexerName;
    }
      /**
    * Function to add to the indexer a posting of a term
    * @param term the term that will be add a posting or if the term is not in the index will be created. 
    * @param doc the posting
    */
    private void add(String term, Posting doc){
        
        if(invertedIndex.size()==MAXSIZEBLOCKINDEX){
            writeBlockToFile();
        }
       
       // if(((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())*100)/(Runtime.getRuntime().totalMemory())>97){
        //    writeBlockToFile();
        //}
        
        
        if(!invertedIndex.containsKey(term)){
          invertedIndex.put(term, new ArrayList<Posting>(Arrays.asList(doc)));
        }else{
            List<Posting> tempListDocs = invertedIndex.get(term);
            tempListDocs.add(doc);
            invertedIndex.put(term, tempListDocs);
        }
       
    }
     /**
    * Function to add the tokens of the document to the indexer.
    * @param documentString the tokens of the document to add to the indexer.
    * @param documentId the id of the document
    *  
    */    
    public boolean addToPostingList(List<String> documentString,int documentId){
        //add words of this document to index.     
        
        
        //mao to save positions of each word in the document
        Map<String,List<Integer>> tokenPosition = new HashMap<>();
        
        
        for(int i = 0;i<documentString.size();i++){
            String tokenTemp = documentString.get(i);
            if(tokenPosition.containsKey(tokenTemp)){
                List<Integer> tempPosList = tokenPosition.get(tokenTemp);
                tempPosList.add(i);
                tokenPosition.put(tokenTemp,tempPosList);     
            }else{
                tokenPosition.put(tokenTemp,new ArrayList<>(Arrays.asList(i)));
            }
        }
        

        
        //counting the number of each term that happens in the doc
        Map<String, Long> counts = documentString.stream().collect(Collectors.groupingBy(e -> e,Collectors.counting()));
        
        //map to save logfreqs of each term in document
        Map<Long, Double> logFreqs = new HashMap<>(); 
        
        //calculating logfreqs for each different count
        counts.forEach((k,v)->{ 
            if(!logFreqs.containsKey(v)){
                logFreqs.put(v,1+Math.log10(v));
            }
        });
               
        //calculate normalization
        double normalization = Math.sqrt(counts.values().stream().mapToDouble(c -> logFreqs.get(c)*logFreqs.get(c)).sum());
        
        //map to save the normalizedvalues of each different count
        Map<Long, Double> normalizedValues = new HashMap<>(); 
        //calculating the normalized value for each different log freqs.
        logFreqs.forEach((k,v)->{
            if(!normalizedValues.containsKey(k)){
                normalizedValues.put(k,v/normalization);
            }
        });

        //removing repeated elements
        documentString = documentString.stream().distinct().collect(Collectors.toList());

        //add to the index the term and the correspondent document id with the log freq normalized
        documentString.stream().forEach(x -> {add(x,new Posting(documentId,normalizedValues.get(counts.get(x)),tokenPosition.get(x)));});

        return true;
    }
        
    /**
     * Function to print the index in memory. Function only used to tests.  
     * 
     */
    public void printIndex(){
        for (Map.Entry<String, List<Posting>> entry : invertedIndex.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }                   
    }
    
      /**
     * Function write to a file a block of the index.  
     * 
     */
    private void writeBlockToFile(){
        
        try {
            
            FileWriter fw = new FileWriter("indexer_"+indexerName+"_"+numberBlock+".txt");
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter writerBlock = new PrintWriter(bw);
            
            invertedIndex.entrySet().forEach((entry) -> {
            writerBlock.println(entry.getKey() + "»" + entry.getValue().toString().replace(", ", ""));
            });
            
            writerBlock.close();
            bw.close(); 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            invertedIndex.clear();
            numberBlock++;
        }

        
      
    }
    
      /**
     * Function to write the last block of the indexer to a file.  
     * 
     */
    public void writeLastBlock(){
        if(!invertedIndex.isEmpty()){
            writeBlockToFile();
        }
    }
     /**
     * Function to merge the blocks and create the final indexer.
     * 
     */
    public void mergeBlocks(){
        PriorityQueue<EntryTermPost> pq = new PriorityQueue<>();
        
        List<FileReader> fileReaders = new ArrayList<>();
        List<BufferedReader> bufferedReaders = new ArrayList<>();
        //inicialize file reader and buffer for each block file
        for (int block = 0; block < numberBlock; block++) {
            try {
                fileReaders.add(new FileReader("indexer_"+ indexerName +"_"+block+".txt"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
            bufferedReaders.add(new BufferedReader(fileReaders.get(block)));
        }
        
        String line = null;
        String[] lineArray = null;
        //put in the queu the first term of each block
        for(int block = 0;block < numberBlock;block++){
            try {
                line = bufferedReaders.get(block).readLine();
            } catch (IOException ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
            lineArray= line.split("»");
            pq.add(new EntryTermPost(lineArray[0],lineArray[1],block));
        }
        List<EntryTermPost> listEntries = new ArrayList<>();
        List<Posting> mergedPostingList = new ArrayList<>(); 
        String[] postingList = null;
        String docId;
        String freq;
        String positions;
        //create file for indexer
        createIndexerFile();
        
        while(!pq.isEmpty()){
            try {
                //remove from queue by alphabetic order
                do{
                    listEntries.add(pq.poll());
                                       
                }while(!pq.isEmpty() && listEntries.get(0).compareTo(pq.peek())==0);
                //if it is true, it means that several blocks has the same terme -> merge posting lists
                if(listEntries.size()>1){
                    //transformar tudo numa entry, ordenar as posting lists
                    for(int i = 0;i<listEntries.size();i++){ 
                        postingList = listEntries.get(i).getPostingList().replaceAll("[\\p{Ps}\\p{Pe}\\s+]","").split(";");
                      
                        for (String posting : postingList) {
                            docId = posting.split(":")[0].replaceAll(":", "");
                            freq = posting.split(":")[1];
                            positions = posting.split(":")[2];
                           // positions.split(",")
                            mergedPostingList.add(new Posting(Integer.parseInt(docId),freq,positions));
                        }    
                    }
                    //sort merged postling lsit
                    Collections.sort(mergedPostingList);
                    //write to indexer term with the posting list merged
                    writeMergedPostingListIndexFile(listEntries.get(0).getTerm(),mergedPostingList);
                    //clear posting list;
                    mergedPostingList.clear();
                    //escrever no index File a entry final
                }else{
                     writeIndexFile(listEntries.get(0));
                }
                
                
                for(int i=0;i<listEntries.size();i++){
                    if((line =  bufferedReaders.get(listEntries.get(i).getBlockNumber()).readLine())!= null){
                        lineArray = line.split("»");
                        pq.add(new EntryTermPost(lineArray[0],lineArray[1],listEntries.get(i).getBlockNumber()));
                    }
                }
               
                listEntries.clear();
            } catch (IOException ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        for (int block = 0; block < numberBlock; block++) {         
            try {
                bufferedReaders.get(block).close();
            } catch (IOException ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        closePrinter();
        
    }
     /**
     * Function to create the indexer file.  
     * 
     */
    private void createIndexerFile(){
        FileWriter fw;
        try {
            fw = new FileWriter("indexer_"+indexerName+".txt");
            BufferedWriter bw = new BufferedWriter(fw);
            writer = new PrintWriter(bw);
        } catch (IOException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
    * Function to write to the final indexer the term and the merged posting list.
    * 
    */
    private void writeMergedPostingListIndexFile(String term, List<Posting> mergedPostingList){
        StringBuilder builder = new StringBuilder();
        for (Posting value : mergedPostingList) {
            builder.append(value);
        }
       
         writer.println(term+ ";" +  builder.toString().replaceAll("[\\p{Ps}\\p{Pe}]|\\s","")); 
    }
    /**
    * Function to write to the final indexer the term and the posting list when it was not necesssary to merge.
    * 
    */    
    private void writeIndexFile(EntryTermPost entry){
        writer.println(entry.getTerm()+ ";" + entry.getPostingList().replaceAll("[\\p{Ps}\\p{Pe}]","")); 
    }
     /**
    * Function to close the printer.
    * 
    */ 
    private void closePrinter(){
        writer.close();
    } 
    
 
}
