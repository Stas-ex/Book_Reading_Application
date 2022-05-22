package com.diploma.black_fox_ex.service;

import com.diploma.black_fox_ex.exeptions.AnswerErrorCode;
import com.diploma.black_fox_ex.model.History;
import com.diploma.black_fox_ex.model.Tag;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.repositories.HistoryRepo;
import com.diploma.black_fox_ex.repositories.TagRepo;
import com.diploma.black_fox_ex.repositories.UserRepo;
import com.diploma.black_fox_ex.request.CreateHistoryDtoReq;
import com.diploma.black_fox_ex.request.DeleteHistoryDtoReq;
import com.diploma.black_fox_ex.request.UpdateHistoryDtoReq;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.IOException;
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
    void createHistoryRepo() throws IOException {
        User user = new User();
        user.setUsername("stanislav");
        user.setId(1);
        FileInputStream fis = new FileInputStream("src/main/resources/img/history-img/history.png");
        MockMultipartFile multipartFile = new MockMultipartFile("file", fis);
        var dtoTrue = new CreateHistoryDtoReq("Good Title", multipartFile, "text text text text text text", "Fantasy");

        Mockito.doReturn(new Tag()).when(tagRepo).findByName("Fantasy");
        historyService.createHistory(user, dtoTrue);

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


        Mockito.doReturn(new Tag()).when(tagRepo).findByName("Fantasy");
        Mockito.doReturn(new History("Title", "img", "bigText")).when(userRepo).findHistoryById(1, 1);

        //Create update
        var dtoTrue = new UpdateHistoryDtoReq(1, "Best Title Update", "text text text text text text", multipartFile, user.getUsername(), "Fantasy");
        var dtoIdErr = new UpdateHistoryDtoReq(0, "Best Title Update", "text text text text text text", multipartFile, user.getUsername(), "Fantasy");
        var dtoImgErr = new UpdateHistoryDtoReq(1, "Best Title Update", "text text text text text text", null, user.getUsername(), "Fantasy");
        var dtoTagNull = new UpdateHistoryDtoReq(1, "Best Title Update", "text text text text text text", multipartFile, user.getUsername(), null);
        var dtoTagErr = new UpdateHistoryDtoReq(1, "Best Title Update", "text text text text text text", multipartFile, user.getUsername(), "tag");
        var dtoTitleErr = new UpdateHistoryDtoReq(1, "11", "text text text text text text", multipartFile, user.getUsername(), "Fantasy");
        var dtoBigTextErr = new UpdateHistoryDtoReq(1, "Best Title Update", "small", multipartFile, user.getUsername(), "Fantasy");


        var respNullReq = historyService.updateHistory(user, null);
        var respIdErr = historyService.updateHistory(user, dtoIdErr);
        var respUserNull = historyService.updateHistory(null, dtoTrue);
        var respTitleErr = historyService.updateHistory(user, dtoTitleErr);
        var respBigErr = historyService.updateHistory(user, dtoBigTextErr);
        var respTagNull = historyService.updateHistory(user, dtoTagNull);
        var respImgErr = historyService.updateHistory(user, dtoImgErr);
        var respTagErr = historyService.updateHistory(user, dtoTagErr);

        assertEquals(respNullReq.getError(), AnswerErrorCode.REQUEST_IS_NULL.getMsg());
        assertEquals(respIdErr.getError(), AnswerErrorCode.HISTORY_ID_NOT_EXIST.getMsg());
        assertEquals(respUserNull.getError(), AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
        assertEquals(respTitleErr.getError(), AnswerErrorCode.HISTORY_TITLE_ERROR.getMsg());
        assertEquals(respBigErr.getError(), AnswerErrorCode.HISTORY_SHORT_TEXT.getMsg());
        assertEquals(respTagNull.getError(), AnswerErrorCode.HISTORY_TAG_ERROR.getMsg());
        assertEquals(respTagErr.getError(), AnswerErrorCode.HISTORY_TAG_ERROR.getMsg());
        assertNull(respImgErr.getError());

        Mockito.verify(historyRepo, Mockito.times(1)).save(any());
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

        Mockito.doReturn(List.of(history1,history2,history3)
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
    void getHistory() {
        User user = new User();
        user.setId(1);

        History history1 = new History("H1", " ", " ");
        history1.setTag(new Tag("tag"));
        history1.setId(1);

        Mockito.doReturn(List.of(history1)).when(userRepo).findAllById(1);

        var respTrue = historyService.getHistoryEditById(user,1);
        var respIdErr = historyService.getHistoryEditById(user,0);
        var respUserNull = historyService.getHistoryEditById(null,111);
        var respHistoryByIdErr = historyService.getHistoryEditById(user,33);

        assertNull(respTrue.getError());
        assertEquals(history1.getTitle(), respTrue.getHistoryDto().getTitle());
        assertEquals(respIdErr.getError(),AnswerErrorCode.HISTORY_USER_NOT_FOUND.getMsg());
        assertEquals(respUserNull.getError(),AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
        assertEquals(respHistoryByIdErr.getError(),AnswerErrorCode.HISTORY_NOT_FOUND.getMsg());
    }
}