package com.diploma.black_fox_ex.io;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * This class for saving downloaded files from the site
 */
@Component
public class FileManager {

    private final String USER_IMG = "user.jpeg";
    private final String BOOK_IMG = "book.jpeg";

    /**
     * This method for saving downloaded files from the site
     *
     * @param directories -> path to the directory of saved images
     * @param imgFile     -> uploaded picture
     * @return file name without directory
     */
    public String createFile(FileDirectories directories, MultipartFile imgFile) {
        if (imgFile == null || Objects.requireNonNull(imgFile.getOriginalFilename()).isEmpty()) {
            if (directories.equals(FileDirectories.USER_IMG_DIR))
                return USER_IMG;
            if (directories.equals(FileDirectories.BOOK_IMG_DIR))
                return BOOK_IMG;
            throw new RuntimeException("Image file catalog not found.");
        }

        String filename = getRandomFilename(imgFile.getOriginalFilename());

        try {
            imgFile.transferTo(new File(directories.getPath() + filename));
        } catch (IOException e) {
            throw new RuntimeException("Error to create image. ", e);
        }
        return filename;
    }

    private String getRandomFilename(String filename) {
        return UUID.randomUUID().toString().substring(3, 7) + "." + filename;
    }
}
