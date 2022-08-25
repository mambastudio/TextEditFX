/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import glyphreader.fonts.notoserif.Resource;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import texteditfx.view.node.NGlyphVector;

/**
 *
 * @author user
 */
public class TestFont extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane baseDrawPanel = new StackPane();
        
        //three quarter size of the screen monitor
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();        
        Scene scene = new Scene(baseDrawPanel, screenBounds.getWidth() * 0.95, screenBounds.getHeight() * 0.85);
        
        NGlyphVector vector = NGlyphVector.getGlyphVector(Resource.class, "NotoSerif-Regular.ttf");
        vector.setSize(400);
        
        baseDrawPanel.getChildren().add(vector.getGlyphGlobalDisplay(2035));
        
        primaryStage.setScene(scene);
       // primaryStage.setMaximized(true);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
