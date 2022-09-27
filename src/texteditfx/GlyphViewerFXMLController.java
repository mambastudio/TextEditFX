/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditfx;

import glyphreader.FontType;
import glyphreader.fonts.notoserif.Resource;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jfx.virtual.display.GridCell;
import jfx.virtual.display.GridDisplay;
import texteditfx.view.node.GlyphOutline;
import texteditfx.view.node.NGlyphShape;
import texteditfx.view.node.NGlyphVector;

/**
 * FXML Controller class
 *
 * @author user
 */
public class GlyphViewerFXMLController implements Initializable {
    @FXML
    StackPane basePane;
    @FXML
    ListView<FontType> systemFontList;
    
    GridDisplay<GlyphOutline> grid;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO        
        grid = new GridDisplay(); 
        //override default grid cell
        grid.setCellFactory(g->{
            GlyphCell cell = new GlyphCell();            
            cell.update(g);
            return cell;
        });        
        basePane.getChildren().addAll(grid);
        
        NGlyphVector vector = NGlyphVector.getGlyphVector(Resource.class, "NotoSerif-Regular.ttf");
        vector.setSize(30);
        initGlyphs(vector);
        
        systemFontList.getItems().addAll(FontType.getAllSystemFonts(30));
        systemFontList.getSelectionModel().selectedItemProperty().addListener((o, ov, nv)->{
            if(nv != null)
            {
                NGlyphVector newGlyphVector = NGlyphVector.getGlyphVector(nv.getTTFInfo());
                newGlyphVector.setSize(30);
                initGlyphs(newGlyphVector);
            }
        });
    }    
    
    protected void initGlyphs(NGlyphVector glyphVector)
    {
        ObservableList<GlyphOutline> shapes = FXCollections.observableArrayList();
        
        for(int i = 0; i<glyphVector.getCount(); i++)
        { 
            GlyphOutline shape = glyphVector.getGlyphOutline(i);
            shapes.add(shape);            
        }
        
        grid.setItems(shapes);
    }
    
    public class GlyphCell extends GridCell<GlyphOutline> {    
        private int index = -1;        
        Group group;
        
        public GlyphCell()
        {
            group = new Group();
            getChildren().setAll(group);

            this.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
            
            //this.setOnMouseEntered(e->{
            //    rect.setFill(new Color(0.5, 0.5, 0.5, 0.5));
            //});
            //this.setOnMouseExited(e->{
            //    rect.setFill(null);
            //});
        }

        @Override
        public Node getNode() {
            return this;
        }

        @Override
        public void update(GlyphOutline glyph) {          
            if(glyph != null)
            {
                group.getChildren().setAll(glyph.getAllOutlineCentered());
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
}
