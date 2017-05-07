/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageoperations;

import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.*;
import javafx.scene.input.DataFormat;

/**
 *
 * @author Clarence
 */
public class ImageRoutines {
    static Canvas setupCanvas(double canvasWidth, double canvasHeight){
        Canvas c1 = new Canvas();
        c1.setWidth(canvasWidth);
        c1.setHeight(canvasHeight);
        return c1;
    }
    
    static Rectangle setupDefaultRectangle(){
        Rectangle defaultRectangle = new Rectangle(0, 0);
        defaultRectangle.setX(0);
        defaultRectangle.setY(0);
        defaultRectangle.setFill(Color.TRANSPARENT);
        defaultRectangle.setStroke(Color.BLACK);
        return defaultRectangle;
    }
    
    static Rectangle findTranslationRectangle(double firstx, double firsty, double x, double y){
        Double ucX = firstx;
        Double ucY = firsty;
        Double scX = x;
        Double scY = y;
        Double scDX = scX - firstx;
        Double scDY = scY - firsty;
                
        if(scDX < 0){
            ucX = scX;
            scDX = Math.abs(scDX);
        }
        if(scDY < 0){
            ucY = scY;
            scDY = Math.abs(scDY);
        }
                 
        Rectangle translationRectangle = new Rectangle(scDX, scDY);
        translationRectangle.setX(ucX);
        translationRectangle.setY(ucY);
        translationRectangle.setFill(Color.TRANSPARENT);
        translationRectangle.setStroke(Color.BLACK);
	
        return translationRectangle;
    }
    
    static  void transferImageToClipbpard(Image image1){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putImage(image1);
        clipboard.setContent(content);                   
    }
    
    static Image transferImageFromClipbpard(){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        Image image2 = (Image) clipboard.getContent(DataFormat.IMAGE);
        return image2;
    }
    
    static WritableImage changeImageToWriteable(Image image2){

        PixelReader pixelReader = image2.getPixelReader();
        WritableImage wImage = new WritableImage(
                                    (int)image2.getWidth(),
                                    (int)image2.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();
        for(int readY=0;readY<image2.getHeight();readY++){
            for(int readX=0; readX<image2.getWidth();readX++){
                Color color = pixelReader.getColor(readX,readY);
                int red1 = (int) (255 * color.getRed());
                int green1 = (int) (255 * color.getGreen());
                int blue1 = (int) (255 * color.getBlue());
                Color color1 = color.rgb((int) (255 * color.getRed()),(int) (255 * color.getGreen()), (int) (255 * color.getBlue()),1.0);
                pixelWriter.setColor(readX,readY,color1);
             }
        }
        
        return wImage;
    }
    
    static void setupMoveRectangle(Image image2, Rectangle moveRectangle){
        moveRectangle.setFill(Color.TRANSPARENT);
        moveRectangle.setStroke(Color.RED);
        moveRectangle.setCursor(Cursor.CLOSED_HAND);
        moveRectangle.setX(0);
        moveRectangle.setY(0);
        moveRectangle.setWidth(image2.getWidth());
        moveRectangle.setHeight(image2.getHeight());
    } 
    
    static void findMoveRectangle(Rectangle selectionRectangle, Rectangle moveRectangle){
        moveRectangle.setFill(Color.TRANSPARENT);
        moveRectangle.setStroke(Color.RED);
        moveRectangle.setCursor(Cursor.CLOSED_HAND);
        moveRectangle.setX(selectionRectangle.getX());
        moveRectangle.setY(selectionRectangle.getY());
        moveRectangle.setWidth(selectionRectangle.getWidth());
        moveRectangle.setHeight(selectionRectangle.getHeight());
    } 
    
    static SnapshotParameters setupSnapshotParameters(Rectangle selectionRectangle){
        SnapshotParameters SSP1 = new SnapshotParameters();
        Rectangle2D r1 = new Rectangle2D(selectionRectangle.getX(), selectionRectangle.getY(),selectionRectangle.getWidth(), selectionRectangle.getHeight());
        SSP1.setViewport(r1);
        return SSP1;
    }  
    
    static ImageView setupImageView(Image i1 , boolean trueFalse){
        ImageView iv1 = new ImageView();
        iv1.setImage(i1);
        iv1.setVisible(trueFalse);
        return iv1;
    }
    
    static Rectangle setupDefaultMoveRectangle(){
        Rectangle defaultMoveRectangle = new Rectangle(10, 10);
        defaultMoveRectangle.setFill(Color.TRANSPARENT);
        defaultMoveRectangle.setStroke(Color.RED);
        defaultMoveRectangle.setVisible(false);
        return defaultMoveRectangle;
    }    
    
    static void moveImage(Rectangle moveRectangle, ImageView iv1, double scDX, double scDY){
                    Double ucX = moveRectangle.getX();
                    Double ucY = moveRectangle.getY();
                    
                    iv1.setX(ucX+scDX);
                    iv1.setY(ucY+scDY);
                    moveRectangle.setX(ucX+scDX);
                    moveRectangle.setY(ucY+scDY);
        
    }
}
