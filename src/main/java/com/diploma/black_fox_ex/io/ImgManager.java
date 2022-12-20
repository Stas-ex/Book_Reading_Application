package com.diploma.black_fox_ex.io;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.diploma.black_fox_ex.io.ImgDirectories.BOOK_IMG_DIR;
import static com.diploma.black_fox_ex.io.ImgDirectories.USER_IMG_DIR;

/**
 * This class for saving downloaded files from the site
 */
@Component
public class ImgManager {

    private static final Map<ImgDirectories, String> defaultImgs;

    static {
        defaultImgs = new HashMap<>() {{
                    put(USER_IMG_DIR, "user.jpeg");
                    put(BOOK_IMG_DIR, "book.jpeg");
        }};
    }

    /**
     * This method for saving downloaded files from the site
     *
     * @param directories -> path to the directory of saved images
     * @param imgFile     -> uploaded picture
     * @return file name without directory
     */
    public static String save(ImgDirectories directories, MultipartFile imgFile) {
        if (Objects.requireNonNull(imgFile.getOriginalFilename()).isBlank()) {
            return getDefaultImg(directories);
        }

        String filename = getRandomFilename(imgFile.getOriginalFilename());
        createdImg(imgFile, directories.getPath() + filename);
        return filename;
    }

    public static boolean isDefaultImg(String filename){
        return defaultImgs.containsValue(filename);
    }

    private static String getDefaultImg(ImgDirectories directories){
        return Optional.ofNullable(defaultImgs.get(directories))
                .orElseThrow(() -> new RuntimeException("Image file catalog not found."));
    }

    private static void createdImg(MultipartFile imgFile , String fullPath){
        try {
            imgFile.transferTo(new File(fullPath));
        } catch (IOException e) {
            throw new RuntimeException("Error to create image. ", e);
        }
    }


    private static String getRandomFilename(String filename) {
        return UUID.randomUUID().toString().substring(3, 7) + "." + filename;
    }
}
