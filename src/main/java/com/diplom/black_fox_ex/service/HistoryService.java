package com.diplom.black_fox_ex.service;

import com.diplom.black_fox_ex.request.*;
import com.diplom.black_fox_ex.repositories.CommentsRepo;
import com.diplom.black_fox_ex.exeptions.AnswerErrorCode;
import com.diplom.black_fox_ex.exeptions.ServerException;
import com.diplom.black_fox_ex.repositories.HistoryRepo;
import com.diplom.black_fox_ex.repositories.UserRepo;
import com.diplom.black_fox_ex.repositories.TagRepo;
import com.diplom.black_fox_ex.io.FileDirectories;
import com.diplom.black_fox_ex.io.FileManager;
import com.diplom.black_fox_ex.model.Comment;
import com.diplom.black_fox_ex.model.History;
import com.diplom.black_fox_ex.model.Tag;
import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.response.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HistoryService {
    private final FileManager fileManager = new FileManager();
    private final HistoryRepo historyRepo;
    private final TagRepo tagRepo;
    private final UserRepo userRepo;
    private final CommentsRepo commentsRepo;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    public HistoryService(HistoryRepo historyRepo, TagRepo tagRepo,
                          UserRepo userRepo, CommentsRepo commentsRepo) {
        this.historyRepo = historyRepo;
        this.tagRepo = tagRepo;
        this.userRepo = userRepo;
        this.commentsRepo = commentsRepo;
    }

    //------------------------------------------------------------------------------------------------------//
    public CreateHistoryDtoResp createHistory(User user, CreateHistoryDtoReq historyDto) {
        var response = new CreateHistoryDtoResp();
        try {
            validateCreateHistory(user, historyDto);
            historyDto.setFileName(fileManager.createFile(
                    FileDirectories.HISTORY_IMG,
                    historyDto.getImgFile())
            );
            History history = new History(historyDto.getTitle(),
                    historyDto.getFileName(), historyDto.getBigText());
            Tag tag = tagRepo.findByName(historyDto.getTag());
            history.setTag(tag);
            historyRepo.save(history);

            List<History> histories = userRepo.findAllById(user.getId());
            histories.add(history);
            user.setHistories(histories);
            userRepo.save(user);

        } catch (ServerException ex) {
            response.setError(ex.getErrorMessage());
            logger.warn("User ({}) -> (createHistory) error {}.", user.getId(), ex.getErrorMessage());
        } catch (Exception ex) {
            logger.error("User ({}) -> (createHistory) error {}.", user.getId(), ex.getMessage());
        }
        return response;
    }

    public UpdateHistoryDtoResp updateHistory(User user, UpdateHistoryDtoReq historyDto) {
        var response = new UpdateHistoryDtoResp();
        try {
            validateUpdateHistory(user, historyDto);
            History history = userRepo.findHistoryById(user.getId(), historyDto.getId());

            if (!Objects.requireNonNull(historyDto.getImgFile().getOriginalFilename()).isEmpty())
                historyDto.setFileName(fileManager.createFile(
                        FileDirectories.HISTORY_IMG, historyDto.getImgFile()));
            else
                historyDto.setFileName(history.getBackgroundImg());

            History historyUpdate = new History(historyDto);
            historyUpdate.setTag(tagRepo.findByName(historyDto.getTag()));
            historyRepo.save(historyUpdate);
            response.setHistoryDto(new GetHistoryCardDtoResp(historyUpdate));
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}) -> (updateHistory) error {}.", user.getId(), e.getErrorMessage());
        } catch (Exception ex) {
            logger.error("User ({}) -> (updateHistory) error {}.", user.getId(), ex.getMessage());
        }
        return response;

    }

    public DeleteHistoryDtoResp deleteHistory(DeleteHistoryDtoReq request) {
        var response = new DeleteHistoryDtoResp();
        try {
            validateDeleteHistory(request);
            User user = request.getUser();
            History history = userRepo.findHistoryById(user.getId(), request.getId());

            List<History> histories = userRepo.findAllById(user.getId());
            histories.remove(history);
            user.setHistories(histories);

            userRepo.save(user);
            historyRepo.delete(history);
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}) -> (deleteHistory) error {}.", request.getUser().getId(), e.getErrorMessage());
        } catch (Exception ex) {
            logger.error("User ({}) -> (deleteHistory) error {}.", request.getUser().getId(), ex.getMessage());
        }
        return response;
    }

    public GetAllHistoryResp getAllHistoryByTag(String nameTag, int numPage) {
        var response = new GetAllHistoryResp();
        try {
            validateGetAllHistoryByTag(nameTag, numPage);
            List<History> listHistory;

            if (nameTag.equals("all"))
                listHistory = historyRepo.findAll();
            else
                listHistory = historyRepo.findAllByTagName(nameTag);
            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = 0; i < (listHistory.size() / 21) + 1; i++) {
                pageNumbers.add(i);
            }
            response.setPageNumbers(pageNumbers);
            response.setTags(tagRepo.findAll());

            if (listHistory.size() == 0)
                throw new ServerException(AnswerErrorCode.PAGE_IS_EMPTY);
            else if (listHistory.size() > (numPage + 1) * 21)
                response.setList(listHistory.subList(numPage * 21, (numPage + 1) * 21));
            else {
                response.setList(listHistory.subList(numPage * 21, listHistory.size()));
            }
        } catch (ServerException ex) {
            response.setError(ex.getErrorMessage());
            logger.warn("(getAllHistoryByTag) error -> {}.", ex.getErrorMessage());
        } catch (Exception ex) {
            logger.error("(getAllHistoryByTag) error -> {}.", ex.getMessage());
        }
        return response;
    }

    public GetHistoryLookPageDtoResp getHistoryById(User user, long id) {
        var response = new GetHistoryLookPageDtoResp();
        try {
            validateGetHistoryById(id);
            History history = historyRepo.findById(id).orElseThrow();

            List<GetCommentsDtoResp> commentsDto = new ArrayList<>();
            history.getComments().forEach(comment -> commentsDto.add(new GetCommentsDtoResp(comment)));
            response.setHistoryDto(new GetHistoryLookDtoResp(history, commentsDto));

            //If the like was clicked
            if (user != null) {
                List<History> list = userRepo.findFavoriteHistoryById(user.getId());
                if (list.contains(history)) {
                    response.setLikeActive("");
                }
            }
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}) , History ({}) -> (getHistoryById) error {}.", user.getId(), id, e.getErrorMessage());
        } catch (Exception ex) {
            logger.error("User ({}), History ({}) -> (getHistoryById) error {}.", user.getId(), id, ex.getMessage());
        }
        return response;
    }

    public AddCommentDtoResp addComment(User user, AddCommentDtoReq request) {
        var responseError = new AddCommentDtoResp();
        try {
            validateAddComment(user, request);
            Comment comment = new Comment(request.getBigText(), request.getColor(), user);
            History history = historyRepo.findById(request.getId()).orElseThrow();
            List<Comment> commentsList = userRepo.findAllCommentsByUserId(user.getId());

            commentsList.add(comment);
            history.addComments(comment);
            user.setComments(commentsList);

            commentsRepo.save(comment);
            historyRepo.save(history);
            userRepo.save(user);
        } catch (ServerException e) {
            responseError.setError(e.getErrorMessage());
            logger.warn("User ({}), History ({}) -> (addComment) error {}.", user.getId(), request.getId(), e.getErrorMessage());
        } catch (Exception ex) {
            logger.error("User ({}), History ({})  -> (addComment) error {}.", user.getId(), request.getId(), ex.getMessage());
        }
        return responseError;
    }

    public GetProfileViewHiAllDtoResp getAllHistoryByUser(User user) {
        GetProfileViewHiAllDtoResp response = new GetProfileViewHiAllDtoResp();
        try {
            validateUser(user);
            List<GetHistoryCardDtoResp> listDto = new ArrayList<>();
            List<History> listHistory = userRepo.findAllById(user.getId());
            listHistory.forEach(history -> listDto.add(new GetHistoryCardDtoResp(history)));
            response.setHistoryDto(listDto);
            return response;
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}) -> (getAllHistoryByUser) error {}.", user.getId(), e.getErrorMessage());
        } catch (Exception ex) {
            logger.error("User ({}) -> (getAllHistoryByUser) error {}.", user.getId(), ex.getMessage());
        }
        return response;
    }

    public GetProfileViewHiDtoResp getHistory(User user, long id) {
        var response = new GetProfileViewHiDtoResp();
        try {
            validateGetHistory(user, id);
            List<History> list = userRepo.findAllById(user.getId());
            History history = list.stream().filter(h -> h.getId() == id).toList().get(0);
            if (history == null)
                throw new ServerException(AnswerErrorCode.HISTORY_NOT_FOUND);
            response.setHistoryDto(new GetHistoryEditDtoResp(history));
        } catch (ServerException ex) {
            response.setError(ex.getErrorMessage());
            logger.warn("User ({}), History ({}) -> (getHistory) error {}.", user.getId(), id, ex.getErrorMessage());
        } catch (Exception ex) {
            logger.error("User ({}), History ({}) -> (getHistory) error {}.", user.getId(), id, ex.getMessage());
        }
        return response;
    }

    public AddFavoriteResp addFavoriteHistory(AddFavoriteHiReq request) {
        var response = new AddFavoriteResp();
        try {
            validateAddFavoriteHistory(request);
            User user = request.getUser();
            History history = historyRepo.findById(request.getHistoryId()).orElseThrow();
            List<History> listAllFavorite = userRepo.findFavoriteHistoryById(user.getId());

            //If the user has already liked
            if (history.getUserLike().contains(user)) {
                history.getUserLike().remove(user);
                listAllFavorite.remove(history);
            } else {
                history.getUserLike().add(user);
                listAllFavorite.add(history);
            }

            user.setFavoriteStories(listAllFavorite);
            historyRepo.save(history);
            userRepo.save(user);

        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}), History ({}) -> (addFavoriteHistory) error {}.", request.getUser().getId(), request.getHistoryId(), e.getErrorMessage());
        } catch (Exception ex) {
            logger.error("User ({}), History ({}) -> (addFavoriteHistory) error {}.", request.getUser().getId(), request.getHistoryId(), ex.getMessage());
        }
        return response;
    }

    public DeleteFavoriteHiResp deleteFavoriteHistory(DeleteFavoriteHiDtoReq request) {
        var response = new DeleteFavoriteHiResp();
        try {
            validateDeleteFavoriteHistory(request);

            User user = request.getUser();
            History history = historyRepo.findById(request.getHistoryId()).orElseThrow();
            List<History> listAllFavorite = userRepo.findFavoriteHistoryById(user.getId());

            listAllFavorite.remove(history);
            history.getUserLike().remove(user);
            user.setFavoriteStories(listAllFavorite);

            historyRepo.save(history);
            userRepo.save(user);

        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}), History ({}) -> (deleteFavoriteHistory) error {}.", request.getUser().getId(), request.getHistoryId(), e.getErrorMessage());
        } catch (Exception ex) {
            logger.error("User ({}), History ({}) -> (deleteFavoriteHistory) error {}.", request.getUser().getId(), request.getHistoryId(), ex.getMessage());
        }
        return response;
    }

    public List<Tag> getAllTag() {
        return tagRepo.findAll();
    }

    //------------------------------------------------------------------------------------------------------//
    private void validateGetHistoryById(long id) throws ServerException {
        if (id == 0) {
            throw new ServerException(AnswerErrorCode.HISTORY_ID_NOT_EXIST);
        }
    }

    private void validateAddComment(User user, AddCommentDtoReq request) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (request.getId() == 0) {
            throw new ServerException(AnswerErrorCode.HISTORY_ID_NOT_EXIST);
        }
        if (request.getColor() == null || request.getColor().isEmpty()) {
            throw new ServerException(AnswerErrorCode.COMMENT_COLOR_ERROR);
        }
        if (request.getBigText() == null || request.getBigText().length() < 10) {
            throw new ServerException(AnswerErrorCode.COMMENT_BIG_TEXT_ERROR);
        }
    }

    private void validateGetAllHistoryByTag(String nameTag, int numPage) throws ServerException {
        if (nameTag == null || nameTag.isEmpty()) {
            throw new ServerException(AnswerErrorCode.HISTORY_TAG_ERROR);
        }
        if (numPage < -1) {
            throw new ServerException(AnswerErrorCode.HISTORY_PAGE_ERROR);
        }
    }

    private void validateCreateHistory(User user, CreateHistoryDtoReq dto) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (user.getUsername().isEmpty()) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (dto.getTitle() == null || dto.getTitle().length() < 3) {
            throw new ServerException(AnswerErrorCode.HISTORY_TITLE_ERROR);
        }
        if (dto.getBigText() == null || dto.getBigText().length() < 20) {
            throw new ServerException(AnswerErrorCode.HISTORY_SHORT_TEXT);
        }
        if (dto.getImgFile() == null) {
            throw new ServerException(AnswerErrorCode.HISTORY_IMG_ERROR);
        }
        if (dto.getTag() == null || dto.getTag().isEmpty()) {
            throw new ServerException(AnswerErrorCode.HISTORY_TAG_ERROR);
        }
    }

    private void validateUpdateHistory(User user, UpdateHistoryDtoReq dto) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (dto.getTag() == null || dto.getTag().isEmpty()) {
            throw new ServerException(AnswerErrorCode.HISTORY_TAG_ERROR);
        }
        if (dto.getTitle() == null || dto.getTitle().length() < 3) {
            throw new ServerException(AnswerErrorCode.HISTORY_TITLE_ERROR);
        }
        if (dto.getBigText() == null || dto.getBigText().length() < 20) {
            throw new ServerException(AnswerErrorCode.HISTORY_SHORT_TEXT);
        }
    }

    private void validateDeleteHistory(DeleteHistoryDtoReq request) throws ServerException {
        if (request.getId() == 0) {
            throw new ServerException(AnswerErrorCode.HISTORY_ID_NOT_EXIST);
        }
        if (request.getUser() == null) {
            throw new ServerException(AnswerErrorCode.HISTORY_USER_NOT_FOUND);
        }
    }

    private void validateUser(User user) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
    }

    private void validateGetHistory(User user, long id) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (id == 0) {
            throw new ServerException(AnswerErrorCode.HISTORY_USER_NOT_FOUND);
        }
    }

    private void validateAddFavoriteHistory(AddFavoriteHiReq request) throws ServerException {
        if (request.getHistoryId() == 0) {
            throw new ServerException(AnswerErrorCode.FAVORITE_HISTORY_ID_ERROR);
        }
        if (request.getUser() == null) {
            throw new ServerException(AnswerErrorCode.FAVORITE_USER_ERROR);
        }
    }

    private void validateDeleteFavoriteHistory(DeleteFavoriteHiDtoReq request) throws ServerException {
        if (request.getHistoryId() == 0) {
            throw new ServerException(AnswerErrorCode.FAVORITE_HISTORY_ID_ERROR);
        }
        if (request.getUser() == null) {
            throw new ServerException(AnswerErrorCode.FAVORITE_USER_ERROR);
        }
    }
}
