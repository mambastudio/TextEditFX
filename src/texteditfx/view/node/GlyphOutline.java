/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditfx.view.node;

import glyphreader.core.FBound;
import glyphreader.core.FPoint2d;
import glyphreader.glyf.Glyph;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

/**
 *
 * @author user
 */
public class GlyphOutline {
    private final Glyph glyph;
    private final double size;
    
    public GlyphOutline(Glyph glyph, double size)
    {
        this.glyph = glyph;
        this.size = size;
    }
    
    public Group getAllOutlineCentered()
    {
        //get glyph centered in font bound
        Path path = getOutlineCentered();
        
        //get transformed font bound rectangle
        Rectangle fontRectangleBound = getTransformedFontRectangle();
        fontRectangleBound.setFill(null);
        fontRectangleBound.setStroke(new Color(0, 0, 0, 0)); //if you don't call this, bound is not calculated for rectangle
        
        return new Group(fontRectangleBound, path);
    }
    
    public Path getOutlineCentered()
    {
        Path path = getOutline();
        path.getTransforms().addAll(new Translate(glyph.fontBound.getWidth()/2 - glyph.glyphBound.getWidth()/2, 0));
        return path;
    }
    
    public Path getOutline()
    {
        Path path = getPath();
        
        if(!path.getElements().isEmpty())
        {
            //margin is space from top global bound to top glyph bound
            double marginY = minY() - glyph.fontBound.yMin;    
            //translate to left of bound (x = 0) and then height relative to baseline 
            path.getTransforms().setAll( getScale(), new Translate(-lsb(), -minY() - glyph.fontBound.getHeight() + marginY));
            //specific paint/color
            path.setFill(Color.BLACK);         
        }
        
        return path;
    }
    
    protected Scale getScale()
    {
        double fscale = size/glyph.unitsPerEm;
        Scale scale = new Scale();
        scale.setX(fscale);
        scale.setY(-fscale);         
        return scale;
    }
    
    protected Path getPath()
    {
        Path path = new Path();
        if(!glyph.isEmpty())
        {
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
                        path.getElements().add(moveTo);
                        break;
                    case 1:
                        if (point.onCurve) {        
                            LineTo lineTo = new LineTo();
                            lineTo.setX(point.x + x);
                            lineTo.setY(point.y + y);
                            path.getElements().add(lineTo);
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
                        path.getElements().add(quadCurveTo);
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
                        path.getElements().add(quadCurveTo);
                    }
                    contourStart = p + 1;
                    c += 1;
                    s = 0;
                }
            }
        }
        return path;
    }
        
    public Rectangle getTransformedFontRectangle()
    {
        Bounds bound = getTransformedFontBound();        
        return new Rectangle(bound.getWidth(), bound.getHeight());
    }
    
    protected Bounds getTransformedFontBound()
    {
        
        double fscale = size/glyph.unitsPerEm;  
        FBound fbounds = glyph.fontBound;
        return new BoundingBox(0, 0, fbounds.getWidth() * fscale, fbounds.getHeight() * fscale); 
    }
    
    public Bounds getBound()
    {        
        FBound bound = glyph.glyphBound.copy();
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
