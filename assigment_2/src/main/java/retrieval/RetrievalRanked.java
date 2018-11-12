/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrieval;

import cache.MemoryCache;
import components.ImprovedTokenizer;
import components.SimpleTokenizer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author rute
 */
public class RetrievalRanked {
    //private RandomAccessFile raf;
    private final String fileName;
    private List<BlockIndex> blockBotTop;
    private final int BLOCK_SIZE=2500;
    private final int totalDocs;
    private final MemoryCache<String,String> cache;
    
    public RetrievalRanked(String fileName,int totalDocs,int cacheSize, long timeToLive, long timerInterval){
        this.fileName=fileName;
        this.totalDocs=totalDocs;
       /* try {
            this.raf = new RandomAccessFile(fileName, "r");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        }*/
       
        //findBottomTopByLetter();
        //findBottomTopBlock();
        cache = new MemoryCache(timeToLive,timerInterval,cacheSize);
    }
    
 
    public void generateBlocks(){
        File f = new File("indexer_sub_block0.txt");
        
        if(!(f.exists() && !f.isDirectory())){
            blockBotTop = new ArrayList<>();
            BufferedReader bufferedReader;
            String line;
            int lineNumber=0;
            int numberBlock = 0;
            BlockIndex blockTempIndex = null;
            FileWriter fw;
           try {
               bufferedReader = new BufferedReader(new FileReader(fileName));
               fw = new FileWriter("indexer_sub_block" + numberBlock + ".txt");
               BufferedWriter bw = new BufferedWriter(fw);
               PrintWriter writerBlock = new PrintWriter(bw);

               if((line = bufferedReader.readLine()) != null){
                   blockTempIndex = new BlockIndex(line.split(",")[0], numberBlock);
                   lineNumber++;
                   writerBlock.println(line);
               }

               while((line = bufferedReader.readLine()) != null){
                   writerBlock.println(line);
                   lineNumber++;
                   if(lineNumber % BLOCK_SIZE==0){
                       blockTempIndex.setBottomString(line.split(",")[0]);
                       //blockTempIndex.setBottomLine(lineNumber);
                       blockBotTop.add(blockTempIndex);

                       numberBlock++;
                       writerBlock.close();
                       fw = new FileWriter("indexer_sub_block" + numberBlock + ".txt");
                       bw = new BufferedWriter(fw);
                       writerBlock = new PrintWriter(bw);
                       if((line = bufferedReader.readLine()) != null){
                           writerBlock.println(line);
                           lineNumber++;
                           blockTempIndex = new BlockIndex(line.split(",")[0],numberBlock);   
                       }
                   }

               }
               bufferedReader.close();
               writerBlock.close();
               bufferedReader = new BufferedReader(new FileReader("indexer_sub_block" + numberBlock + ".txt"));
               String lastLine=null;
               while((line = bufferedReader.readLine()) != null){
                    lastLine=line;
               }
               
               writerBlock.close();
               bufferedReader.close();
               blockTempIndex.setBottomString(lastLine.split(",")[0]);
               //blockTempIndex.setBottomLine(lineNumber);
               blockBotTop.add(blockTempIndex);
               
             /*  for(BlockIndex block : blockBotTop){
                System.out.println(block);
               }*/
               serializeBlocksArray();

           } catch (IOException ex) {
               Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
           }
        }else{
           // readParamBlocks();
           deserializeBlocksArray();
        }   
    }
    
    private void deserializeBlocksArray(){
         try {
             try (FileInputStream fileIn = new FileInputStream("blockArray.ser")) {
                 ObjectInputStream in = new ObjectInputStream(fileIn);
                 blockBotTop = (List<BlockIndex>) in.readObject();
                 in.close();
             }
      } catch (IOException i) {
         Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, i);         
      } catch (ClassNotFoundException c) {
         System.out.println("Class not found");
         Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, c);
      }
    }
    
    private void serializeBlocksArray(){
         try {
             try (FileOutputStream fileOut = new FileOutputStream("blockArray.ser")) {
                 ObjectOutputStream out = new ObjectOutputStream(fileOut);
                 out.writeObject(blockBotTop);
                 out.close();
             }
         System.out.printf("Serialized data is saved in blockArray.ser");
      } catch (IOException i) {
         i.printStackTrace();
      }
    }
    private String readBlockFromFile(String target){
        BlockIndex block = getBlock(target);
        
        if(block==null){
            return null;
        }
        int numberBlock = block.getBlock();

        FileReader fileReader;
        BufferedReader bufferedReader;
        String line;
        try {
            fileReader = new FileReader("indexer_sub_block"+numberBlock+".txt");
            bufferedReader = new BufferedReader(fileReader);
            
             while((line = bufferedReader.readLine()) != null) {
               if (line.split(",")[0].compareTo(target) == 0) {
                    return line;
               }
             }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
       
    }
    
    private BlockIndex getBlock(String term){
        for(BlockIndex block : blockBotTop){
            if(block.getTopString().compareTo(term)<=0 && term.compareTo(block.getBottomString())<=0){
                return block;
            }
        }
        return null;
    }
 
    
    public Map<Integer,Double> cosineScore(String query,boolean tokenizeSimple){
        Map<Integer,Double> score = new HashMap<>();
        
        List<String> queryTokens=null;
        
        if(tokenizeSimple){
            queryTokens = SimpleTokenizer.tokenize(query);
        }else{
            try {
                queryTokens = ImprovedTokenizer.personalizedTokenize(query);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String postingList;
        String[] postingListArray=null;
        double weightTD;
        int docId;
        double idf;
       
        for(String token:queryTokens){
            //raf.seek(binarySearch(token));
            //postingList =  raf.readLine().split(",");
            long startTime = System.currentTimeMillis();
            if((postingList = cache.get(token))!=null){
                  postingListArray = postingList.split(",");
            }else{
                postingList = readBlockFromFile(token);
                if(postingList!=null){
                    postingListArray=postingList.split(",");
                    cache.put(token, postingList);
                }
            }
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            //System.out.println("From block file. Search ElapseTime->"+elapsedTime+" target: "+ token);
            if(postingList!=null){
                idf=Math.log10(totalDocs/(postingListArray.length-1));
                for(int i = 1;i<postingListArray.length;i++){
                    weightTD = Double.parseDouble(postingListArray[i].split(":")[1].replaceAll("\\s+",""));
                    docId = Integer.parseInt(postingListArray[i].split(":")[0].replaceAll("\\s+",""));
                    if(score.containsKey(docId)){
                        score.put(docId, score.get(docId)+(weightTD*idf));
                    }else{
                        score.put(docId,(weightTD*idf));
                    }
                }
            }
        }
           
        return score;   
    }
    
    /*
    ###########################################################################################
    ###########################################################################################
                 THE NEXT COMMENT CODE WAS DONE FOR OTHER VERSIONS NOT SO EFFICIENT
    ###########################################################################################
    ###########################################################################################
    */
    
    
    
      /* private void findBottomTopBlock(){
        blockBotTop = new ArrayList<>();
        
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            int lineNumber=0;
            int totalBytesread=0;
            char currentChar='.';
            CharacterInf infTemp;
            int numberBlock = 0;
            BlockIndex blockTempIndex = null;
            FileWriter fw = new FileWriter("indexer_sub_block"+numberBlock+".txt");
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter writerBlock = new PrintWriter(bw);
            
            if((line = bufferedReader.readLine()) != null){
                blockBotTop.add(new BlockIndex(line.split(",")[0],lineNumber,totalBytesread, numberBlock));
                lineNumber++;
                totalBytesread+=line.length()+1;
                writerBlock.println(line);
             }
            String tempTerm=null;
            while((line = bufferedReader.readLine()) != null){
                writerBlock.println(line);
                if(lineNumber % BLOCK_SIZE==0){
                    System.out.println(line.split(",")[0]+" block Number: "+ numberBlock);
                    blockTempIndex = blockBotTop.get(blockBotTop.size()-1);
                    blockTempIndex.setBottomString(line.split(",")[0]);
                    blockTempIndex.setBottomLine(lineNumber);
                    blockTempIndex.setBottomLineBytes(totalBytesread);
                    
                    numberBlock++;
                    fw = new FileWriter("indexer_sub_block"+numberBlock+".txt");
                    bw = new BufferedWriter(fw);
                    writerBlock = new PrintWriter(bw);
                    
                    if((line = bufferedReader.readLine()) != null){
                        writerBlock.println(line);
                        lineNumber++;
                        totalBytesread+=line.length()+1;
                        blockBotTop.add(new BlockIndex(line.split(",")[0],lineNumber,totalBytesread,numberBlock));
                    }
                }
                lineNumber++;
                totalBytesread+=line.length()+1;
                tempTerm=line.split(",")[0];
            } 
             bufferedReader.close();
             blockTempIndex = blockBotTop.get(blockBotTop.size()-1);
             blockTempIndex.setBottomString(tempTerm);
             blockTempIndex.setBottomLine(lineNumber);
             blockTempIndex.setBottomLineBytes(totalBytesread);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(BlockIndex block : blockBotTop){
             System.out.println(block);
        }            
    }*/
    
    
 /*   private void readParamBlocks(){
        int numberBlock = 0;
        BufferedReader bufferedReader;
        String line;
        File f;
        String lastLine=null;
        BlockIndex blockTempIndex=null;
        blockBotTop = new ArrayList<>();
        try {
            do{
                   
                bufferedReader = new BufferedReader(new FileReader("indexer_sub_block"+numberBlock+".txt"));
                if((line = bufferedReader.readLine()) != null){
                    blockTempIndex = new BlockIndex(line.split(",")[0], numberBlock);
                }
                while((line = bufferedReader.readLine()) != null){
                   lastLine=line;
                }
                bufferedReader.close();
                blockTempIndex.setBottomString(lastLine.split(",")[0]);
          
                blockBotTop.add(blockTempIndex);
                numberBlock++;
                f = new File("indexer_sub_block"+numberBlock+".txt");      
            }while(f.exists() && !f.isDirectory());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (BlockIndex block : blockBotTop) {
            System.out.println(block);
        }
    }*/
    
       /*
    private void findBottomTopByLetter(){
        charBotTop = new HashMap<>();
        
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            int lineNumber=0;
            int totalBytesread=0;
            char currentChar='.';
            CharacterInf infTemp;
            while((line = bufferedReader.readLine()) != null){
                
                if(lineNumber!=0 && currentChar!=line.charAt(0)){
                    infTemp = charBotTop.get(currentChar);
                    infTemp.setBottom(totalBytesread);
                    infTemp.setBottomLine(lineNumber);
                    charBotTop.put(currentChar,infTemp);
                }
                if(!charBotTop.containsKey(line.charAt(0))){
                    currentChar = line.charAt(0);
                    charBotTop.put(currentChar,new CharacterInf(totalBytesread,lineNumber));
                }
                lineNumber++;
                totalBytesread+=line.length()+1;
            }
            infTemp = charBotTop.get(currentChar);
            infTemp.setBottom(totalBytesread);
            infTemp.setBottomLine(lineNumber);
            charBotTop.put(currentChar,infTemp);
            getBlock
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        }
        charBotTop.forEach((k,v)->System.out.println("k: "+k+" v: "+v));
        
        
    }*/
    
 /*   private String readBlock(String target){ //nao funciona
        BlockIndex block = getBlock(target);
        System.out.println("target: "+target);
        System.out.println("topByte: "+ block.getTopLineBytes()+ " bottomLine: "+block.getBottomLineBytes()+" top: "+ block.getTopLine() + " bottom: "+block.getBottomLine()+" bottom String: "+block.getBottomString()+" top String: "+block.getTopString());
        
             
        int buffSize=block.getBottomLineBytes()-block.getTopLineBytes();
        System.out.println(buffSize);
        byte[] bytes = new byte[buffSize];
        try {
            raf.read(bytes, block.getTopLineBytes(), buffSize);
            String blockString = new String(bytes);
            String[] termPostingList = blockString.split("\n");
            for (String termPosting : termPostingList) {
                System.out.println(termPosting.split(",")[0]);
                if (termPosting.split(",")[0].compareTo(target) == 0) {
                    return termPosting;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    */
  /*   private Long binarySearch(String target) throws IOException{
       long startTime = System.currentTimeMillis();
       BlockIndex block = getBlock(target) ;
       long top = block.getTopLineBytes(); 
       //long top = charBotTop.get(target.charAt(0)).getTop();
       long bottom = block.getBottomLineBytes();
       int cycles=0;
       //long bottom = charBotTop.get(target.charAt(0)).getBottom();
       
       System.out.println("top: "+ block.getTopLine() + " bottom: "+block.getBottomLine()+" bottom String: "+block.getBottomString()+" top String: "+block.getTopString());
        raf.seek(top);
        String termLine = raf.readLine().split(",")[0];
         //String termLine = raf.readLine();
        //first verification. the first line is skiped, so it is necessary to check before while loop.
        if (termLine == null || termLine.compareTo(target) >= 0) {
  
            return null;
        }

       
         //initial config to binary search
        //long top = 0;
        //long bottom = inicialBottom;
        
        
        while (top <= bottom) {
            //find mid point
            long mid = top + (bottom - top) / 2;
            //go to mid poit
            raf.seek(mid);
            //to advance to the beginning of the line
            raf.readLine();
            //to read the line and save the term
            termLine = raf.readLine().split(",")[0];
            //termLine = raf.readLine();
           // System.out.println(termLine);
            if (termLine == null || termLine.compareTo(target) >= 0) {
               
                bottom = mid - 1;
            } else {
               
                top = mid + 1;
            }
           cycles++; 
        }

        raf.seek(top);
        raf.readLine();
        
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("ElapseTime of BinarySearch->"+elapsedTime+" target: "+target);
        
        
        return raf.getFilePointer();
    }*/
     
     
 /*   private String sequentialSearch(String target){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            int countLine=0;
            
            int top = charBotTop.get(target.charAt(0)).getTopLine();
            System.out.println("top:"+top);
            int bottom = charBotTop.get(target.charAt(0)).getBottomLine();
            String line;
            while(bufferedReader.readLine()!=null){
               
                if(countLine>=top){
                    if(countLine<=bottom){
                          while((line = bufferedReader.readLine()) != null){
                            if(countLine<=bottom){  
                                if(line.split(",")[0].compareTo(target) == 0){
                                    return line;
                                }
                            }
                             countLine++;
                          }
                    }else{
                        break;
                    }
                }
                
                countLine++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RetrievalRanked.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }*/
    
   
    
}