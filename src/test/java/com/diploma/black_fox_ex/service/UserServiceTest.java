//package com.diploma.black_fox_ex.service;
//
//import com.diploma.black_fox_ex.dto.DeleteHelpDtoReq;
//import com.diploma.black_fox_ex.dto.UpdateUserDtoReq;
//import com.diploma.black_fox_ex.exeptions.AnswerErrorCode;
//import com.diploma.black_fox_ex.io.FileDirectories;
//import com.diploma.black_fox_ex.model.SupportAnswer;
//import com.diploma.black_fox_ex.model.User;
//import com.diploma.black_fox_ex.repositories.UserRepo;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
//class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//
//    @MockBean
//    private UserRepo userRepo;
//
//    @Test
//    void loadUserByUsername() {
//        User user = new User();
//        user.setUsername("Stanislav");
//        Mockito.doReturn(user).when(userRepo).findByUsername("Stanislav");
//        var userDetails = userService.loadUserByUsername("Stanislav");
//        assertEquals(userDetails.getUsername(),user.getUsername());
//    }
//
//    @Test
//    void getUserMenu() {
//        assertNull(userService.getUserMenu(null));
//        assertNotNull(userService.getUserMenu(new User()));
//        User user = new User();
//        user.setUsername("Stas");
//        user.setImgFile("img");
//        user.setEmail("stas@mail.ru");
//
//        var resp = userService.getUserMenu(user);
//
//        assertEquals(user.getEmail(), resp.getEmail());
//        assertEquals(user.getUsername(), resp.getUsername());
//        assertEquals(FileDirectories.USER_IMG.getPath() + user.getImgFile(), resp.getImgFile());
//    }
//
//    @Test
//    void getUserProfile() {
//        assertNull(userService.getUserProfile(null));
//        assertNotNull(userService.getUserProfile(new User()));
//        User user = new User("Stanislav", "stas@mail.ru", "12345", (byte) 120, "big sex", "no comments", "img", "@StasseeX");
//
//        var resp = userService.getUserProfile(user);
//        assertEquals(user.getUsername(), resp.getUsername());
//        assertEquals(user.getEmail(), resp.getEmail());
//        assertEquals(user.getPassword(), resp.getPassword());
//        assertEquals(user.getAge(), resp.getAge());
//        assertEquals(user.getSex(), resp.getSex());
//        assertEquals(user.getInfo(), resp.getInfo());
//        assertEquals(user.getTelegramUsername(), resp.getTelegramUsername());
//        assertEquals(FileDirectories.USER_IMG.getPath() + user.getImgFile(), resp.getImgFile());
//    }
//
////    @Test
////    void registrationUser() throws IOException {
////        FileInputStream fis = new FileInputStream("src/main/resources/img/history-img/history.png");
////        MockMultipartFile multipartFile = new MockMultipartFile("file", fis);
////
////        var dtoTrue = new UserDto("Stanislav", "stas@mail.ru", "fdk1kj2kjJHF", "10",
////                "big sex big sex big sex big sex big sex", multipartFile, "boy");
////
////        var dtoUsernameErr = new UserDto("S", "stas@mail.ru", "fdk1kj2kjJHF", "10",
////                "big sex big sex big sex big sex big sex", multipartFile, "boy");
////
////        var dtoEmailErr = new UserDto("Stanislav", "stasail.ru", "fdk1kj2kjJHF", "10",
////                "big sex big sex big sex big sex big sex", multipartFile, "boy");
////
////        var dtoPassErr = new UserDto("Stanislav", "stas@mail.ru", "1", "10",
////                "big sex big sex big sex big sex big sex", multipartFile, "boy");
////
////        var dtoAgeRangErr = new UserDto("Stanislav", "stas@mail.ru", "fdk1kj2kjJHF", "2000",
////                "big sex big sex big sex big sex big sex", multipartFile, "boy");
////
////        var dtoAgeSynErr = new UserDto("Stanislav", "stas@mail.ru", "fdk1kj2kjJHF", "ss",
////                "big sex big sex big sex big sex big sex", multipartFile, "boy");
////
////        var dtoInfoErr = new UserDto("Stanislav", "stas@mail.ru", "fdk1kj2kjJHF", "10",
////                "bi", multipartFile, "boy");
////
////        var dtoImgErr = new UserDto("Stanislav", "stas@mail.ru", "fdk1kj2kjJHF", "10",
////                "big sex big sex big sex big sex big sex", null, "boy");
////
////        var dtoSexErr = new UserDto("Stanislav", "stas@mail.ru", "fdk1kj2kjJHF", "10",
////                "big sex big sex big sex big sex big sex", multipartFile, null);
////
////        var dtoExist = new UserDto("Julia", "stas@mail.ru", "fdk1kj2kjJHF", "10",
////                "big sex big sex big sex big sex big sex", multipartFile, "boy");
////
////        User user = new User();
////        user.setUsername("Julia");
////        Mockito.doReturn(user).when(userRepo).findByUsername("Julia");
////
////        var respTrue = userService.registrationUser(dtoTrue);
////        var respUserErr = userService.registrationUser(dtoUsernameErr);
////        var respEmailErr = userService.registrationUser(dtoEmailErr);
////        var respPassErr = userService.registrationUser(dtoPassErr);
////        var respInfoErr = userService.registrationUser(dtoInfoErr);
////        var respAgeRangeErr = userService.registrationUser(dtoAgeRangErr);
////        var respAgeSynErr = userService.registrationUser(dtoAgeSynErr);
////        var respImgErr = userService.registrationUser(dtoImgErr);
////        var respSexErr = userService.registrationUser(dtoSexErr);
////        var respExistUser = userService.registrationUser(dtoExist);
////
////        assertEquals(respUserErr.getError(), AnswerErrorCode.REGISTRATION_WRONG_USERNAME.getMsg());
////        assertEquals(respEmailErr.getError(), AnswerErrorCode.REGISTRATION_WRONG_VALIDATE_EMAIL.getMsg());
////        assertEquals(respPassErr.getError(), AnswerErrorCode.REGISTRATION_WRONG_VALIDATE_PASSWORD.getMsg());
////        assertEquals(respSexErr.getError(), AnswerErrorCode.REGISTRATION_WRONG_VALIDATE_SEX.getMsg());
////        assertEquals(respInfoErr.getError(), AnswerErrorCode.REGISTRATION_WRONG_VALIDATE_INFO.getMsg());
////        assertEquals(respImgErr.getError(), AnswerErrorCode.REGISTRATION_WRONG_VALIDATE_IMG.getMsg());
////        assertEquals(respAgeRangeErr.getError(), AnswerErrorCode.REGISTRATION_WRONG_AGE_RANGE.getMsg());
////        assertEquals(respAgeSynErr.getError(), AnswerErrorCode.REGISTRATION_WRONG_AGE_SYNTAX.getMsg());
////        assertEquals(respExistUser.getError(), AnswerErrorCode.REGISTRATION_USERNAME_ALREADY_EXIST.getMsg());
////        assertNull(respTrue.getError());
////
////        Mockito.verify(userRepo, Mockito.times(1)).save(any());
////    }
//
////    @Test
////    void updateUser() throws IOException {
////        FileInputStream fis = new FileInputStream("src/main/resources/img/history-img/history.png");
////        MockMultipartFile multipartFile = new MockMultipartFile("file", fis);
////
////        var dtoTrue = new UpdateUserDtoReq("Stanislav", "stas@mail.ru", "fdk1kj2kjJHF", (byte) 10,
////                "big sex big sex big sex big sex big sex", multipartFile, "boy", "@StaseeX");
////
////        var dtoUsernameErr = new UpdateUserDtoReq("S", "stas@mail.ru", "fdk1kj2kjJHF", (byte) 10,
////                "big sex big sex big sex big sex big sex", multipartFile, "boy", "@StaseeX");
////
////        var dtoEmailErr = new UpdateUserDtoReq("Stanislav", "stasail.ru", "fdk1kj2kjJHF", (byte) 10,
////                "big sex big sex big sex big sex big sex", multipartFile, "boy", "@StaseeX");
////
////        var dtoPassErr = new UpdateUserDtoReq("Stanislav", "stas@mail.ru", "1", (byte) 10,
////                "big sex big sex big sex big sex big sex", multipartFile, "boy", "@StaseeX");
////
////        var dtoAgeRangErr = new UpdateUserDtoReq("Stanislav", "stas@mail.ru", "fdk1kj2kjJHF", "2000",
////                "big sex big sex big sex big sex big sex", multipartFile, "boy", "@StaseeX");
////
////        var dtoAgeSynErr = new UpdateUserDtoReq("Stanislav", "stas@mail.ru", "fdk1kj2kjJHF", "ss",
////                "big sex big sex big sex big sex big sex", multipartFile, "boy", "@StaseeX");
////
////        var dtoInfoErr = new UpdateUserDtoReq("Stanislav", "stas@mail.ru", "fdk1kj2kjJHF", "10",
////                "bi", multipartFile, "boy", "@StaseeX");
////
////        var dtoSexErr = new UpdateUserDtoReq("Stanislav", "stas@mail.ru", "fdk1kj2kjJHF", "10",
////                "big sex big sex big sex big sex big sex", multipartFile, null, "@StaseeX");
////
////        var dtoTelegram = new UpdateUserDtoReq("Stanislav", "stas@mail.ru", "fdk1kj2kjJHF", "10",
////                "big sex big sex big sex big sex big sex", multipartFile, "boy", "asa");
////
////        User user = new User();
////        user.setId(1L);
////        user.setUsername("Stanislav");
////
////        var respTrue = userService.updateUser(user, dtoTrue);
////        var respNull = userService.updateUser(null, dtoTrue);
////        var respUserErr = userService.updateUser(user, dtoUsernameErr);
////        var respEmailErr = userService.updateUser(user, dtoEmailErr);
////        var respPassErr = userService.updateUser(user, dtoPassErr);
////        var respInfoErr = userService.updateUser(user, dtoInfoErr);
////        var respAgeRangeErr = userService.updateUser(user, dtoAgeRangErr);
////        var respAgeSynErr = userService.updateUser(user, dtoAgeSynErr);
////        var respSexErr = userService.updateUser(user, dtoSexErr);
////        var respTelegramUser = userService.updateUser(user, dtoTelegram);
////
////        assertEquals(respNull.getErrors(), AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
////        assertEquals(respUserErr.getErrors(), AnswerErrorCode.UPDATE_NOT_ROOT_UPDATE.getMsg());
////        assertEquals(respEmailErr.getErrors(), AnswerErrorCode.UPDATE_WRONG_VALIDATE_EMAIL.getMsg());
////        assertEquals(respPassErr.getErrors(), AnswerErrorCode.UPDATE_WRONG_VALIDATE_PASSWORD.getMsg());
////        assertEquals(respSexErr.getErrors(), AnswerErrorCode.UPDATE_WRONG_VALIDATE_SEX.getMsg());
////        assertEquals(respInfoErr.getErrors(), AnswerErrorCode.UPDATE_WRONG_VALIDATE_INFO.getMsg());
////        assertEquals(respTelegramUser.getErrors(), AnswerErrorCode.UPDATE_WRONG_TELEGRAM_ADDRESS.getMsg());
////        assertEquals(respAgeSynErr.getErrors(), AnswerErrorCode.UPDATE_WRONG_AGE_SYNTAX.getMsg());
////        assertEquals(respAgeRangeErr.getErrors(), AnswerErrorCode.UPDATE_WRONG_AGE_RANGE.getMsg());
////        assertNull(respTrue.getErrors());
////
////        Mockito.verify(userRepo, Mockito.times(1)).save(any());
////    }
//
//    @Test
//    void deleteUser() {
//        User user = new User();
//        user.setUsername("Stanislav");
//        var respTrue = userService.deleteUser(user, "Stanislav");
//        var respUserNull = userService.deleteUser(null, "Stanislav");
//        var respUsername = userService.deleteUser(user, "Pasha");
//
//        assertEquals(respUsername.getError(), AnswerErrorCode.UPDATE_NOT_ROOT_UPDATE.getMsg());
//        assertEquals(respUserNull.getError(), AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
//        assertNull(respTrue.getError());
//
//        Mockito.verify(userRepo, Mockito.times(1)).delete(user);
//    }
//
//    @Test
//    void getAllAnswersSupportByUserDto() {
//        User user = new User();
//        user.setId(1L);
//
//        List<SupportAnswer> listAns = new ArrayList<>(List.of(new SupportAnswer(1), new SupportAnswer(2)));
//
//        Mockito.doReturn(listAns).when(userRepo).findSupportAnswerById(1);
//
//        var dtoTrue = userService.getAllAnswersSupportByUserDto(user);
//        var dtoUserNull = userService.getAllAnswersSupportByUserDto(null);
//
//        assertNull(dtoTrue.getErrors());
//        assertEquals(dtoTrue.getAnswers(), listAns);
//        assertEquals(dtoUserNull.getErrors(), AnswerErrorCode.ERROR_ANSWER_BY_USER.getMsg());
//
//    }
//
//    @Test
//    void deleteAnswerByUser() {
//        User userReq = new User();
//        userReq.setUsername("Stanislav");
//        userReq.setId(1L);
//
//        User userResp = new User();
//        userResp.setUsername("Stanislav");
//        userResp.setSupportAnswer(new ArrayList<>(List.of(new SupportAnswer(1), new SupportAnswer(2), new SupportAnswer(3))));
//
//        var dtoTrue = new DeleteHelpDtoReq(userReq, 1);
//        var dtoIdErr = new DeleteHelpDtoReq(userReq, -1);
//        var dtoUserNull = new DeleteHelpDtoReq(null, 1);
//
//        Mockito.doReturn(userResp).when(userRepo).findByUsername("Stanislav");
//
//        var respTrue = userService.deleteAnswerByUser(dtoTrue);
//        var respIdErr = userService.deleteAnswerByUser(dtoIdErr);
//        var respUserNull = userService.deleteAnswerByUser(dtoUserNull);
//
//        assertNull(respTrue.getErrors());
//        assertEquals(respIdErr.getErrors(), AnswerErrorCode.ERROR_ANSWER_BY_USER.getMsg());
//        assertEquals(respUserNull.getErrors(), AnswerErrorCode.USER_NOT_REGISTERED.getMsg());
//
//        Mockito.verify(userRepo, Mockito.times(1)).save(any());
//    }
//}