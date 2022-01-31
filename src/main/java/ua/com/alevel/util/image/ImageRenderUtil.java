package ua.com.alevel.util.image;

import org.springframework.web.multipart.MultipartFile;
import ua.com.alevel.exception.ImageSizeNotAllowed;
import ua.com.alevel.properties.StaticProperties;
import ua.com.alevel.web.dto.request.PathRequestDto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


public final class ImageRenderUtil {

    private static String pathToBigImage;
    private static String newPostId;
    private static double newHeight;
    private static double newWight;


    private ImageRenderUtil() {
        throw new IllegalStateException("Utility class.");
    }


    public static PathRequestDto writeImageToFilesAndGetPath(MultipartFile multipartFile, String newPostId) throws IOException {
        ImageRenderUtil.newPostId = newPostId;

        PathRequestDto pathRequestDto = new PathRequestDto();

        pathRequestDto.setPathToBig(savingLargeImage(multipartFile));
        pathRequestDto.setPathToSmall(savingSmallImage(multipartFile));

        return pathRequestDto;
    }

    private static String savingLargeImage(MultipartFile image) throws IOException {
        String pathToPackage = generatePathToPackageForSavedImage();
        new File(pathToPackage).mkdirs();

        pathToBigImage = pathToPackage + "/" + newPostId + "big" + getExtensionFile(Objects.requireNonNull(image.getOriginalFilename()));
        image.transferTo(new File(pathToBigImage));

        if (checkSizeImage()) {
            new File(pathToBigImage).delete();
            new File(pathToPackage).delete();
            throw new ImageSizeNotAllowed("image size not allowed");
        }

        return pathToBigImage;
    }

    private static String savingSmallImage(MultipartFile file) throws IOException {
        String pathToSmallImage = generatePathToPackageForSavedImage() + "/" + newPostId + "small" + getExtensionFile(Objects.requireNonNull(file.getOriginalFilename()));

        File bigImage = new File(pathToBigImage);
        BufferedImage bufferedImageInput = ImageIO.read(bigImage);

        generateSmallSizeImage(bufferedImageInput.getWidth(), bufferedImageInput.getHeight());

        BufferedImage bufferedImageOutput = new BufferedImage((int) newWight,
                (int) newHeight, bufferedImageInput.getType());

        Graphics2D g2d = bufferedImageOutput.createGraphics();

        g2d.drawImage(bufferedImageInput, 0, 0, (int) newWight, (int) newHeight, null);
        g2d.dispose();


        String formatName = pathToSmallImage.substring(pathToSmallImage
                .lastIndexOf(".") + 1);

        ImageIO.write(bufferedImageOutput, formatName, new File(pathToSmallImage));

        return pathToSmallImage;
    }

    private static String generatePathToPackageForSavedImage() {
        return StaticProperties.PATH_PROJECT + "/src/main/resources/static/files" + "/" + newPostId;
    }

    private static String getExtensionFile(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf('.'));
    }

    private static boolean checkSizeImage() throws IOException {
        BufferedImage bigImg = ImageIO.read(new File(pathToBigImage));
        return bigImg.getHeight() < 300 || bigImg.getWidth() < 300 || bigImg.getWidth() > 3100 || bigImg.getHeight() > 3100;
    }

    //Getting the dimensions of a small image while maintaining proportions
    private static void generateSmallSizeImage(double wight, double height) {
        double prop = wight / height;
        if (wight > height) {
            newWight = 300;
            newHeight = 300 / prop;
        } else if (wight < height) {
            newHeight = 300;
            newWight = 300 / prop;
        } else {
            newHeight = 300;
            newWight = 300;
        }
    }
}
