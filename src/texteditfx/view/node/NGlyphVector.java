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
    
    public NGlyphShape getGlyphShapeScaled(int index)
    {
        NGlyphShape shape = new NGlyphShape(ttf.getGlyph(index));          
        shape.getTransforms().setAll( getScaleInGlobalBound(), new Translate(-shape.lsb(), -shape.minY() - shape.height()));
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
        translate.setY(-ttf.getFontMetrics().getAscent()); //translates down since scale is negative in y direction
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
    
    public Node getGlyphGlobalDisplay(int index)
    {       
        
        Bounds bound = this.getDirectScaledBound();
        Rectangle rect = new Rectangle();
       
        Bounds b = this.getDirectScaledBound();
        rect.setX(0);
        rect.setY(0);
        rect.setWidth(b.getWidth());
        rect.setHeight(b.getHeight());     
        rect.setFill(null);
        rect.setStroke(Color.BLACK);
        
        
        Pane pane = new Pane();
        pane.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        pane.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        pane.setPrefSize(bound.getWidth()+10, bound.getHeight()+10); System.out.println(bound);
        
        NGlyphShape shape = this.getGlyphShapeScaled(index);
        
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
