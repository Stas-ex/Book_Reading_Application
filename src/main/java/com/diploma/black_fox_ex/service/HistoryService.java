package com.diploma.black_fox_ex.service;

import com.diploma.black_fox_ex.repositories.CommentsRepo;
import com.diploma.black_fox_ex.exeptions.AnswerErrorCode;
import com.diploma.black_fox_ex.exeptions.ServerException;
import com.diploma.black_fox_ex.repositories.HistoryRepo;
import com.diploma.black_fox_ex.repositories.UserRepo;
import com.diploma.black_fox_ex.repositories.TagRepo;
import com.diploma.black_fox_ex.io.FileDirectories;
import com.diploma.black_fox_ex.io.FileManager;
import com.diploma.black_fox_ex.model.Comment;
import com.diploma.black_fox_ex.model.History;
import com.diploma.black_fox_ex.model.Tag;
import com.diploma.black_fox_ex.model.User;

import com.diploma.black_fox_ex.request.*;
import com.diploma.black_fox_ex.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This class performs basic actions on received history interaction requests.
 */
@Service
public class HistoryService {
    private final FileManager fileManager = new FileManager();
    private final HistoryRepo historyRepo;
    private final TagRepo tagRepo;
    private final UserRepo userRepo;
    private final CommentsRepo commentsRepo;
    private static final Logger logger = LoggerFactory.getLogger(HistoryService.class);


    @Autowired
    public HistoryService(HistoryRepo historyRepo, TagRepo tagRepo,
                          UserRepo userRepo, CommentsRepo commentsRepo) {
        this.historyRepo = historyRepo;
        this.tagRepo = tagRepo;
        this.userRepo = userRepo;
        this.commentsRepo = commentsRepo;
    }

    /**
     * History creation method
     *
     * @param user       includes all fields of an authorized user
     * @param historyDto contains the main parameters for create history
     * @return dto response , with an error field
     * @see #validateCreateHistory(User, CreateHistoryDtoReq)
     */
    public CreateHistoryDtoResp createHistory(User user, CreateHistoryDtoReq historyDto) {
        var response = new CreateHistoryDtoResp();
        try {
            validateCreateHistory(user, historyDto);

            Tag tag = tagRepo.findByName(historyDto.getTag());

            if (tag == null)
                throw new ServerException(AnswerErrorCode.HISTORY_TAG_ERROR);

            String fileName = fileManager.createFile(FileDirectories.HISTORY_IMG, historyDto.getImgFile());

            History history = new History(historyDto.getTitle(),
                    fileName, historyDto.getBigText(), tag);

            historyRepo.save(history);

            List<History> histories = userRepo.findAllById(user.getId());
            histories.add(history);
            user.setHistories(histories);
            userRepo.save(user);

        } catch (ServerException ex) {
            response.setError(ex.getErrorMessage());
            logger.warn("User ({}) -> (createHistory) error {}.", user != null ? user.getId() : "null", ex.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}) -> (createHistory) error {}.", user != null ? user.getId() : "null", ex.getMessage());
        }
        return response;
    }

    /**
     * History update method
     *
     * @param user       includes all fields of an authorized user
     * @param historyDto contains the main parameters for update history
     * @return dto response , with an error field
     * @see #validateUpdateHistory(User, UpdateHistoryDtoReq)
     */
    public UpdateHistoryDtoResp updateHistory(User user, UpdateHistoryDtoReq historyDto) {
        var response = new UpdateHistoryDtoResp();
        try {
            validateUpdateHistory(user, historyDto);
            Tag tag = tagRepo.findByName(historyDto.getTag());
            if (tag == null)
                throw new ServerException(AnswerErrorCode.HISTORY_TAG_ERROR);

            History history = userRepo.findHistoryById(user.getId(), historyDto.getId());
            if (history == null)
                throw new ServerException(AnswerErrorCode.HISTORY_ID_NOT_EXIST);

            String fileName;
            if (historyDto.getImgFile() == null || Objects.requireNonNull(historyDto.getImgFile().getOriginalFilename()).isEmpty()) {
                fileName = history.getBackgroundImg();
            } else {
                fileName = fileManager.createFile(FileDirectories.HISTORY_IMG, historyDto.getImgFile());
            }

            History historyUpdate = new History(history.getId(), historyDto.getTitle(), fileName, historyDto.getBigText(), tag);
            historyRepo.save(historyUpdate);
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}) -> (updateHistory) error {}.", user != null ? user.getId() : "null", e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}) -> (updateHistory) error {}.", user != null ? user.getId() : "null", ex.getMessage());
        }
        return response;

    }

    /**
     * History delete method
     *
     * @param request contains the main parameters for delete history
     * @return dto response , with an error field
     * @see #validateDeleteHistory(DeleteHistoryDtoReq)
     */
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
            logger.warn("User ({}) -> (deleteHistory) error {}.", request.getUser() != null ? request.getUser().getId() : "null", e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}) -> (deleteHistory) error {}.", request.getUser() != null ? request.getUser().getId() : "null", ex.getMessage());
        }
        return response;
    }

    /**
     * Method returns all stories by tag
     *
     * @param nameTag includes tag name
     * @param numPage includes number page
     * @return dto response , with an error field
     * @see #validateGetAllHistoryByTag(String, int)
     */
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
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("(getAllHistoryByTag) error -> {}.", ex.getMessage());
        }
        return response;
    }

    /**
     * Method gets history by id
     *
     * @param user includes all fields of an authorized user
     * @param id   -> history parameter
     * @return dto response , with an error field
     * @see #validateGetHistoryById(long)
     */
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
            logger.warn("User ({}) , History ({}) -> (getHistoryById) error {}.", user != null ? user.getId() : "null", id, e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}), History ({}) -> (getHistoryById) error {}.", user != null ? user.getId() : "null", id, ex.getMessage());
        }
        return response;
    }

    /**
     * Method returns a list of saved histories from an authorized user
     *
     * @param user includes all fields of an authorized user
     * @return response containing a list of the user's favorite histories
     * @see #validateGetAllFavoriteByUser(User)
     */
    public GetAllFavoriteHiResp getAllFavoriteByUser(User user) {
        GetAllFavoriteHiResp response = new GetAllFavoriteHiResp();
        try {
            validateGetAllFavoriteByUser(user);
            List<GetHistoryCardDtoResp> listHistoryDto = new ArrayList<>();
            List<History> listHistory = userRepo.findFavoriteHistoryById(user.getId());
            listHistory.forEach(history -> listHistoryDto.add(new GetHistoryCardDtoResp(history)));
            response.setListDto(listHistoryDto);
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}) -> (getAllFavoriteByUser) error {}.", user != null ? user.getId() : "null", e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}) -> (getAllFavoriteByUser) error {}.", user != null ? user.getId() : "null", ex.getMessage());
        }
        return response;
    }

    /**
     * Method create comment for a story
     *
     * @param user    includes all fields of an authorized user
     * @param request contains the main parameters for create comment
     * @return dto response , with an error field
     * @see #validateAddComment(User, AddCommentDtoReq)
     */
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
            logger.warn("User ({}), History ({}) -> (addComment) error {}.", user != null ? user.getId() : "null", request.getId(), e.getErrorMessage());
        } catch (Exception ex) {
            responseError.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}), History ({})  -> (addComment) error {}.", user != null ? user.getId() : "null", request.getId(), ex.getMessage());
        }
        return responseError;
    }

    /**
     * Method returns a list of all histories from an authorized user
     *
     * @param user includes all fields of an authorized user
     * @return list of all stories for a given users
     * @see #validateUser(User)
     */
    public GetProfileViewHiAllDtoResp getAllHistoryByUser(User user) {
        GetProfileViewHiAllDtoResp response = new GetProfileViewHiAllDtoResp();
        try {
            validateUser(user);
            List<GetHistoryCardDtoResp> listDto = new ArrayList<>();
            List<History> listHistory = userRepo.findAllById(user.getId());
            listHistory.forEach(history -> listDto.add(new GetHistoryCardDtoResp(history)));
            response.setHistoryListDto(listDto);
            return response;
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}) -> (getAllHistoryByUser) error {}.", user != null ? user.getId() : "null", e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}) -> (getAllHistoryByUser) error {}.", user != null ? user.getId() : "null", ex.getMessage());
        }
        return response;
    }

    /**
     * Method returns product by id
     *
     * @param user includes all fields of an authorized user
     * @param id   -> history parameter
     * @return historyDto, with an error field
     * @see #validateGetHistory(User, long)
     */
    public GetProfileViewHiDtoResp getHistoryEditById(User user, long id) {
        var response = new GetProfileViewHiDtoResp();
        try {
            validateGetHistory(user, id);
            List<History> list = userRepo.findAllById(user.getId());
            List<History> historyList = list.stream().filter(h -> h.getId() == id).toList();
            if (historyList.size() == 0)
                throw new ServerException(AnswerErrorCode.HISTORY_NOT_FOUND);
            response.setHistoryDto(new GetHistoryEditDtoResp(historyList.get(0)));
        } catch (ServerException ex) {
            response.setError(ex.getErrorMessage());
            logger.warn("User ({}), History ({}) -> (getHistory) error {}.", user != null ? user.getId() : "null", id, ex.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}), History ({}) -> (getHistory) error {}.", user != null ? user.getId() : "null", id, ex.getMessage());
        }
        return response;
    }

    /**
     * The method implements adding history to the saved tab for this user
     *
     * @param request contains the main parameters for add favorite history
     * @return dto response, with an error field
     * @see #validateAddFavoriteHistory(AddFavoriteHiReq)
     */
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
            logger.warn("User ({}), History ({}) -> (addFavoriteHistory) error {}.", request.getUser() != null ? request.getUser().getId() : "null", request.getHistoryId(), e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}), History ({}) -> (addFavoriteHistory) error {}.", request.getUser() != null ? request.getUser().getId() : "null", request.getHistoryId(), ex.getMessage());
        }
        return response;
    }

    /**
     * The method implements deleting history from the saved stories tab for this user
     *
     * @param request contains the main parameters for delete favorite history
     * @return dto response, with an error field
     * @see #validateAddFavoriteHistory(AddFavoriteHiReq)
     */
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
            logger.warn("User ({}), History ({}) -> (deleteFavoriteHistory) error {}.", request.getUser() != null ? request.getUser().getId() : "null", request.getHistoryId(), e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}), History ({}) -> (deleteFavoriteHistory) error {}.", request.getUser() != null ? request.getUser().getId() : "null", request.getHistoryId(), ex.getMessage());
        }
        return response;
    }

    /**
     * The method returns a list of all tags
     *
     * @return list tags
     */
    public List<Tag> getAllTag() {
        return tagRepo.findAll();
    }


    private void validateGetHistoryById(long id) throws ServerException {
        if (id <= 0) {
            throw new ServerException(AnswerErrorCode.HISTORY_ID_NOT_EXIST);
        }
    }

    private void validateAddComment(User user, AddCommentDtoReq request) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (request.getId() <= 0) {
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
        if (numPage < 0) {
            throw new ServerException(AnswerErrorCode.HISTORY_PAGE_ERROR);
        }
    }

    private void validateCreateHistory(User user, CreateHistoryDtoReq dto) throws ServerException {
        if (user == null || user.getUsername() == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (dto.getTitle() == null || dto.getTitle().length() < 3) {
            throw new ServerException(AnswerErrorCode.HISTORY_TITLE_ERROR);
        }
        if (!historyRepo.findAllByTitle(dto.getTitle()).isEmpty()) {
            throw new ServerException(AnswerErrorCode.HISTORY_TITLE_ALREADY_EXIST);
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
        if (dto == null) {
            throw new ServerException(AnswerErrorCode.REQUEST_IS_NULL);
        }
        if (dto.getId() < 1) {
            throw new ServerException(AnswerErrorCode.HISTORY_ID_NOT_EXIST);
        }
        if (user == null || user.getUsername() == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }

        if (dto.getImgFile() == null) {
            throw new ServerException(AnswerErrorCode.HISTORY_IMG_ERROR);
        }

        List<History> historyByTitle = historyRepo.findAllByTitle(dto.getTitle());
        History historyUpdate = historyRepo.findById(dto.getId()).orElseThrow();

        if (!historyByTitle.isEmpty() && historyUpdate.getId() != historyByTitle.get(0).getId()) {
            throw new ServerException(AnswerErrorCode.HISTORY_TITLE_ALREADY_EXIST);
        }
        if (dto.getTitle() == null || dto.getTitle().length() < 3) {
            throw new ServerException(AnswerErrorCode.HISTORY_TITLE_ERROR);
        }
        if (dto.getTag() == null || dto.getTag().isEmpty()) {
            throw new ServerException(AnswerErrorCode.HISTORY_TAG_ERROR);
        }
        if (dto.getBigText() == null || dto.getBigText().length() < 20) {
            throw new ServerException(AnswerErrorCode.HISTORY_SHORT_TEXT);
        }
    }

    private void validateDeleteHistory(DeleteHistoryDtoReq request) throws ServerException {
        if (request.getId() <= 0) {
            throw new ServerException(AnswerErrorCode.HISTORY_ID_NOT_EXIST);
        }
        if (request.getUser() == null) {
            throw new ServerException(AnswerErrorCode.HISTORY_USER_NOT_FOUND);
        }
    }

    private void validateUser(User user) throws ServerException {
        if (user == null || user.getId() <= 0) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
    }

    private void validateGetHistory(User user, long id) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (id <= 0) {
            throw new ServerException(AnswerErrorCode.HISTORY_USER_NOT_FOUND);
        }
    }

    private void validateAddFavoriteHistory(AddFavoriteHiReq request) throws ServerException {
        if (request.getHistoryId() <= 0) {
            throw new ServerException(AnswerErrorCode.FAVORITE_HISTORY_ID_ERROR);
        }
        if (request.getUser() == null) {
            throw new ServerException(AnswerErrorCode.FAVORITE_USER_ERROR);
        }
    }

    private void validateDeleteFavoriteHistory(DeleteFavoriteHiDtoReq request) throws ServerException {
        if (request.getHistoryId() <= 0) {
            throw new ServerException(AnswerErrorCode.FAVORITE_HISTORY_ID_ERROR);
        }
        if (request.getUser() == null) {
            throw new ServerException(AnswerErrorCode.FAVORITE_USER_ERROR);
        }
    }

    private void validateGetAllFavoriteByUser(User user) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.FAVORITE_USER_ERROR);
        }
        if (user.getId() <= 0) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
    }
}
