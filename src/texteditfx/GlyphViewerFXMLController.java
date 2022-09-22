/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditfx;

import glyphreader.fonts.notoserif.Resource;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jfx.virtual.display.GridCell;
import jfx.virtual.display.GridDisplay;
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

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        NGlyphVector vector = NGlyphVector.getGlyphVector(Resource.class, "NotoSerif-Regular.ttf");
        vector.setSize(30);
        
        
        ObservableList<NGlyphShape> shapes = FXCollections.observableArrayList();
        
        for(int i = 0; i<vector.getCount(); i++)
        { 
            NGlyphShape shape = vector.getGlyphShapeCentered(i);
            shapes.add(shape);            
        }
        
        GridDisplay<NGlyphShape> grid = new GridDisplay(); 
        //override default grid cell
        grid.setCellFactory(g->{
            GlyphCell cell = new GlyphCell();            
            cell.update(g);
            return cell;
        });
        grid.setItems(shapes);
        
        basePane.getChildren().addAll(grid);
    }    
    
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
            
            this.setOnMouseEntered(e->{
                rect.setFill(new Color(0.5, 0.5, 0.5, 0.5));
            });
            this.setOnMouseExited(e->{
                rect.setFill(null);
            });
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
