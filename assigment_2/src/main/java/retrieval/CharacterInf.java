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
public class CharacterInf {
    private long bottom;
    private final long top;
    private int lineBottom;
    private int lineTop;
    public CharacterInf(long top, int lineTop){
        this.top=top;
        this.lineTop=lineTop;
    }
    
    public long getBottom(){
        return bottom;
    }
    
    public long getTop(){
        return top;
    }
    public int getBottomLine(){
        return lineBottom;
    }
    
    public int getTopLine(){
        return lineTop;
    }
    public void setBottom(long bottom){
        this.bottom=bottom;
    }
    public void setBottomLine(int lineBottom){
            this.lineBottom=lineBottom;
    }
    public void setTopLine(int lineTop){
            this.lineTop=lineTop;
    }

    @Override
    public String toString() {
        return " Top: "+ top+" Bottom; "+bottom+ " TopLine: "+ lineTop+" BottomLine; "+lineBottom + " dif: " + (lineBottom-lineTop);//To change body of generated methods, choose Tools | Templates.
    }
    
      
}
