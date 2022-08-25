/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import jfx.virtual.display.GridCell;
import texteditfx.view.node.NGlyphShape;

/**
 *
 * @author user
 */
public class GlyphCell extends GridCell<NGlyphShape> {
    
    private int index = -1;
    
    public GlyphCell()
    {
        
        init();
    }
    private void init()
    {
        setMinHeight(USE_PREF_SIZE);
        setMaxHeight(USE_PREF_SIZE);
        setPrefHeight(200);
        setPrefWidth(200);
        setMaxWidth(Double.MAX_VALUE);
                
        
    }
    
    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public void update(NGlyphShape label) {            
        getChildren().setAll(label);
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
