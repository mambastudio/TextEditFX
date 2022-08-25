/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditfx.view.node;

import glyphreader.Glyph;
import glyphreader.core.FBound;
import glyphreader.core.FPoint2d;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;

/**
 *
 * @author user
 */
public class NGlyphShape extends Path {
    private final Glyph glyph;
    
    public NGlyphShape(Glyph glyph)
    {
        this.glyph = glyph;
        this.initPath();
    }
    
    public Glyph getGlyph()
    {
        return glyph;
    }
    
    private void initPath()
    {
        if ( glyph == null) {
            return;
        }
        
        int x = 0, y = 0;
        
        int s = 0,
            p = 0,
            c = 0,
            contourStart = 0;
        
        FPoint2d  prev;
                               
        for (; p < glyph.points.size(); p++) {          
            FPoint2d point = glyph.points.get(p);
            
            switch (s) {
                case 0:      
                    MoveTo moveTo = new MoveTo();
                    moveTo.setX(point.x + x);
                    moveTo.setY(point.y + y);
                    s = 1;
                    getElements().add(moveTo);
                    break;
                case 1:
                    if (point.onCurve) {        
                        LineTo lineTo = new LineTo();
                        lineTo.setX(point.x + x);
                        lineTo.setY(point.y + y);
                        getElements().add(lineTo);
                    } 
                    else {
                        s = 2;
                    }   
                    break;
                default:
                    prev = glyph.points.get(p - 1);
                    QuadCurveTo quadCurveTo = new QuadCurveTo();
                    if (point.onCurve) {
                        quadCurveTo.setControlX(prev.x + x);
                        quadCurveTo.setControlY(prev.y + y);
                        quadCurveTo.setX(point.x + x);
                        quadCurveTo.setY(point.y + y);                        
                        s = 1;
                    } else {
                        quadCurveTo.setControlX(prev.x + x);
                        quadCurveTo.setControlY(prev.y + y);
                        quadCurveTo.setX((prev.x + point.x) / 2 + x);
                        quadCurveTo.setY((prev.y + point.y) / 2 + y);                       
                    }
                    getElements().add(quadCurveTo);
                    break;
            }
            if (p == glyph.contourEnds.get(c)) {
                if (s == 2) { // final point was off-curve. connect to start
                    prev = point;
                    point = glyph.points.get(contourStart);
                    QuadCurveTo quadCurveTo = new QuadCurveTo();
                    if (point.onCurve) {
                        quadCurveTo.setControlX(prev.x + x);
                        quadCurveTo.setControlY(prev.y + y);
                        quadCurveTo.setX(point.x + x);
                        quadCurveTo.setY(point.y + y);                           
                    } else {
                        quadCurveTo.setControlX(prev.x + x);
                        quadCurveTo.setControlY(prev.y + y);
                        quadCurveTo.setX((prev.x + point.x) / 2 + x);
                        quadCurveTo.setY((prev.y + point.y) / 2 + y);       
                    }
                    getElements().add(quadCurveTo);
                }
                contourStart = p + 1;
                c += 1;
                s = 0;
            }
        }
    }
    
    public Bounds getBound()
    {        
        FBound bound = glyph.bound.copy();
        return new BoundingBox(bound.xMin, bound.yMin, bound.getWidth(), bound.getHeight());
    }
    
    public double lsb()
    {
        return glyph.getGlyphMetrics().leftSideBearing();
    }
    
    public double advanceWidth()
    {
        return glyph.getGlyphMetrics().advanceWidth();
    }
    
    public double minX()
    {
        return getBound().getMinX();
    }
    
    public double minY()
    {
        return getBound().getMinY();
    }
    
    public double width()
    {
        return getBound().getWidth();
    }
    
    public double height()
    {
        return getBound().getHeight();
    }
    
    public boolean isNull()
    {
        return glyph == null;
    }
}
