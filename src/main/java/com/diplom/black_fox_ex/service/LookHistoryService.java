package com.diplom.black_fox_ex.service;

import com.diplom.black_fox_ex.exeptions.AnswerErrorCode;
import com.diplom.black_fox_ex.exeptions.ServerException;
import com.diplom.black_fox_ex.model.Comments;
import com.diplom.black_fox_ex.model.History;
import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.repositories.CommentsRepo;
import com.diplom.black_fox_ex.repositories.HistoryRepo;
import com.diplom.black_fox_ex.repositories.UserRepo;
import com.diplom.black_fox_ex.request.CommentAddDtoRequest;
import com.diplom.black_fox_ex.request.HistoryLookDtoRequest;
import com.diplom.black_fox_ex.response.CommentAddDtoResponse;
import com.diplom.black_fox_ex.response.HistoryDto;
import com.diplom.black_fox_ex.response.HistoryLookDtoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LookHistoryService {
    /**-----------------------------------------------------------------------------------**/
    @Autowired
    HistoryRepo historyRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    CommentsRepo commentsRepo;

    @Value("${upload.path}")
    private String uploadPath;// Вытаскиваем путь к файлу
    /**-----------------------------------------------------------------------------------**/
    public HistoryLookDtoResponse getHistoryById(HistoryLookDtoRequest request) {
        HistoryLookDtoResponse response = new HistoryLookDtoResponse();
        try {
            validateGetHistoryById(request);
            History history = historyRepo.findById(request.getId()).orElseThrow();
            //If the like was clicked
            if(request.getUserDto() != null){
                List<History> list = userRepo.findFavoriteHistoryById(request.getUserDto().getId());
                if(list.contains(history)){
                    response.setLikeActive("");
                }
            }
            response.setHistoryDto(new HistoryDto(history,uploadPath));
        }
        catch (ServerException e) {
            response.setError(e.getErrorMessage());
        }catch (Exception ex){
            response.setError(ex.getMessage());
        }
        return response;
    }
    /**-----------------------------------------------------------------------------------**/
    private void validateGetHistoryById(HistoryLookDtoRequest request) throws ServerException {
        if(Long.valueOf(request.getId()) == null){
            throw new ServerException(AnswerErrorCode.HISTORY_ID_NOT_EXIST);
        }
    }
    /**-----------------------------------------------------------------------------------**/
    public CommentAddDtoResponse addComment(CommentAddDtoRequest request) {
        CommentAddDtoResponse response = new CommentAddDtoResponse();
        try {
            validateAddComment(request);

            Comments comments = new Comments(request.getBigText(),request.getColor());
            commentsRepo.save(comments);

            History history = historyRepo.findById(request.getIdHistory()).orElseThrow();
            history.addComments(comments);
            historyRepo.save(history);

            User user = userRepo.findByUsername(request.getUsername());
            user.addComments(comments);
            userRepo.save(user);

        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
        }
        return response;
    }
    /**-----------------------------------------------------------------------------------**/
    private void validateAddComment(CommentAddDtoRequest request) throws ServerException {
        if(Long.valueOf(request.getIdHistory()) == null){
            throw new ServerException(AnswerErrorCode.HISTORY_ID_NOT_EXIST);
        }
        if(request.getBigText() == null || request.getBigText().length() < 10){
            throw new ServerException(AnswerErrorCode.COMMENT_BIGTEXT_ERROR);
        }
        if(request.getUsername() == null){
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if(request.getColor() == null){
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
    }
}
