/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

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
    private NGlyphShape shapeGlyph = null;
    
    public GlyphCell()
    {
        Rectangle rect = new Rectangle();             
        rect.setX(0);
        rect.setY(0);
        rect.setWidth(90);
        rect.setHeight(90);     
        rect.setFill(null);
        rect.setStroke(Color.BLACK);
        
        shapeGlyph = new NGlyphShape(null);
        getChildren().addAll(rect, shapeGlyph);
    }
    
    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public void update(NGlyphShape shapeGlyph) {            
        this.shapeGlyph.setGlyph(shapeGlyph.getGlyph());
        this.shapeGlyph.getTransforms().setAll(shapeGlyph.getTransforms());
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
