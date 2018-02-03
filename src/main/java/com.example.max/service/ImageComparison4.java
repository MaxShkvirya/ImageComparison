package com.example.max.service;

import javax.swing.*;
import java.awt.image.BufferedImage;
// Самому написать можно, взяв за основу некое решение и переработав его под свой стиль, изменив и дополнив
//        1) если ты нашел , значит кто угодно тоже может найти и сверить

//        2)если ты разобрался как работает то перепиши под себя ,переработай,дополни

//        3)если ты не будешь понимать то что делаешь,это сразу заметят
//          Макс читай , пиши примеры и пытайся их улучшить переработать
//        Посмотри как работает буфер что внутри и напиши свой со своей логикой
public class ImageComparison4 {
    private int width;
    private int height;
    private int arrayPixelsImage1[][];
    private int arrayPixelsImage2[][];
    private int arrayContrastPixelsImages[][];//сравнение пикселей
    private int contrast[][]; //площадь совпадения
    private BufferedImage comparisonImages;
    private int idAreasContrast;//массив измененной картинки
    private boolean backgroundImage;
    private boolean imagesIdentical;

    // сравнение
    public void comparison(BufferedImage firstImage, BufferedImage secondImage) {
        if (firstImage.getHeight() == secondImage.getHeight() & firstImage.getWidth() == secondImage.getWidth()) {
            this.width = firstImage.getWidth();   // 985
            this.height = secondImage.getHeight(); // 701
            this.arrayPixelsImage1 = arrayPixelsImage(firstImage);
            this.arrayPixelsImage2 = arrayPixelsImage(secondImage);
            backgroundImage = findSecondaryRGB(firstImage, secondImage);// находит второстепенное изображение
            this.contrast = new int[height][width];
            this.idAreasContrast = 1;

            if (backgroundImage) {
                arrayContrastPixelsImages = arrayPixelsImage2;
            } else arrayContrastPixelsImages = arrayPixelsImage1;
            //присваевается массив пикселей на котором буду рисовать отличие
            if (!imagesIdentical) {
                identifyDifferences(firstImage, secondImage);
                definesAreasDifference();// выявлять районы
                drawsBorder();//рисовать границу
                showImage();// показ результата
            }
        } else {  // размеры изображений не равны
            System.out.println("The dimensions of the images are not equal.");
            JOptionPane.showMessageDialog(null, "The dimensions of the images are not equal.");
        }
    }

    //  считывает пиксели в массив[][]
    private int[][] arrayPixelsImage(BufferedImage image) {
        int arrayPixels[][] = new int[image.getHeight()][image.getWidth()];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                arrayPixels[i][j] = image.getRGB(j, i);
            }
        }
        return arrayPixels;
    }


    // +  найти в разный пикселях меньщее число по RGB(белый лист) и чёрный цвет после означаюший изменение пикселя(чёрным по белому)
    private boolean findSecondaryRGB(BufferedImage arrayPixelsImage1, BufferedImage arrayPixelsImage2) {
        int countFirstPixelOrigin = 0;
        int countSecondPixelOrigin = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (arrayPixelsImage1.getRGB(j, i) > arrayPixelsImage2.getRGB(j, i)
                        && 0.9 > Math.abs((double) arrayPixelsImage1.getRGB(j, i) / (double) arrayPixelsImage2.getRGB(j, i))
                        | Math.abs((double) arrayPixelsImage1.getRGB(j, i) / (double) arrayPixelsImage2.getRGB(j, i)) > 1.1) {
                    countFirstPixelOrigin++;
                }
                if (arrayPixelsImage1.getRGB(j, i) < arrayPixelsImage2.getRGB(j, i)
                        && 0.9 > Math.abs((double) arrayPixelsImage1.getRGB(j, i) / (double) arrayPixelsImage2.getRGB(j, i))
                        | Math.abs((double) arrayPixelsImage1.getRGB(j, i) / (double) arrayPixelsImage2.getRGB(j, i)) > 1.1) {
                    countSecondPixelOrigin++;
                }
            }
        }

        if (countFirstPixelOrigin == countSecondPixelOrigin) {
            System.out.println("Images are identical.");
            JOptionPane.showMessageDialog(null, "Images are identical.");
            imagesIdentical = true;
        } else {
            imagesIdentical = false;
        }
        return countFirstPixelOrigin > countSecondPixelOrigin;
    }

    // +- выявить разницу и разделить на -1/0 новый массив
    private void identifyDifferences(BufferedImage arrayPixelsImage1, BufferedImage arrayPixelsImage2) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (arrayPixelsImage1.getRGB(j, i) != arrayPixelsImage2.getRGB(j, i)
                        && 0.9 > Math.abs((double) arrayPixelsImage1.getRGB(j, i) / (double) arrayPixelsImage2.getRGB(j, i))
                        | Math.abs((double) arrayPixelsImage1.getRGB(j, i) / (double) arrayPixelsImage2.getRGB(j, i)) > 1.1) {
                    contrast[i][j] = 0;// отличаются
                } else contrast[i][j] = -1;
            }
        }
    }

    // +- определяет районы
    private void definesAreasDifference() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (contrast[i][j] == 0) {
                    numberSquareDifference(i, j, idAreasContrast); // растекается присваивая номер области
                    pointsRegion(idAreasContrast); //  найти точки области. начинает объединять области.
                    boundaryRegion(idAreasContrast);// рисовать границу
                    //еще раз для объединения областей
                    numberSquareDifference(i, j, idAreasContrast);
                    pointsRegion(idAreasContrast);
                    boundaryRegion(idAreasContrast);
                    idAreasContrast++;
                }
            }
        }
    }

    // + растекается присваивая номер области
    private int numberSquareDifference(int y, int x, int idAreasContrast) {

        if (contrast[y][x] == -1) {
            return -1;
        }
        if (contrast[y][x] != idAreasContrast) {
            contrast[y][x] = idAreasContrast;
        }

        if (contrast[y][x + 1] != idAreasContrast) {
            numberSquareDifference(y, x + 1, idAreasContrast);
        }
        if (contrast[y][x - 1] != idAreasContrast) {
            numberSquareDifference(y, x - 1, idAreasContrast);
        }
        if (contrast[y + 1][x] != idAreasContrast) {
            numberSquareDifference(y + 1, x, idAreasContrast);
        }
        return numberSquareDifference(y - 1, x, idAreasContrast);
    }

    // + найти точки области. начинает объединять области.
    private void pointsRegion(int arrayModified) {
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        boolean startPoint = true;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (contrast[i][j] == arrayModified && startPoint) {
                    x1 = j;
                    y1 = i;
                    startPoint = false;
                }
                if (contrast[i][j] == arrayModified) {
                    x2 = j;
                    y2 = i;
                }
            }
        }
        //нижняя левая точка(угол)
        int lowerLeftPoint = increasesArea(x1, y1, x2, y2, arrayModified);
        //верхняя правая точка (угол)
        int upperRightPoint = increasesArea(x2, y2, x1, y1, arrayModified);
        //два пункта для метода рисования // расширять Площадь
        increasesArea(upperRightPoint, y2, lowerLeftPoint, y1, arrayModified);

    }

    //  расширять Площадь прямоугольника
    private int increasesArea(int x1, int y1, int x2, int y2, int idAreasContrast) {
        if (contrast[y1][x1] != idAreasContrast) {
            contrast[y1][x1] = idAreasContrast;
        }
        if (contrast[y2][x2] != idAreasContrast) {
            contrast[y2][x2] = idAreasContrast;
        }
        if (Math.abs(x1 - x2) <= 0 || Math.abs(y1 - y2) <= 0) {
            return x1;
        }

        if (y1 < y2) {
            if (contrast[y1][x1 - 1] == idAreasContrast) {
                return increasesArea(x1 - 1, y1, x2, y2, idAreasContrast);
            }
            return increasesArea(x1, y1 + 1, x2, y2, idAreasContrast);
        }

        if (contrast[y1][x1 + 1] == idAreasContrast) {
            if (contrast[y1][x1 + 1] == -1 || contrast[y1 + 1][x1 + 1] == idAreasContrast) {
                return increasesArea(x1, y1 + 1, x2, y2, idAreasContrast);
            } else {
                return increasesArea(x1 + 1, y1, x2, y2, idAreasContrast);
            }
        }
        return increasesArea(x1, y1 - 1, x2, y2, idAreasContrast);
    }


    // + рисовать границу
    private void boundaryRegion(int arrayModified) {
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        boolean startPoint = true;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (contrast[i][j] == arrayModified && startPoint) {
                    x1 = j;
                    y1 = i;
                    startPoint = false;
                }
                if (contrast[i][j] == arrayModified) {
                    x2 = j;
                    y2 = i;
                }
            }
        }
        cleaningArea(x1, y1, x2, y2, arrayModified);
    }

    // + чистит область
    private void cleaningArea(int x1, int y1, int x2, int y2, int idAreasContrast) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (((y1 - 1 <= i) && (y2 + 1 >= i)) && ((x1 - 1 == j) || (x2 + 1 == j))) {
                    contrast[i][j] = idAreasContrast;
                }
                if (((x1 - 1 <= j) && (x2 + 1 >= j)) && ((y1 - 1 == i) || (y2 + 1 == i))) {
                    contrast[i][j] = idAreasContrast;
                }
                if ((y1 <= i) && (y2 >= i) && (x1 <= j) && (x2 >= j)) {
                    contrast[i][j] = -1;
                }
            }
        }
    }

    //  рисует результат красным
    private void drawsBorder() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (contrast[i][j] != -1) {
                    arrayContrastPixelsImages[i][j] = 0x00ff0000;
                }
            }
        }
    }

    private BufferedImage imageFromPixels(int pixels[][]) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image.setRGB(j, i, pixels[i][j]);
            }
        }
        return image;
    }

    private void showImage() {
        comparisonImages = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        comparisonImages = imageFromPixels(arrayContrastPixelsImages);
        comparisonImages.createGraphics();

        JLabel jLabel = new JLabel();
        jLabel.setIcon(new ImageIcon(comparisonImages));
        JOptionPane.showMessageDialog(null, jLabel);

    }


}