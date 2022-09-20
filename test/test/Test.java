/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import glyphreader.fonts.notoserif.Resource;
import texteditfx.view.node.NGlyphVector;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jfx.virtual.display.GridDisplay;
import texteditfx.view.node.NGlyphShape;

/**
 *
 * @author user
 */
public class Test extends Application{
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        StackPane baseDrawPanel = new StackPane();
       
        //three quarter size of the screen monitor
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();        
        Scene scene = new Scene(baseDrawPanel, screenBounds.getWidth() * 0.95, screenBounds.getHeight() * 0.85);
        
        NGlyphVector vector = NGlyphVector.getGlyphVector(Resource.class, "Phosphor.ttf");
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
      
        
        
        baseDrawPanel.getChildren().addAll(grid);
        
        
        
        
        primaryStage.setScene(scene);
       // primaryStage.setMaximized(true);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
  
    
   
    
   
}
