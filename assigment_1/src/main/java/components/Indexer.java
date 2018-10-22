/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author rute
 */
public class Indexer {
    private Map<String,List<Document>> invertedIndex;
    private int numberBlock;
    private PrintWriter writer=null;
    private static final int MAXSIZEBLOCKINDEX=150;
    //dicionario in memory and localization of the postinglist
    public Indexer(){
        this.invertedIndex = new TreeMap<String,List<Document>>();
        this.numberBlock=0;
    }
    
    private void add(String term, Document doc){
        if(invertedIndex.size()==MAXSIZEBLOCKINDEX){
            writeBlockToFile();
        }
        
        
        if(!invertedIndex.containsKey(term)){
          invertedIndex.put(term, new ArrayList<Document>(Arrays.asList(doc)));
        }else{
            List<Document> tempListDocs = invertedIndex.get(term);
            tempListDocs.add(doc);
            invertedIndex.put(term, tempListDocs);

        }
       
    }
    
    public boolean addToPostingList(List<String> documentString,int documentId){
        //add words of this document to index. 
        
        //counting the number of each term that happens in the doc
        Map<String, Long> counts = documentString.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        //removing repeated elements
        documentString = documentString.stream().distinct().collect(Collectors.toList());
        
        //add to the index the term and the correspondent document
        documentString.stream().forEach(x -> {add(x,new Document(documentId,Math.toIntExact(counts.get(x))));});
        
        
        return true;
    }
        
    
    public void printIndex(){
        for (Map.Entry<String, List<Document>> entry : invertedIndex.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }                   
    }
    
    
    private void writeBlockToFile(){
        PrintWriter writer=null;
        try {
            writer = new PrintWriter("indexer_"+numberBlock+".txt", "UTF-8");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Map.Entry<String, List<Document>> entry : invertedIndex.entrySet()) {
            //System.out.println(entry.getKey() + " = " + entry.getValue());
            writer.println(entry.getKey() + ";" + entry.getValue().toString());
        }
        writer.close();
        invertedIndex.clear();
        numberBlock++;
    }
    
    public void mergeBlocks(){
        PriorityQueue<EntryTermPost> pq = new PriorityQueue<EntryTermPost>();
        
        List<FileReader> fileReaders = new ArrayList<>();
        List<BufferedReader> bufferedReaders = new ArrayList<>();
        //inicialize file reader and buffer for each block file
        for (int block = 0; block < numberBlock; block++) {
            try {
                fileReaders.add(new FileReader("/home/rute/Documents/cadeiras/5ano/ri/assigment_1/indexer_"+block+".txt"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
            bufferedReaders.add(new BufferedReader(fileReaders.get(block)));
        }
        
        String line = null;
        String[] lineArray = null;
        
        for(int block = 0;block < numberBlock;block++){
            try {
                line = bufferedReaders.get(block).readLine();
            } catch (IOException ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
            lineArray= line.split(";");
            pq.add(new EntryTermPost(lineArray[0],lineArray[1],block));
        }
        List<EntryTermPost> listEntries = new ArrayList<>();
        List<Document> mergedPostingList = new ArrayList<>(); 
        String[] postingList = null;
        String docId;
        String freq;
        
        createIndexerFile();
        
        while(!pq.isEmpty()){
            try {
                         
                do{
                    listEntries.add(pq.poll());
                                       
                }while(!pq.isEmpty() && listEntries.get(0).compareTo(pq.peek())==0);
                           
                if(listEntries.size()>1){
                    //transformar tudo numa entry, ordenar as posting lists
                    for(int i = 0;i<listEntries.size();i++){ 
                        postingList = listEntries.get(i).getPostingList().replaceAll("[\\p{Ps}\\p{Pe}\\s+]","").split(",");
                      
                        for(int post = 0;post < postingList.length ;post++){
                            docId=postingList[post].split(":")[0].replaceAll(":","");
                            freq=postingList[post].split(":")[1];
                                                       
                            mergedPostingList.add(new Document(Integer.parseInt(docId),Integer.parseInt(freq))); 
                        }    
                    }
                    Collections.sort(mergedPostingList);
                    
                    writeMergedPostingListIndexFile(listEntries.get(0).getTerm(),mergedPostingList);
                    
                    //escrever no index File a entry final
                }else{
                     writeIndexFile(listEntries.get(0));
                }
                
                
                for(int i=0;i<listEntries.size();i++){
                    if((line =  bufferedReaders.get(listEntries.get(i).getBlockNumber()).readLine())!= null){
                        lineArray = line.split(";");
                        pq.add(new EntryTermPost(lineArray[0].replaceAll(",",""),lineArray[1],listEntries.get(i).getBlockNumber()));
                    }
                }
               
                listEntries.clear();
            } catch (IOException ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        closePrinter();
        
        
    }
    
    private void createIndexerFile(){
        try {
            writer = new PrintWriter("indexer.txt", "UTF-8");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    private void writeMergedPostingListIndexFile(String term, List<Document> mergedPostingList){
         writer.println(term+ "," + mergedPostingList); 
    }
        
    private void writeIndexFile(EntryTermPost entry){
        writer.println(entry.getTerm()+ "," + entry.getPostingList()); 
    }
    
    private void closePrinter(){
        writer.close();
    }
    public void testeQueue(){
        PriorityQueue<String> queu = new PriorityQueue<String>();
        queu.add("pedro");
        queu.add("emanuel");
        queu.add("asda");
        queu.add("ad");
        queu.add("asda");
        while(!queu.isEmpty()){
            System.out.println(queu.poll());
        }
    }
    
    
    private void serializeBlock(){
        try {
            FileOutputStream fileOut = new FileOutputStream("/tmp/indexer_"+numberBlock+".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(invertedIndex);
            out.close();
            fileOut.close();
            System.out.println("/tmp/indexer_"+numberBlock+".ser");
        } catch (IOException i) {
            i.printStackTrace();
        } finally{
            invertedIndex.clear();
            numberBlock++;
        }
    }
    
    
 
}
