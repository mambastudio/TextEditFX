/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditfx.view.node;

import glyphreader.TrueTypeFont;
import glyphreader.core.FBound;
import glyphreader.core.metrics.FGlyphMetrics;
import java.nio.file.Path;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
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
    
    /**
     * Shape is located in the left most part of the global bound for all glyphs.
     * The y coordinates are based on the description of the font hence base line for this font is the common base line for all glyphs.
     *  
     * new Translate(-shape.lsb(), -shape.minY() - getGlobalBounds().getHeight() + marginY);
     * 
     * @param index
     * @return 
     */
    public NGlyphShape getGlyphShape(int index)
    {
        //glyph shape path/outline
        NGlyphShape shape = new NGlyphShape(ttf.getGlyph(index)); 
        if(shape.isNull())
            return shape;
        //margin is space from top global bound to top glyph bound
        double marginY = shape.minY() - ttf.getBound().yMin;    
        //translate to left of bound (x = 0) and then height relative to baseline 
        shape.getTransforms().setAll( getScaleInGlobalBound(), new Translate(-shape.lsb(), -shape.minY() - getGlobalBounds().getHeight() + marginY));
        //specific paint/color
        shape.setFill(paint);       
        return shape;
    }
    
    public NGlyphShape getGlyphShapeCentered(int index)
    {
        NGlyphShape shape = this.getGlyphShape(index);     
        if(!shape.isNull())
            shape.getTransforms().addAll(new Translate(ttf.getBound().getWidth()/2 - shape.width()/2, 0)); 
        shape.setBoundRectangle(getFontBoundRectangle());
        return shape;
    }
    
    public Rectangle getFontBoundRectangle()
    {
        Bounds bound = getDirectScaledBound();
        return new Rectangle(bound.getWidth(), bound.getHeight());
    }
    
    protected Bounds getGlobalBounds()
    {
        FBound fbounds = ttf.getBound();           
        return new BoundingBox(fbounds.xMin, fbounds.yMin, fbounds.xMax - fbounds.xMin, fbounds.yMax - fbounds.yMin); 
    }
        
    protected Scale getScaleInGlobalBound()
    {
        double fscale = size/ttf.getUnitsPerEm();        
        Scale scale = new Scale();
        scale.setX(fscale);
        scale.setY(-fscale);         
        return scale;
    }
    
    protected Scale getScaleInGlobalBound2()
    {
        double fscale = size/ttf.getUnitsPerEm();        
        Scale scale = new Scale();
        scale.setX(fscale);
        scale.setY(fscale);         
        return scale;
    }
    
    protected Scale getDirectScale()
    {
        double fscale = size/ttf.getUnitsPerEm();        
        Scale scale = new Scale();
        scale.setX(fscale);
        scale.setY(fscale);         
        return scale;
    }
    
    protected Bounds getDirectScaledBound()
    {
        double fscale = size/ttf.getUnitsPerEm();  
        FBound fbounds = ttf.getBound();        
        return new BoundingBox(0, 0, fbounds.getWidth() * fscale, fbounds.getHeight() * fscale); 
    }
    
    
    
    
    protected Translate getTranslateInGlobalBound()
    {
        Translate translate = new Translate();
        Bounds bounds = this.getGlobalBounds();        
        translate.setX(-bounds.getMinX());
        translate.setY(-ttf.getFontHorizontalMetrics().getAscent()); //translates down since scale is negative in y direction
        return translate;
    }
    
    protected Translate getTranslateInGlobalBound2()
    {
        Translate translate = new Translate();
        Bounds bounds = this.getGlobalBounds();        
        translate.setX(-bounds.getMinX());
        translate.setY(bounds.getHeight()/2 ); 
        return translate;
    }
    
    protected ObservableList<Transform> getTransformsInGlobalBound()
    {
        ObservableList<Transform> transforms = FXCollections.observableArrayList();
        transforms.addAll(getScaleInGlobalBound(), getTranslateInGlobalBound());
        return transforms;
    }
    
    public Node getGlyphDisplayAt(int index)
    {       
        Bounds bound = this.getGlobalBounds();
        Bounds scaledBound = this.getDirectScaledBound();
        
        Rectangle rec = new Rectangle();             
        rec.setX(bound.getMinX());
        rec.setY(bound.getMinY());
        rec.setWidth(bound.getWidth());
        rec.setHeight(bound.getHeight());   
        rec.getTransforms().setAll(this.getScaleInGlobalBound());
        
        
        Rectangle rect = new Rectangle();             
        rect.setX(0);
        rect.setY(0);
        rect.setWidth(scaledBound.getWidth());
        rect.setHeight(scaledBound.getHeight());     
        rect.setFill(null);
        rect.setStroke(Color.BLACK);
        
        
        Pane pane = new Pane();
        pane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        pane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        pane.setPrefSize(scaledBound.getWidth()+10, scaledBound.getHeight()+10); 
        
        NGlyphShape shape = this.getGlyphShape(index);     
        if(!shape.isNull())
            shape.getTransforms().addAll(new Translate(ttf.getBound().getWidth()/2 - shape.width()/2, 0));         
        pane.getChildren().addAll(rect, shape);
        
       
        return pane;
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
