package ua.com.alevel.util.image;

import org.springframework.web.multipart.MultipartFile;
import ua.com.alevel.exception.ImageSizeNotAllowed;
import ua.com.alevel.properties.StaticProperties;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class AvatarRenderUtil {

    private static String newAvatarId;
    private static String pathToAvatars;

    private AvatarRenderUtil() {
        throw new IllegalStateException("Utility class.");
    }

    public static String writeAvatarToFilesAndGetPath(MultipartFile multipartFile, String newAvatarId) {
        AvatarRenderUtil.newAvatarId = newAvatarId;

        try {
            return savingAvatar(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String savingAvatar(MultipartFile avatar) throws IOException {
        String pathToPackage = generatePathToPackageForSavedAvatar();
        new File(pathToPackage).mkdirs();

        pathToAvatars = pathToPackage + "/" + newAvatarId + "avatars" + getExtensionFile(Objects.requireNonNull(avatar.getOriginalFilename()));

        avatar.transferTo(new File(pathToAvatars));

        if (checkSizeImage()) {
            new File(pathToAvatars).delete();
            new File(pathToPackage).delete();
            throw new ImageSizeNotAllowed("image size not allowed");
        }

//        BufferedImage avatars = getCompleteAvatar();
//        new File(pathToAvatars).delete();
//
//        ImageIO.write(avatars, getExtensionFile(Objects.requireNonNull(avatar.getOriginalFilename()))
//        , new File(pathToAvatars));

        return pathToAvatars;
    }

    private static String generatePathToPackageForSavedAvatar() {
        return StaticProperties.PATH_PROJECT + "/src/main/resources/static/files/avatars/" + newAvatarId;
    }

    //        return bigAvatars.getSubimage((bigWight - cropWight) / 2, (bigHeight - cropHeight) / 2, cropWight, bigHeight);
    private static BufferedImage generateCenterCoordsImage(double wight, double height) throws IOException {
        double prop = wight / height;
        int cropWight = 300;
        int cropHeight = 300;
        if (wight < height) {
            wight = 300;
            height = (height - 300) / 2;
        }
        return null;
    }

    private static String getExtensionFile(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf('.'));
    }

    private static boolean checkSizeImage() throws IOException {
        BufferedImage bigImg = ImageIO.read(new File(pathToAvatars));
        return bigImg.getHeight() < 300 || bigImg.getWidth() < 300 || bigImg.getWidth() > 3100 || bigImg.getHeight() > 3100;
    }
}
