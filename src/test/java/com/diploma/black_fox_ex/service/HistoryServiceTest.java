package com.diploma.black_fox_ex.service;

import com.diploma.black_fox_ex.exeptions.AnswerErrorCode;
import com.diploma.black_fox_ex.model.History;
import com.diploma.black_fox_ex.model.Tag;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.repositories.CommentsRepo;
import com.diploma.black_fox_ex.repositories.HistoryRepo;
import com.diploma.black_fox_ex.repositories.TagRepo;
import com.diploma.black_fox_ex.repositories.UserRepo;
import com.diploma.black_fox_ex.request.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class HistoryServiceTest {

    @Autowired
    private HistoryService historyService;

    @MockBean
    private HistoryRepo historyRepo;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private TagRepo tagRepo;

    @MockBean
    private CommentsRepo commentsRepo;


    @Test
    void createValidateHistory() throws IOException {
        FileInputStream fis = new FileInputStream("src/main/resources/img/history-img/history.png");
        MockMultipartFile multipartFile = new MockMultipartFile("file", fis);

        User user = new User();
        user.setUsername("stanislav");

        var dtoTrue = new CreateHistoryDtoReq("Good Title", multipartFile, "text text text text text text", "Fantasy");
        var dtoTitleErr = new CreateHistoryDtoReq("11", multipartFile, "text text text text text text", "Fantasy");
        var dtoImgErr = new CreateHistoryDtoReq("Good Title", null, "text text text text text text", "Fantasy");
        var dtoBigTextErr = new CreateHistoryDtoReq("Good Title", multipartFile, "text", "favorite");
        var dtoTagNull = new CreateHistoryDtoReq("Good Title", multipartFile, "text text text text text text", null);
        var dtoTagErr = new CreateHistoryDtoReq("Good Title", multipartFile, "text text text text text text", "error");

        Mockito.doReturn(new Tag()).when(tagRepo).findByName("Fantasy");

        var respTrue = historyService.createHistory(user, dtoTrue);
        var respUserNull = historyService.createHistory(null, dtoTrue);
        var respTitleErr = historyService.createHistory(user, dtoTitleErr);
        var respImgErr = historyService.createHistory(user, dtoImgErr);
        var respBigErr = historyService.createHistory(user, dtoBigTextErr);
        var respTagNull = historyService.createHistory(user, dtoTagNull);
        var respTagErr = historyService.createHistory(user, dtoTagErr);

        assertEquals(respUserNull.getError(), AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
        assertEquals(respTitleErr.getError(), AnswerErrorCode.HISTORY_TITLE_ERROR.getMsg());
        assertEquals(respImgErr.getError(), AnswerErrorCode.HISTORY_IMG_ERROR.getMsg());
        assertEquals(respBigErr.getError(), AnswerErrorCode.HISTORY_SHORT_TEXT.getMsg());
        assertEquals(respTagNull.getError(), AnswerErrorCode.HISTORY_TAG_ERROR.getMsg());
        assertEquals(respTagErr.getError(), AnswerErrorCode.HISTORY_TAG_ERROR.getMsg());
        assertNull(respTrue.getError());

        Mockito.verify(userRepo, Mockito.times(1)).save(any());
    }

    @Test
    void createHistory() throws IOException {
        User user = new User();
        user.setId(1);
        user.setUsername("stanislav");
        FileInputStream fis = new FileInputStream("src/main/resources/img/history-img/history.png");
        MockMultipartFile multipartFile = new MockMultipartFile("file", fis);

        var dtoTrue = new CreateHistoryDtoReq("Good Title", multipartFile, "text text text text text text", "Fantasy");
        var dtoRepeat = new CreateHistoryDtoReq("Repeat Title", multipartFile, "text text text text text text", "Fantasy");
        var dtoTitle = new CreateHistoryDtoReq("G", multipartFile, "text text text text text text", "Fantasy");
        var dtoBigText = new CreateHistoryDtoReq("Good Title", multipartFile, "te", "Fantasy");
        var dtoImg = new CreateHistoryDtoReq("Good Title", null, "text text text text text text", "Fantasy");
        var dtoTag = new CreateHistoryDtoReq("Good Title", multipartFile, "text text text text text text", "");
        var dtoTagNotFound = new CreateHistoryDtoReq("Good Title", multipartFile, "text text text text text text", "tag");

      Mockito.doReturn(List.of(new History())).when(historyRepo).findAllByTitle("Repeat Title");
      Mockito.doReturn(new Tag()).when(tagRepo).findByName("Fantasy");

        var respTrue = historyService.createHistory(user, dtoTrue);
        var respUserNull = historyService.createHistory(null, dtoTrue);
        var respRepeat = historyService.createHistory(user, dtoRepeat);
        var respTitle = historyService.createHistory(user, dtoTitle);
        var respBigText = historyService.createHistory(user, dtoBigText);
        var respImg = historyService.createHistory(user, dtoImg);
        var respTag = historyService.createHistory(user, dtoTag);
        var respTagNotFound = historyService.createHistory(user, dtoTagNotFound);

        assertNull(respTrue.getError());
        assertEquals(respUserNull.getError(),AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
        assertEquals(respRepeat.getError(),AnswerErrorCode.HISTORY_TITLE_ALREADY_EXIST.getMsg());
        assertEquals(respTitle.getError(),AnswerErrorCode.HISTORY_TITLE_ERROR.getMsg());
        assertEquals(respBigText.getError(),AnswerErrorCode.HISTORY_SHORT_TEXT.getMsg());
        assertEquals(respImg.getError(),AnswerErrorCode.HISTORY_IMG_ERROR.getMsg());
        assertEquals(respTag.getError(),AnswerErrorCode.HISTORY_TAG_ERROR.getMsg());
        assertEquals(respTagNotFound.getError(),AnswerErrorCode.HISTORY_TAG_ERROR.getMsg());

        Mockito.verify(historyRepo, Mockito.times(1)).save(any());
        Mockito.verify(userRepo, Mockito.times(1)).save(any());
    }

    @Test
    void updateHistory() throws IOException {
        FileInputStream fis = new FileInputStream("src/main/resources/img/history-img/history.png");
        MockMultipartFile multipartFile = new MockMultipartFile("file", fis);
        User user = new User();
        user.setId(1);
        user.setUsername("Stanislav");

        History hiRepeat = new History("Title repeat", "img", "bigText");
        hiRepeat.setId(5);

        Mockito.doReturn(new Tag()).when(tagRepo).findByName("Fantasy");
        Mockito.doReturn(List.of(hiRepeat)).when(historyRepo).findAllByTitle("Title repeat");
        Mockito.doReturn(Optional.of(new History("Title repeat", "img", "bigText"))).when(historyRepo).findById(1L);
        Mockito.doReturn(new History("Title", "img", "bigText")).when(userRepo).findHistoryById(1,1);

        //Create update
        var dtoTrue = new UpdateHistoryDtoReq(1, "Best Title Update", "text text text text text text", multipartFile, user.getUsername(), "Fantasy");
        var dtoTrue2 = new UpdateHistoryDtoReq(1, "Best Title Update", "text text text text text text", multipartFile, user.getUsername(), "Fantasy");
        var dtoIdErr = new UpdateHistoryDtoReq(0, "Best Title Update", "text text text text text text", multipartFile, user.getUsername(), "Fantasy");
        var dtoImgErr = new UpdateHistoryDtoReq(1, "Best Title Update", "text text text text text text", null, user.getUsername(), "Fantasy");
        var dtoTagNull = new UpdateHistoryDtoReq(1, "Best Title Update", "text text text text text text", multipartFile, user.getUsername(), null);
        var dtoTagErr = new UpdateHistoryDtoReq(1, "Best Title Update", "text text text text text text", multipartFile, user.getUsername(), "tag");
        var dtoTitleShortErr = new UpdateHistoryDtoReq(1, "11", "text text text text text text", multipartFile, user.getUsername(), "Fantasy");
        var dtoTitleExistErr = new UpdateHistoryDtoReq(1, "Title repeat", "text text text text text text", multipartFile, user.getUsername(), "Fantasy");
        var dtoBigTextErr = new UpdateHistoryDtoReq(1, "Best Title Update jksfd", "small", multipartFile, user.getUsername(), "Fantasy");
        var dtoHistoryErr = new UpdateHistoryDtoReq(2, "Best Title Update", "small", multipartFile, user.getUsername(), "Fantasy");


        var respNullReq = historyService.updateHistory(user, null);
        var respIdErr = historyService.updateHistory(user, dtoIdErr);
        var respUserNull = historyService.updateHistory(null, dtoTrue);
        var respTitleShortErr = historyService.updateHistory(user, dtoTitleShortErr);
        var respTitleExistErr = historyService.updateHistory(user, dtoTitleExistErr);
        var respBigErr = historyService.updateHistory(user, dtoBigTextErr);
        var respTagNull = historyService.updateHistory(user, dtoTagNull);
        var respImgErr = historyService.updateHistory(user, dtoImgErr);
        var respTagErr = historyService.updateHistory(user, dtoTagErr);
        var respHistoryErr = historyService.updateHistory(user, dtoHistoryErr);

        var respTrue = historyService.updateHistory(user, dtoTrue);
        var respTrue2 = historyService.updateHistory(user, dtoTrue2);

        assertEquals(respNullReq.getError(), AnswerErrorCode.REQUEST_IS_NULL.getMsg());
        assertEquals(respIdErr.getError(), AnswerErrorCode.HISTORY_ID_NOT_EXIST.getMsg());
        assertEquals(respUserNull.getError(), AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
        assertEquals(respTitleShortErr.getError(), AnswerErrorCode.HISTORY_TITLE_ERROR.getMsg());
        assertEquals(respTitleExistErr.getError(), AnswerErrorCode.HISTORY_TITLE_ALREADY_EXIST.getMsg());
        assertEquals(respBigErr.getError(), AnswerErrorCode.HISTORY_SHORT_TEXT.getMsg());
        assertEquals(respTagNull.getError(), AnswerErrorCode.HISTORY_TAG_ERROR.getMsg());
        assertEquals(respTagErr.getError(), AnswerErrorCode.HISTORY_TAG_ERROR.getMsg());
        assertEquals(respImgErr.getError(), AnswerErrorCode.HISTORY_IMG_ERROR.getMsg());
        assertEquals(respHistoryErr.getError(), AnswerErrorCode.HISTORY_ID_NOT_EXIST.getMsg());
        assertNull(respTrue.getError());
        assertNull(respTrue2.getError());

        Mockito.verify(historyRepo, Mockito.times(2)).save(any());
    }

    @Test
    void deleteHistory() {
        User user = new User();
        user.setId(1);

        var dtoTrue = new DeleteHistoryDtoReq(1, new User());
        var dtoIdErr = new DeleteHistoryDtoReq(-1, new User());
        var dtoUserErr = new DeleteHistoryDtoReq(1, null);
        Mockito.doReturn(new History("Title", "img", "bigText")).when(userRepo).findHistoryById(1, 1);

        var respTrue = historyService.deleteHistory(dtoTrue);
        var respIdErr = historyService.deleteHistory(dtoIdErr);
        var respUserErr = historyService.deleteHistory(dtoUserErr);

        assertEquals(respIdErr.getError(), AnswerErrorCode.HISTORY_ID_NOT_EXIST.getMsg());
        assertEquals(respUserErr.getError(), AnswerErrorCode.HISTORY_USER_NOT_FOUND.getMsg());
        assertNull(respTrue.getError());

        Mockito.verify(userRepo).save(any());
        Mockito.verify(historyRepo).delete(any());
    }

    @Test
    void getAllHistoryByTag() {
        Mockito.doReturn(List.of(new History(), new History(), new History()))
                .when(historyRepo).findAll();
        User user = new User();
        user.setUsername("Stas");
        History history1 = new History(" ", " ", " ", new Tag("tag"));
        History history2 = new History(" ", " ", " ", new Tag("tags"));
        History history3 = new History(" ", " ", " ", new Tag("tag"));

        history1.setUsers(Collections.singleton(user));
        history2.setUsers(Collections.singleton(user));
        history3.setUsers(Collections.singleton(user));

        Mockito.doReturn(List.of(history1, history2)).when(historyRepo).findAllByTagName("MyTag");
        Mockito.doReturn(List.of(history1, history2, history3)).when(historyRepo).findAll();

        var respTrue = historyService.getAllHistoryByTag("MyTag", 0);
        var respAll = historyService.getAllHistoryByTag("all", 0);
        var respTagErr = historyService.getAllHistoryByTag(null, 1);
        var respPageErr = historyService.getAllHistoryByTag("MyTag", -1);
        var respPageEmptyErr = historyService.getAllHistoryByTag("tag", 0);

        assertEquals(respTagErr.getError(), AnswerErrorCode.HISTORY_TAG_ERROR.getMsg());
        assertEquals(respPageErr.getError(), AnswerErrorCode.HISTORY_PAGE_ERROR.getMsg());
        assertEquals(respPageEmptyErr.getError(), AnswerErrorCode.PAGE_IS_EMPTY.getMsg());
        assertNull(respTrue.getError());
        assertEquals(2, respTrue.getListDto().size());
        assertNull(respAll.getError());
        assertEquals(3, respAll.getListDto().size());
    }

    @Test
    void getHistoryById() {
        User user = new User();
        user.setId(1);
        Optional<History> history = Optional.of(new History("Title", "img.jpg", "Bigtext"));
        Mockito.doReturn(history).when(historyRepo).findById(1L);
        var respIdErr = historyService.getHistoryById(user, 0);
        var respTrue = historyService.getHistoryById(user, 1);

        assertEquals(respIdErr.getError(), AnswerErrorCode.HISTORY_ID_NOT_EXIST.getMsg());
        assertEquals(history.get().getTitle(), respTrue.getHistoryDto().getTitle());
    }

    @Test
    void getAllFavoriteByUser() {
        User user = new User();
        History history1 = new History("H1", " ", " ");
        History history2 = new History("H2", " ", " ");
        History history3 = new History("H3", " ", " ");

        history1.setTag(new Tag("tag"));
        history2.setTag(new Tag("tag"));
        history3.setTag(new Tag("tag"));

        Mockito.doReturn(List.of(history1, history2, history3)
        ).when(userRepo).findFavoriteHistoryById(1);

        var dtoUserId = historyService.getAllFavoriteByUser(user);
        user.setId(1);
        var dtoUserNull = historyService.getAllFavoriteByUser(null);
        var dtoTrue = historyService.getAllFavoriteByUser(user);

        assertEquals(dtoUserNull.getError(), AnswerErrorCode.FAVORITE_USER_ERROR.getMsg());
        assertEquals(dtoUserId.getError(), AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
        assertEquals(3, dtoTrue.getListDto().size());
        assertNull(dtoTrue.getError());
    }

    @Test
    void getHistoryEditById() {
        User user = new User();
        user.setId(1);

        History history1 = new History("H1", " ", " ");
        history1.setTag(new Tag("tag"));
        history1.setId(1);

        Mockito.doReturn(List.of(history1)).when(userRepo).findAllById(1);

        var respTrue = historyService.getHistoryEditById(user, 1);
        var respIdErr = historyService.getHistoryEditById(user, 0);
        var respUserNull = historyService.getHistoryEditById(null, 111);
        var respHistoryByIdErr = historyService.getHistoryEditById(user, 33);

        assertNull(respTrue.getError());
        assertEquals(history1.getTitle(), respTrue.getHistoryDto().getTitle());
        assertEquals(respIdErr.getError(), AnswerErrorCode.HISTORY_USER_NOT_FOUND.getMsg());
        assertEquals(respUserNull.getError(), AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
        assertEquals(respHistoryByIdErr.getError(), AnswerErrorCode.HISTORY_NOT_FOUND.getMsg());
    }

    @Test
    void addComment() {
        User user = new User();
        var dtoTrue = new AddCommentDtoReq(1, "My bigText My bigText My bigText My bigText My bigText", "Red");
        var dtoIdErr = new AddCommentDtoReq(0, "Helloasdaasdasdasdasdsd", "Red");
        var dtoColorNull = new AddCommentDtoReq(1, "Helloasasdasdasdasddasd", null);
        var dtoBigText = new AddCommentDtoReq(1, "Hello", "Red");

        Optional<History> history = Optional.of(new History("Title", "img.jpg", "Bigtext"));
        Mockito.doReturn(history).when(historyRepo).findById(1L);

        var respTrue = historyService.addComment(user, dtoTrue);
        var respUserNull = historyService.addComment(null, dtoTrue);
        var respIdErr = historyService.addComment(user, dtoIdErr);
        var respColorNull = historyService.addComment(user, dtoColorNull);
        var respBigTextSmall = historyService.addComment(user, dtoBigText);

        assertNull(respTrue.getError());
        assertEquals(respUserNull.getError(), AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
        assertEquals(respIdErr.getError(), AnswerErrorCode.HISTORY_ID_NOT_EXIST.getMsg());
        assertEquals(respColorNull.getError(), AnswerErrorCode.COMMENT_COLOR_ERROR.getMsg());
        assertEquals(respBigTextSmall.getError(), AnswerErrorCode.COMMENT_BIG_TEXT_ERROR.getMsg());

        Mockito.verify(commentsRepo).save(any());
        Mockito.verify(historyRepo).save(any());
        Mockito.verify(userRepo).save(any());
    }

    @Test
    void getAllHistoryByUser() {
        Mockito.doReturn(List.of(new History(), new History(), new History()))
                .when(historyRepo).findAll();
        User user = new User();
        user.setId(1L);
        History history1 = new History("h1 ", " ", " ", new Tag("tag"));
        History history2 = new History("h2 ", " ", " ", new Tag("tags"));
        History history3 = new History("h3 ", " ", " ", new Tag("tag"));

        history1.setUsers(Collections.singleton(user));
        history2.setUsers(Collections.singleton(user));
        history3.setUsers(Collections.singleton(user));

        Mockito.doReturn(List.of(history1, history2)).when(userRepo).findAllById(1);

        var respTrue = historyService.getAllHistoryByUser(user);
        var respUserErr = historyService.getAllHistoryByUser(null);
        user.setId(-100);
        var respUserIdErr = historyService.getAllHistoryByUser(user);

        assertNull(respTrue.getError());
        assertEquals(respUserErr.getError(), AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
        assertEquals(respUserIdErr.getError(), AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
        assertEquals(2, respTrue.getHistoryListDto().size());
    }

    @Test
    void addFavoriteHistory() {
        User user = new User();
        user.setId(1L);
        Optional<History> history = Optional.of(new History("Title", "img.jpg", "Bigtext"));
        History history1 = new History("H1", " ", " ");
        History history2 = new History("H2", " ", " ");
        History history3 = new History("H3", " ", " ");

        history1.setTag(new Tag("tag"));
        history2.setTag(new Tag("tag"));
        history3.setTag(new Tag("tag"));

        Mockito.doReturn(new ArrayList<>(List.of(history1, history2, history3))).when(userRepo).findFavoriteHistoryById(1);
        Mockito.doReturn(history).when(historyRepo).findById(1L);

        var dtoTrue = new AddFavoriteHiReq(user, 1);
        var dtoIdErr = new AddFavoriteHiReq(user, 0);
        var dtoUserNull = new AddFavoriteHiReq(null, 1);

        var respIdErr = historyService.addFavoriteHistory(dtoIdErr);
        var respUserNull = historyService.addFavoriteHistory(dtoUserNull);
        var respTrue = historyService.addFavoriteHistory(dtoTrue);

        assertEquals(respIdErr.getError(), AnswerErrorCode.FAVORITE_HISTORY_ID_ERROR.getMsg());
        assertEquals(respUserNull.getError(), AnswerErrorCode.FAVORITE_USER_ERROR.getMsg());
        assertNull(respTrue.getError());

        Mockito.verify(historyRepo).save(any());
        Mockito.verify(userRepo).save(any());
    }

    @Test
    void deleteFavoriteHistory() {
        User user = new User();
        user.setId(1L);
        History history1 = new History("H1", " ", " ");
        History history2 = new History("H2", " ", " ");
        History history3 = new History("H3", " ", " ");

        history1.setTag(new Tag("tag"));
        history2.setTag(new Tag("tag"));
        history3.setTag(new Tag("tag"));

        history1.setId(1);
        history2.setId(2);
        history3.setId(3);

        List<History> list = new ArrayList<>(List.of(history1, history2, history3));

        Mockito.doReturn(list).when(userRepo).findFavoriteHistoryById(1);
        Mockito.doReturn(Optional.of(history1)).when(historyRepo).findById(1L);

        var dtoTrue = new DeleteFavoriteHiDtoReq(1, user);
        var dtoIdErr = new DeleteFavoriteHiDtoReq(0, user);
        var dtoUserNull = new DeleteFavoriteHiDtoReq(1, null);

        var respIdErr = historyService.deleteFavoriteHistory(dtoIdErr);
        var respUserNull = historyService.deleteFavoriteHistory(dtoUserNull);
        var respTrue = historyService.deleteFavoriteHistory(dtoTrue);

        assertEquals(respIdErr.getError(), AnswerErrorCode.FAVORITE_HISTORY_ID_ERROR.getMsg());
        assertEquals(respUserNull.getError(), AnswerErrorCode.FAVORITE_USER_ERROR.getMsg());
        assertNull(respTrue.getError());

        list.remove(history1);
        user.setFavoriteStories(list);
        Mockito.verify(userRepo).save(user);
        Mockito.verify(historyRepo).save(any());
    }

    @Test
    void getAllTag() {
        List<Tag> tagList = new ArrayList<>(List.of(
                new Tag("1"),
                new Tag("2"),
                new Tag("3"),
                new Tag("4"))
        );

        Mockito.doReturn(tagList).when(tagRepo).findAll();
        List<Tag> respListTag = historyService.getAllTag();
        assertEquals(respListTag,tagList);
    }
    @Test
    void getAllTagNull() {
        List<Tag> respListTag = historyService.getAllTag();
        assertTrue(respListTag.isEmpty());
    }
}