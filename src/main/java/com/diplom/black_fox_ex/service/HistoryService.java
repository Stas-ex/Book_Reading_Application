package com.diplom.black_fox_ex.service;
/*-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.exeptions.AnswerErrorCode;
import com.diplom.black_fox_ex.exeptions.ServerException;
import com.diplom.black_fox_ex.model.History;
import com.diplom.black_fox_ex.model.Tag;
import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.repositories.HistoryRepo;
import com.diplom.black_fox_ex.repositories.TagRepo;
import com.diplom.black_fox_ex.repositories.UserRepo;
import com.diplom.black_fox_ex.request.HistoryCreateDtoReq;
import com.diplom.black_fox_ex.request.HistoryDeleteDtoReq;
import com.diplom.black_fox_ex.request.HistoryUpdateDtoReq;
import com.diplom.black_fox_ex.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
/**-----------------------------------------------------------------------------------**/
@Service
public class HistoryService {
    /**-----------------------------------------------------------------------------------**/
    @Autowired
    HistoryRepo historyRepo;

    @Autowired
    TagRepo tagRepo;

    @Autowired
    UserRepo userRepo;

    @Value("${upload.path}")
    private String uploadPath;// Вытаскиваем путь к файлу

    /**-----------------------------------------------------------------------------------**/
    public HistoryCreateDtoResponse createHistory(HistoryCreateDtoReq dto) {
        HistoryCreateDtoResponse response = new HistoryCreateDtoResponse();
        try {
            validateCreateHistory(dto);
            String fileName;
            if ((fileName = createFile(dto.getFileBackgroundName())).equals("")) {
                dto.setFileName("historyBack.png");
            } else {
                dto.setFileName(fileName);
            }
            History history = new History(dto.getNameHistory(), dto.getFileName(), dto.getBigText());
            historyRepo.save(history);
            Tag tag = tagRepo.getById(Long.valueOf(dto.getTagId()));
            if(tag == null){throw new ServerException(AnswerErrorCode.HISTORY_TAG_ERROR);}
            history.addTag(tag);

            User user = userRepo.findByUsername(dto.getUsername());
            user.addHistory(history);
            userRepo.save(user);

        } catch (IOException e) {
            response.setError(e.getMessage());
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
        }
        return response;
    }
    /**-----------------------------------------------------------------------------------**/
    public HistoryUpdateDtoResponse updateHistory(HistoryUpdateDtoReq dtoReq) {
        HistoryUpdateDtoResponse response = new HistoryUpdateDtoResponse();
        try {
            validateUpdateHistory(dtoReq);
            String fileName;
            if ((fileName = createFile(dtoReq.getImg())).equals("")) {
                dtoReq.setFileName(historyRepo.findById(dtoReq.getId()).orElseThrow().getBackgroundImg());
            } else {
                dtoReq.setFileName(fileName);
            }
            History history = new History(dtoReq.getId(), dtoReq.getTitle(), dtoReq.getFileName(), dtoReq.getBigText());
            historyRepo.save(history);
            response.setHistoryDto(new HistoryDto(history));

        } catch (IOException e) {
            response.setError(AnswerErrorCode.HISTORY_IMG_ERROR.getMsg());
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
        }
        return response;

    }
    /**-----------------------------------------------------------------------------------**/
    public HistoryDeleteDtoResponse deleteByIdAndUsername(HistoryDeleteDtoReq request) {
        HistoryDeleteDtoResponse response =  new HistoryDeleteDtoResponse();
        try {
            validateDeleteByIdAndUsername(request);

            History history = userRepo.findAllByUsernameAndId(request.getUsername(),request.getId());
            User user = userRepo.findByUsername(request.getUsername());
            validateHistoryAndUser(history,user);
            user.removeHistory(history);

            userRepo.save(user);
            historyRepo.delete(history);

        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
        }
        return response;
    }

    /**-----------------------------------------------------------------------------------**/
    public ResponseAllHistory getAllHistory() {
        ResponseAllHistory response = new ResponseAllHistory();
        try {
            List<History> list = historyRepo.findAll();
            setMapHistoriesByList(list,response);
            response.setTags(tagRepo.findAll());
        }catch (ServerException ex){
            response.setError(ex.getErrorMessage());
            response.setTags(tagRepo.findAll());
        }
        return response;
    }
    /**-----------------------------------------------------------------------------------**/
    public ResponseAllHistory getAllHistoryByTag(String nameTag) {
        ResponseAllHistory response = new ResponseAllHistory();
        try {
            List<History> list = historyRepo.findAllByTagName(nameTag);
            setMapHistoriesByList(list,response);
            response.setTags(tagRepo.findAll());
        }catch (ServerException ex){
            response.setError(ex.getErrorMessage());
            response.setTags(tagRepo.findAll());
        }
        return response;
    }
    /**-----------------------------------------------------------------------------------**/
    private void setMapHistoriesByList(List<History> list, ResponseAllHistory response) throws ServerException {
        ArrayList<HistoryDto> listDto = new ArrayList<>();
        int counter = 0;
        for (History history: list) {
            if (listDto.size() == 21) {
                response.getMap().put(counter, (List<HistoryDto>) listDto.clone());
                listDto.clear();
                counter++;
            }
            listDto.add(new HistoryDto(history, uploadPath));
        }
        if(listDto.size() != 0){
            response.getMap().put(counter,listDto);
        }
        if(response.getMap().size() == 0){
            throw new ServerException(AnswerErrorCode.HISTORY_TAG_ERROR);
        }
    }

    /**-----------------------------------------------------------------------------------**/
    private String createFile(MultipartFile imgFile) throws IOException {
        //Проверка и сохранение картинки
        if (!Objects.equals(imgFile.getOriginalFilename(), "")) {
            File uploadDir = new File(uploadPath);

            //Если файла не существует
            if (!uploadDir.exists()) {
                uploadDir.mkdir();//Мы его создадим
            }

            String randomName = UUID.randomUUID().toString().substring(3, 7);
            randomName += "." + imgFile.getOriginalFilename();
            imgFile.transferTo(new File(uploadPath + randomName));
            return randomName;
        }
        return "";
    }
    /**-----------------------------------------------------------------------------------**/
    private void validateHistoryAndUser(History history, User user) throws ServerException {
        if(history == null || user == null){
            throw new ServerException(AnswerErrorCode.HISTORY_NOT_FOUND);
        }
    }
    /**-----------------------------------------------------------------------------------**/
    private void validateCreateHistory(HistoryCreateDtoReq dto) throws ServerException {
        if (dto.getUsername() == null) {
            throw new ServerException(AnswerErrorCode.HISTORY_USER_NOT_FOUND);
        }
        if (dto.getNameHistory() == null || dto.getNameHistory().length() < 3) {
            throw new ServerException(AnswerErrorCode.HISTORY_TITLE_ERROR);
        }
        if (dto.getBigText() == null || dto.getBigText().length() < 20) {
            throw new ServerException(AnswerErrorCode.HISTORY_SHORT_TEXT);
        }
        if (dto.getFileBackgroundName().isEmpty()) {
            throw new ServerException(AnswerErrorCode.HISTORY_IMG_ERROR);
        }
        if(Objects.equals(dto.getTagId(), "")){
            throw new ServerException(AnswerErrorCode.HISTORY_TAG_ERROR);
        }
    }
    /**-----------------------------------------------------------------------------------**/
    private void validateUpdateHistory(HistoryUpdateDtoReq dto) throws ServerException {
        if (Long.valueOf(dto.getId()) == null) {
            throw new ServerException(AnswerErrorCode.HISTORY_ID_NOT_EXIST);
        }
        if (dto.getUsername() == null) {
            throw new ServerException(AnswerErrorCode.HISTORY_USER_NOT_FOUND);
        }
        if (dto.getTitle() == null || dto.getTitle().length() < 3) {
            throw new ServerException(AnswerErrorCode.HISTORY_TITLE_ERROR);
        }
        if (dto.getBigText() == null || dto.getBigText().length() < 20) {
            throw new ServerException(AnswerErrorCode.HISTORY_SHORT_TEXT);
        }
    }
    /**-----------------------------------------------------------------------------------**/
    private void validateDeleteByIdAndUsername(HistoryDeleteDtoReq request) throws ServerException {
        if(Long.valueOf(request.getId()) == null){
            throw new ServerException(AnswerErrorCode.HISTORY_ID_NOT_EXIST);
        }
        if(request.getUsername() == null){
            throw new ServerException(AnswerErrorCode.HISTORY_USER_NOT_FOUND);
        }
    }
    /**-----------------------------------------------------------------------------------**/
    public List<Tag> getAllTag() {
        return tagRepo.findAll();
    }
}
