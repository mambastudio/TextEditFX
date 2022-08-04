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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

/**
 *
 * @author user
 */
public class NGlyphVector {
    
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
    
    //Ignores line space. Metrics specific to glyph
    public NGlyphShape getGlyphOutline(int index)
    {
        NGlyphShape shape = new NGlyphShape(ttf.getGlyph(index));        
        double fscale = size/ttf.getUnitsPerEm();
        FGlyphMetrics gmetrics = shape.getGlyph().getGlyphMetrics();
        
        Scale scale = new Scale();
        scale.setX(fscale);
        scale.setY(-fscale);  
        
        Translate translate = new Translate();
        translate.setX(gmetrics.leftSideBearing());
        translate.setY(shape.getGlyph().bound.yMin - shape.getGlyph().bound.getHeight());    //local bounds
        
        shape.getTransforms().addAll(scale, translate);
        
        shape.setFill(paint);
        
        return shape;
    }
    
    
    
    public static NGlyphVector getGlyphVector(TrueTypeFont ttf)
    {
        return new NGlyphVector(ttf);
    }
    
    public static NGlyphVector getGlyphVector(Path path)
    {
        return new NGlyphVector(new TrueTypeFont(path));
    }
    
    
}
