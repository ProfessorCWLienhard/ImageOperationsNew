/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageoperations;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author 55clienhar
 */
public class ImageOperations extends Application {
        Double firstx = 0.0;
        Double firsty = 0.0;
        Boolean firstPoint = true;
        Double firstMovex = 0.0;
        Double firstMovey = 0.0;
        Boolean firstMovePoint = true;
        Rectangle rect1;
        Canvas c1;
        Group group1;
        Rectangle moveRect;
        ImageView iv1;
        
    @Override
    public void start(Stage primaryStage) {

        ScrollPane sc1 = new ScrollPane();
        group1 = new Group();
        c1 =ImageRoutines.setupCanvas(2000., 1500.);
        GraphicsContext g1 = drawBasicOval(200, 200, 100, 50);  
        group1.getChildren().add(0, c1);
        sc1.setContent(group1);

        moveRect = ImageRoutines.setupDefaultMoveRectangle();
        
        c1.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if(c1.getCursor() == Cursor.CROSSHAIR){
                    if(firstPoint){
                        firstPoint = false;
                        firstx = me.getX();
                        firsty = me.getY();
                    }
                    rect1 = ImageRoutines.findTranslationRectangle(firstx, firsty, me.getX(), me.getY());
                    group1.getChildren().remove(1);
                    group1.getChildren().add(1, rect1);
                }
            }
        });
        
        c1.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                
               firstPoint = true;
               
            }
        });
        
        c1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                
                if(group1.getChildren().size() > 2){
                    g1.drawImage(iv1.getImage(), moveRect.getX(), moveRect.getY(), moveRect.getWidth(), moveRect.getHeight());
                    group1.getChildren().remove(3);
                    group1.getChildren().remove(2);
                    c1.setCursor(Cursor.DEFAULT);
                    firstMovePoint = true;
                }
                if(group1.getChildren().size() > 1){
                    group1.getChildren().remove(1);
                    c1.setCursor(Cursor.DEFAULT);
                }
               
            }
        });

        moveRect.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if(moveRect.getCursor() == Cursor.CLOSED_HAND){
                    c1.setCursor(Cursor.CLOSED_HAND);
                    Double scX = me.getX();
                    Double scY = me.getY();
                    Double scDX;
                    Double scDY;
                    if(firstMovePoint){
                        firstMovePoint = false;
                        scDX = 0.0;
                        scDY = 0.0;
                        iv1.setVisible(true);
                        g1.clearRect(rect1.getX(), rect1.getY(), rect1.getWidth(), rect1.getHeight());
                        rect1.setVisible(false);
                    }
                    else{
                        scDX = scX - firstMovex;
                        scDY = scY - firstMovey;
                    }
                    firstMovex = scX;
                    firstMovey = scY;
                    
                    ImageRoutines.moveImage(moveRect, iv1, scDX, scDY);
               }

            }
        });
        
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(secondMenuBar());
        borderPane.setCenter(sc1);
        
        Scene scene = new Scene(borderPane, 400, 400);
        
        primaryStage.setTitle("Select Image Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private GraphicsContext drawBasicOval(int d1, int d2, int d3, int d4){
        GraphicsContext g1 = c1.getGraphicsContext2D();
        g1.setFill(Color.BLACK);
        g1.fillOval(d1, d2, d3, d4);
        return g1;
    }
    
        public MenuBar secondMenuBar() {
            MenuBar myBar = new MenuBar();
            final Menu editMenu = new Menu("Edit");
            myBar.getMenus().addAll(editMenu);
            
            MenuItem copy = new MenuItem("Copy");
            copy.setOnAction((ActionEvent e) -> {
                
                if(group1.getChildren().size() > 2){
                    ImageRoutines.transferImageToClipbpard(iv1.getImage());
                }
                
                if(group1.getChildren().size() == 2){
                    ImageRoutines.transferImageToClipbpard(c1.snapshot(ImageRoutines.setupSnapshotParameters(rect1), null));
                }
                
            });
            editMenu.getItems().add(copy);

            MenuItem paste = new MenuItem("Paste");
            paste.setOnAction((ActionEvent e) -> {
                if(group1.getChildren().size() > 2){
                    c1.getGraphicsContext2D().drawImage(iv1.getImage(), moveRect.getX(), moveRect.getY(), moveRect.getWidth(), moveRect.getHeight());
                    group1.getChildren().remove(3);
                    group1.getChildren().remove(2);
                    c1.setCursor(Cursor.DEFAULT);
                    firstMovePoint = true;
                }
                if(group1.getChildren().size() > 1){
                    group1.getChildren().remove(1);
                }
                
                Image image2 = ImageRoutines.transferImageFromClipbpard();
                WritableImage wImage = ImageRoutines.changeImageToWriteable(image2);
                iv1 = ImageRoutines.setupImageView(wImage, true);
                    
                rect1 = ImageRoutines.setupDefaultRectangle();
                group1.getChildren().add(1, rect1);

                ImageRoutines.setupMoveRectangle(image2, moveRect);
                group1.getChildren().add(2, iv1);
                    
                moveRect.setVisible(true);
                group1.getChildren().add(3, moveRect);

            });
            editMenu.getItems().add(paste);
            
            final Menu toolsMenu = new Menu("Tools");
            myBar.getMenus().addAll(toolsMenu);
            MenuItem selection = new MenuItem("Selection");
            MenuItem moveTool = new MenuItem("Move");
            MenuItem pointer = new MenuItem("Pointer");
            toolsMenu.getItems().add(selection);
            toolsMenu.getItems().add(moveTool);
            toolsMenu.getItems().add(pointer);
            
            selection.setOnAction((ActionEvent e) -> {

                if(group1.getChildren().size() > 2){
                    c1.getGraphicsContext2D().drawImage(iv1.getImage(), moveRect.getX(), moveRect.getY(), moveRect.getWidth(), moveRect.getHeight());
                    group1.getChildren().remove(3);
                    group1.getChildren().remove(2);
                    c1.setCursor(Cursor.CROSSHAIR);
                    firstMovePoint = true;
                }
                if(group1.getChildren().size() < 2){
               
                    Rectangle rect2 = new Rectangle(0, 0);
                    rect2.setFill(Color.TRANSPARENT);
                    rect2.setStroke(Color.RED);
                    group1.getChildren().add(1, rect2);
                }
                c1.setCursor(Cursor.CROSSHAIR);
                
            });
            
            moveTool.setOnAction((ActionEvent e) -> {
                
                if(group1.getChildren().size() == 2){
                    ImageRoutines.findMoveRectangle(rect1, moveRect);
                    Image i1 = c1.snapshot(ImageRoutines.setupSnapshotParameters(moveRect), null);
                    iv1 = ImageRoutines.setupImageView(i1, false);

                    group1.getChildren().add(2, iv1);
                    
                    moveRect.setVisible(true);
                    group1.getChildren().add(3, moveRect);

                }
            });
            
            pointer.setOnAction((ActionEvent e) -> {

                if(group1.getChildren().size() > 2){
                    c1.getGraphicsContext2D().drawImage(iv1.getImage(), moveRect.getX(), moveRect.getY(), moveRect.getWidth(), moveRect.getHeight());
                    group1.getChildren().remove(3);
                    group1.getChildren().remove(2);
                    firstMovePoint = true;
                }
                
                if(group1.getChildren().size() > 1){
                    group1.getChildren().remove(1);
                }
                
                c1.setCursor(Cursor.DEFAULT);
 
            });
            
            return myBar;

        }    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
