package com.diplom.black_fox_ex.io;

import com.diplom.black_fox_ex.exeptions.AnswerErrorCode;
import com.diplom.black_fox_ex.exeptions.ServerException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileManager {

    public String createFile(FileDirectories directories, MultipartFile imgFile) throws ServerException {
        //Проверка и сохранение картинки
        String fileName = imgFile.getOriginalFilename();
        if (Objects.requireNonNull(fileName).isEmpty()) return "user.jpg";

        validateSuffixFileName(fileName);
        File uploadDir = new File(directories.getPath());

        //If the file does not exist
        if (!uploadDir.exists()) uploadDir.mkdirs();//We will create it


        String fileNameRand = UUID.randomUUID().toString().substring(3, 7) + "." + fileName;
        try {
            imgFile.transferTo(new File(directories.getPath() + fileNameRand));
        } catch (IOException e) {
            throw new ServerException(AnswerErrorCode.FILE_CREATE_ERROR);
        }
        return fileNameRand;
    }

    private void validateSuffixFileName(String fileName) throws ServerException {
        String[] fileNameSplit = fileName.split("\\.");
        String suffix = fileNameSplit[fileNameSplit.length-1];
        if(!suffix.equals("jpg") && !suffix.equals("png") && !suffix.equals("jpeg")){
            throw new ServerException(AnswerErrorCode.FILE_CREATE_ERROR);
        }
    }

}
