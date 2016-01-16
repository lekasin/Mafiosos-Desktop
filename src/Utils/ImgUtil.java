package Utils;

import gui.StretchIcon;
import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by lars-erikkasin on 20.08.15.
 */
public class ImgUtil {
    public static final String IMG_PATH = "/img/";

    public static Image getImage(String imgName) {
        String imgPath = IMG_PATH + imgName;
        URL url = Main.class.getResource(imgPath);
        return Toolkit.getDefaultToolkit().createImage(url);
    }

    public static ImageIcon getAppIcon(){
        return getScaledIcon("mafiosos.png", 90, 90);
    }

    public static ImageIcon getIcon(String imgName) {
        String imgPath = IMG_PATH + imgName;
        URL url = Main.class.getResource(imgPath);
        return new ImageIcon(url);
    }

    public static ImageIcon scaleIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public static ImageIcon getScaledIcon(String img, int w, int h) {
        return scaleIcon(getIcon(img), w, h);
    }

    public static StretchIcon getStretchIcon(String imgName) {
        String imgPath = IMG_PATH + imgName;
        URL url = Main.class.getResource(imgPath);
        return new StretchIcon(url);
    }
}
