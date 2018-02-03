package com.example.max;

import com.example.max.service.ImageComparison4;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        File file1 = new File("C:\\Users\\sammyholla\\Desktop\\ПРИЧЕСКА\\image1.png");
        File file2 = new File("C:\\Users\\sammyholla\\Desktop\\ПРИЧЕСКА\\image22.png");

        BufferedImage image1 = ImageIO.read(file1);
        BufferedImage image2 = ImageIO.read(file2);

        ImageComparison4 imageComparison4 = new ImageComparison4();
        imageComparison4.comparison(image1,image2);



    }
}