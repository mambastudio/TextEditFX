/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jfx.virtual.display.GridCell;
import texteditfx.view.node.NGlyphShape;

/**
 *
 * @author user
 */
public class GlyphCell extends GridCell<NGlyphShape> {
    
    private int index = -1;
    private final NGlyphShape shapeGlyph;
    Group group;
    Rectangle rect = new Rectangle();             
    public GlyphCell()
    {
        
        rect.setX(0);
        rect.setY(0);
        rect.setWidth(50);
        rect.setHeight(50);     
        rect.setFill(null);
        rect.setStrokeWidth(0.0009);
        rect.setStroke(Color.BLACK);
        
        shapeGlyph = new NGlyphShape(null);
        
        group = new Group(rect, shapeGlyph);
     
        getChildren().setAll(group);
        
        this.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
    }
    
    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public void update(NGlyphShape glyph) {          
        if(glyph != null)
        {
            group.getChildren().setAll(rect, glyph);
            
            //this.setPrefSize(50, 50);
        }
        else 
            group.getChildren().clear();
       
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