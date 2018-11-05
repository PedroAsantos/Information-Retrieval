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
    private int bottom;
    private final int top;
    
    public CharacterInf(int top){
        this.top=top;
    }
    
    public int getBottom(){
        return bottom;
    }
    
    public int getTop(){
        return top;
    }
    
    public void setBottom(int bottom){
        this.bottom=bottom;
    }

    @Override
    public String toString() {
        return " Top: "+ top+" Bottom; "+bottom;//To change body of generated methods, choose Tools | Templates.
    }
    
      
}
