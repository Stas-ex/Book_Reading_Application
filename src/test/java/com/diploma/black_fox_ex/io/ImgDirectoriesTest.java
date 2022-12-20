package com.diploma.black_fox_ex.io;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.diploma.black_fox_ex.io.ImgDirectories.BOOK_IMG_DIR;
import static com.diploma.black_fox_ex.io.ImgDirectories.USER_IMG_DIR;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
class ImgDirectoriesTest {

    @Test
    public void getDefaultImg() {
        var pathBookImg = Path.of(BOOK_IMG_DIR.getPath() + "book.jpeg");
        var pathUserImg = Path.of(USER_IMG_DIR.getPath() + "user.jpeg");
        assertTrue(Files.exists(pathBookImg));
        assertTrue(Files.exists(pathUserImg));
    }
}