import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;

class Image {

    public static BufferedImage cropImage(BufferedImage inputImage, int newWidth, int newHeight) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        if (height < newHeight || width < newWidth) {
            throw new Error("Invalid crop dimensions!");
        }

        BufferedImage outputImage = new BufferedImage(newWidth, newHeight,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(j, i));
            }
        }
        return outputImage;
    }

    public static BufferedImage pixelatedBlur(BufferedImage inputImage, int strength) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height / strength; i++) {
            for (int j = 0; j < width / strength; j++) {
                long red = 0;
                long blue = 0;
                long green = 0;
                for (int k = 0; k < strength; k++) {
                    for (int l = 0; l < strength; l++) {
                        Color color = new Color(inputImage.getRGB(strength * j + l, strength * i + k));
                        red += color.getRed();
                        blue += color.getBlue();
                        green += color.getGreen();
                    }
                }
                int averageRed = (int) (red / (strength * strength));
                int averageBlue = (int) (blue / (strength * strength));
                int averageGreen = (int) (green / (strength * strength));
                Color averageColor = new Color(averageRed, averageGreen, averageBlue);
                for (int k = 0; k < strength; k++) {
                    for (int l = 0; l < strength; l++) {
                        outputImage.setRGB(strength * j + l, strength * i + k, averageColor.getRGB());
                    }
                }
            }
        }
        return outputImage;
    }

    public static BufferedImage rotateImageClockwise(BufferedImage inputImage) {
        int height = inputImage.getWidth();
        int width = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(i, width - 1 - j));
            }
        }
        return outputImage;
    }

    public static BufferedImage rotateImageAntiClockwise(BufferedImage inputImage) {
        BufferedImage outputImage = rotateImageClockwise(inputImage);
        outputImage = rotateImageClockwise(outputImage);
        outputImage = rotateImageClockwise(outputImage);
        return outputImage;
    }

    public static BufferedImage convertToGrayScale(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(j, i));
            }
        }
        return outputImage;
    }

    public static BufferedImage horizontalInvert(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(width - j - 1, i));
            }
        }
        return outputImage;
    }

    public static BufferedImage verticalInvert(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                outputImage.setRGB(j, i, inputImage.getRGB(j, height - i - 1));
            }
        }
        return outputImage;
    }

    public static int updateValue(int n) {
        int increased = n + n / 5;
        if (increased <= 255)
            return increased;
        return 255;
    }

    public static BufferedImage increaseBrightness(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color pixel = new Color(inputImage.getRGB(j, i));
                int red = updateValue(pixel.getRed());
                int blue = updateValue(pixel.getBlue());
                int green = updateValue(pixel.getGreen());
                Color newPixel = new Color(red, green, blue);
                outputImage.setRGB(j, i, newPixel.getRGB());
            }
        }
        return outputImage;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter filename to edit (must be in the same folder): ");
        String inputFilePath = scanner.nextLine();
        File inputFile = new File(inputFilePath);
        try {
            BufferedImage inputImage = ImageIO.read(inputFile);
            if (inputImage == null) {
                throw new IOException("Could not read the image file.");
            }

            while (true) {
                System.out.println();
                System.out.println("===== SELECT A FILTER =====");
                System.out.println("1: grayscaled");
                System.out.println("2: horizontal flip");
                System.out.println("3: vertical flip");
                System.out.println("4: rotate clockwise");
                System.out.println("5: rotate anticlockwise");
                System.out.println("6: increase brightness");
                System.out.println("7: pixelated blur");
                System.out.println("8: crop");
                System.out.println("q: QUIT");
                System.out.print("\nEnter an option: ");
                String choice = scanner.next();

                BufferedImage filtered = null;
                boolean didQuit = false;
                switch (choice) {
                    case "1":
                        filtered = convertToGrayScale(inputImage);
                        break;
                    case "2":
                        filtered = horizontalInvert(inputImage);
                        break;
                    case "3":
                        filtered = verticalInvert(inputImage);
                        break;
                    case "4":
                        filtered = rotateImageClockwise(inputImage);
                        break;
                    case "5":
                        filtered = rotateImageAntiClockwise(inputImage);
                        break;
                    case "6":
                        filtered = increaseBrightness(inputImage);
                        break;
                    case "7":
                        System.out.print("Enter blur strength (<= width && height): ");
                        int strength = scanner.nextInt();
                        scanner.nextLine();  // consume the remaining newline
                        filtered = pixelatedBlur(inputImage, strength);
                        break;
                    case "8":
                        System.out.print("new width: ");
                        int newWidth = scanner.nextInt();
                        System.out.print("new height: ");
                        int newHeight = scanner.nextInt();
                        scanner.nextLine();  
                        try {
                            filtered = cropImage(inputImage, newWidth, newHeight);
                        } catch (Exception e) {
                            System.out.println("Invalid dimensions!");
                        }
                        break;
                    case "q":
                        System.out.println("BYE");
                        didQuit = true;
                        break;
                    default:
                        System.out.println("Select a valid option");
                        continue;
                }

                if (didQuit) {
                    break;
                }

                System.out.print("Enter the name for output file: ");
                scanner.nextLine(); 
                String outFileName = scanner.nextLine();
                File outputFile = new File(outFileName + ".jpg");

                if (filtered != null) {
                    boolean didWrite = ImageIO.write(filtered, "jpg", outputFile);
                    System.out.println(didWrite ? "\nEdit Successful!" : "\nEdit Failed!");
                }
            }
        } catch (IOException e) {
            System.out.println("Invalid input file: " + e.getMessage());
        }
    }

}