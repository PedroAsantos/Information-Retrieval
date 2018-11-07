/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrieval;

/**
 *
 * @author rute
 */
public class BlockIndex {
    private String topString;
    private String bottomString;
    private int block;
    
    private int topLine;
    private int bottomLine;
    private int bottomLineBytes;
    private int topLineBytes;
   
    public BlockIndex(String topString, int topLine,int topLineBytes,int block){
        this.topString=topString;
        this.topLine=topLine;
        this.topLineBytes=topLineBytes;
        this.block=block;
    }
    public BlockIndex(String topString, int block){
        this.topString=topString;
        this.block=block;
    }
    public void setBottomLineBytes(int bottomLineBytes){
        this.bottomLineBytes=bottomLineBytes;
    }
    public int getBottomLineBytes(){
        return bottomLineBytes;
    }
    public int getTopLineBytes(){
        return topLineBytes;
    }
    public void setBottomString(String bottomString){
        this.bottomString=bottomString;
    }
    public void setTopString(String topString){
        this.topString=topString;
    }
    public void setTopLine(int topLine){
        this.topLine=topLine;
    }
    public void setBottomLine(int bottomLine){
        this.bottomLine=bottomLine;
    }
    
    public int getTopLine(){
        return topLine;
    }
    public String getTopString(){
        return topString;
    }
    public String getBottomString(){
        return bottomString;
    }
    public int getBottomLine(){
        return bottomLine;
    }
    public void setBlock(int block){
        this.block=block;
    }
    public int getBlock(){
        return block;
    }
    @Override
    public String toString() {
        return "Block: "+ block+" topString: "+topString+ " topByte: " +topLineBytes+" bottomByte: "+ bottomLineBytes +" topLine: "+topLine+" bottomString: "+bottomString+" bottomLine: "+bottomLine; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
