/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditfx.view.node;

import texteditfx.view.node.NGlyphShape;
import glyphreader.TrueTypeFont;
import glyphreader.core.FBound;
import glyphreader.core.metrics.FGlyphMetrics;
import glyphreader.core.metrics.FHorizontalMetrics;
import java.nio.file.Path;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

/**
 *
 * @author user
 */
public class NGlyphVector {
    
    public enum BoundType{GLOBAL_BOUND, LOCAL_BOUND};
    
    private final TrueTypeFont ttf;        
    
    private int size = 18;
    private Paint paint = Color.BLACK;
    
    private NGlyphVector(TrueTypeFont ttf)
    {
        this.ttf = ttf;
    }
    
    public void setColor(Paint paint)
    {
        this.paint = paint;
    }
    
    public void setSize(int size)
    {
        this.size = size;
    }
    
    public FGlyphMetrics getGlyphMetrics(int index)
    {
        return ttf.getGlyphMetrics(index);
    }
 
    public NGlyphShape getGlyphShapeInGlobalBound(int index)
    {
        NGlyphShape shape = new NGlyphShape(ttf.getGlyph(index));        
        if(shape.isNull())
            return shape;
                      
        shape.getTransforms().setAll(getTransformsInGlobalBound());
                               
        shape.setFill(paint);        
        return shape;
    }
    
    protected Bounds getGlobalBounds()
    {
        FBound fbounds = ttf.getBound();        
        return new BoundingBox(fbounds.xMin, fbounds.yMin, fbounds.xMax, fbounds.yMax); 
    }
        
    protected Scale getScaleInGlobalBound()
    {
        double fscale = size/ttf.getUnitsPerEm();        
        Scale scale = new Scale();
        scale.setX(fscale);
        scale.setY(-fscale);         
        return scale;
    }
    
    protected Translate getTranslateInGlobalBound()
    {
        Translate translate = new Translate();
        Bounds bounds = this.getGlobalBounds();        
        translate.setX(-bounds.getMinX());
        translate.setY(-ttf.getFontMetrics().getAscent()); //translates down since scale is negative in y direction
        return translate;
    }
    
    protected ObservableList<Transform> getTransformsInGlobalBound()
    {
        ObservableList<Transform> transforms = FXCollections.observableArrayList();
        transforms.addAll(getScaleInGlobalBound(), getTranslateInGlobalBound());
        return transforms;
    }
    
    public Node getGlyphGlobalDisplay(int index)
    {       double fscale = size/ttf.getUnitsPerEm();  
        Bounds bound = getGlobalBounds();
        Rectangle rect = new Rectangle(); 
        rect.setX(bound.getMinX()); 
        rect.setY(bound.getMinY());
        rect.setWidth(bound.getWidth());
        rect.setHeight(bound.getHeight()); 
        
        
        
        NGlyphShape shape = new NGlyphShape(ttf.getGlyph(index)); 
        if(!shape.isNull())
        {
            Bounds sBound = shape.getBound();
            Point2D midB = new Point2D(bound.getMinX(), bound.getMinY()).midpoint(new Point2D(bound.getMaxX(), bound.getMaxY()));
            Point2D midS = new Point2D(sBound.getMinX(), sBound.getMinY()).midpoint(new Point2D(sBound.getMaxX(), sBound.getMaxY()));
            rect.getTransforms().setAll(new Scale(fscale, fscale, midB.getX(), midB.getY()), new Translate(-bound.getMinX(), bound.getHeight()));  
        }
        
        
        
        
        
        
        shape.getTransforms().setAll(getTransformsInGlobalBound());
        
        System.out.println(index);
        System.out.println(new Group(shape).getBoundsInLocal());
        System.out.println(new Group(rect).getBoundsInLocal());
       
        
        return new Pane(rect, shape);
    }
    
    public int getCount()
    {
        return ttf.length;
    }
        
    public static NGlyphVector getGlyphVector(TrueTypeFont ttf)
    {
        return new NGlyphVector(ttf);
    }
    
    public static NGlyphVector getGlyphVector(Path path)
    {
        return new NGlyphVector(new TrueTypeFont(path));
    }
    
    public static NGlyphVector getGlyphVector(Class<?> clazz, String fileName)
    {
        return new NGlyphVector(new TrueTypeFont(clazz, fileName));
    }
}
