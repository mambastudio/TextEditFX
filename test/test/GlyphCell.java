/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javafx.scene.Node;
import jfx.virtual.display.GridCell;

/**
 *
 * @author user
 */
public class GlyphCell extends GridCell<Node> {
    
    private int index = -1;
    
    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public void update(Node label) {  
        if(label != null)
            getChildren().setAll(label);
        else
            getChildren().removeAll(getChildren());
    }   
    
    @Override
    public void updateIndex(int index)
    {
        this.index = index;
    }
    
    @Override
    public int getIndex()
    {
        return this.index;
    }
}
