package com.diplom.black_fox_ex.service;

import com.diplom.black_fox_ex.exeptions.AnswerErrorCode;
import com.diplom.black_fox_ex.exeptions.ServerException;
import com.diplom.black_fox_ex.repositories.UserRepo;
import com.diplom.black_fox_ex.model.SupportAnswer;
import com.diplom.black_fox_ex.io.FileDirectories;
import com.diplom.black_fox_ex.io.FileManager;
import com.diplom.black_fox_ex.model.History;
import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.request.*;
import com.diplom.black_fox_ex.response.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;


@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final FileManager fileManager = new FileManager();
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final String USERNAME_PATTERN = "^[A-z]{3,20}";
    private final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,15})";
    private final String EMAIL_PATTERN = "([A-z0-9_.-]{1,})@([A-z0-9_.-]{1,}).([A-z]{2,8})";
    private final String TELEGRAM_PATTERN = ".*\\B@(?=\\w{5,32}\\b)[a-zA-Z0-9]+(?:_[a-zA-Z0-9]+)*.*";

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    //------------------------------------------------------------------------------------------------------//
    public GetUserMenuDtoResp getUserMenu(User user) {
        if (user == null) return null;
        return new GetUserMenuDtoResp(user);
    }

    public GetUserProfileDtoResp getUserProfile(User user) {
        if (user == null) return null;
        return new GetUserProfileDtoResp(user);
    }

    public RegistrationUserDtoResp registrationUser(RegistrationUserDtoReq dto) {
        var dtoResponse = new RegistrationUserDtoResp();
        try {
            validateRegistrationUser(dto);
            dto.setFileName(fileManager.createFile(FileDirectories.USER_IMG, dto.getImg()));
            User user = new User(
                    dto.getUsername(), dto.getEmail(), dto.getPassword(),
                    Integer.parseInt(dto.getAge()), dto.getSex(), dto.getInfo(),
                    dto.getFileName(), dto.isActive()
            );
            userRepo.save(user);
        } catch (ServerException ex) {
            dtoResponse.setErrors(ex.getErrorMessage());
            logger.warn("(registrationUser) error -> {}",ex.getErrorMessage());
        }catch (Exception ex){
            logger.error("(registrationUser) error: {}", ex.getMessage());
        }
        return dtoResponse;
    }

    public UpdateUserDtoResp updateUser(User userOld, UpdateUserDtoReq userDto) {
        var response = new UpdateUserDtoResp();
        try {
            validateUpdateUser(userOld, userDto);
            if (userDto.getImgFile() != null && !userDto.getImgFile().isEmpty())
                userDto.setFileName(fileManager.createFile(FileDirectories.USER_IMG, userDto.getImgFile()));
            else
                userDto.setFileName(userOld.getImgFile());
            userOld.updateUserByDto(userDto);
            userRepo.save(userOld);
        } catch (ServerException ex) {
            response.setErrors(ex.getErrorMessage());
            logger.warn("User ({}) -> (updateUser) error -> {}.",userOld.getId(), ex.getErrorMessage());
        }catch (Exception ex){
            logger.error("User ({}) -> (updateUser) error -> {}.", userOld.getId(), ex.getMessage());
        }
        return response;
    }

    public DeleteUserDtoResp deleteUser(User user, String username) {
        var deleteResp = new DeleteUserDtoResp();
        try {
            validateDeleteUser(user, username);
            userRepo.delete(user);
        } catch (ServerException ex) {
            deleteResp.setError(ex.getErrorMessage());
            logger.warn("User ({}) -> (deleteUser) error -> {}.",user.getId(), ex.getErrorMessage());
        }catch (Exception ex){
            logger.error("User ({}) -> (deleteUser) error -> {}.",user.getId(), ex.getMessage());
        }
        return deleteResp;
    }

    public GetAllHelpsResp getAllAnswersSupportByUserDto(User user) {
        GetAllHelpsResp response = new GetAllHelpsResp();
        try {
            validateGetAllAnswersSupport(user);
            List<SupportAnswer> listSupp = userRepo.findSupportAnswerById(user.getId());
            response.setAnswers(listSupp);
        } catch (ServerException e) {
            response.setErrors(e.getMessage());
            logger.warn("User ({}) -> (getAllAnswersSupportByUserDto) error {}.",user.getId(), e.getErrorMessage());
        }catch (Exception ex){
            logger.error("User ({}) -> (getAllAnswersSupportByUserDto) error {}.",user.getId(), ex.getMessage());
        }
        return response;
    }

    public DeleteHelpResp deleteAnswerByUser(DeleteHelpDtoReq request) {
        DeleteHelpResp response = new DeleteHelpResp();
        try {
            validateDeleteAnswerSupport(request);
            User user = userRepo.findByUsername(request.getUser().getUsername());
            user.removeSupportAnswer(request.getId());
            userRepo.save(user);
        } catch (ServerException e) {
            response.setErrors(e.getErrorMessage());
            logger.warn("User ({}) -> (deleteAnswerByUser) error {}.",request.getUser().getId(), e.getErrorMessage());
        }
        catch (Exception ex){
            logger.error("User ({}) -> (deleteAnswerByUser) error {}.",request.getUser().getId(), ex.getMessage());
        }
        return response;
    }

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
            logger.warn("User ({}) -> (getAllFavoriteByUser) error {}.",user.getId(), e.getErrorMessage());
        }catch (Exception ex){
            logger.error("User ({}) -> (getAllFavoriteByUser) error {}.",user.getId(), ex.getMessage());
        }
        return response;
    }

    //---------------------------------------------------------------------------//
    private void validateGetAllFavoriteByUser(User user) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.FAVORITE_USER_ERROR);
        }
    }

    private void validateUpdateUser(User userOld, UpdateUserDtoReq userDto) throws ServerException {
        if (userOld == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (!userOld.getUsername().equals(userDto.getUsername())) {
            throw new ServerException(AnswerErrorCode.UPDATE_NOT_ROOT_UPDATE);
        }
        if (!userDto.getEmail().matches(EMAIL_PATTERN)) {
            throw new ServerException(AnswerErrorCode.UPDATE_WRONG_VALIDATE_EMAIL);
        }
        if (!Pattern.matches(PASSWORD_PATTERN, userDto.getPassword())) {
            throw new ServerException(AnswerErrorCode.UPDATE_WRONG_VALIDATE_PASSWORD);
        }
        if (userDto.getSex() == null || Objects.equals(userDto.getSex(), "")) {
            throw new ServerException(AnswerErrorCode.UPDATE_WRONG_VALIDATE_SEX);
        }
        if (userDto.getInfo() == null || userDto.getInfo().length() < 10) {
            throw new ServerException(AnswerErrorCode.UPDATE_WRONG_VALIDATE_INFO);
        }
        if (!userDto.getTelegramUsername().isEmpty() && !Pattern.matches(TELEGRAM_PATTERN, userDto.getTelegramUsername())) {
            throw new ServerException(AnswerErrorCode.UPDATE_WRONG_TELEGRAM_ADDRESS);
        }

        int age;
        try {
            age = Integer.parseInt(userDto.getAge());
        } catch (Exception ex) {
            throw new ServerException(AnswerErrorCode.UPDATE_WRONG_AGE_SYNTAX);
        }
        if (age > 110 || age < 3) {
            throw new ServerException(AnswerErrorCode.UPDATE_WRONG_AGE_RANGE);
        }
    }

    private void validateRegistrationUser(RegistrationUserDtoReq dto) throws ServerException {
        if (!Pattern.matches(USERNAME_PATTERN, dto.getUsername())) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_USERNAME);
        }

        if (userRepo.findByUsername(dto.getUsername()) != null) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_USERNAME_ALREADY_EXIST);
        }
        if (!dto.getEmail().matches(EMAIL_PATTERN)) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VALIDATE_EMAIL);
        }
        if (!Pattern.matches(PASSWORD_PATTERN, dto.getPassword())) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VALIDATE_PASSWORD);
        }
        if (dto.getSex() == null || Objects.equals(dto.getSex(), "")) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VALIDATE_SEX);
        }
        if (dto.getInfo() == null || dto.getInfo().length() < 10) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VALIDATE_INFO);
        }
        if (dto.getImg() == null) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VALIDATE_IMG);
        }

        int age;
        try {
            age = Integer.parseInt(dto.getAge());
        } catch (Exception ex) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_AGE_SYNTAX);
        }
        if (age > 110 || age < 3) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_AGE_RANGE);
        }
    }

    private void validateGetAllAnswersSupport(User user) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.ERROR_ANSWER_BY_USER);
        }
    }

    private void validateDeleteAnswerSupport(DeleteHelpDtoReq request) throws ServerException {
        if (request.getId() == 0) {
            throw new ServerException(AnswerErrorCode.ERROR_ANSWER_BY_USER);
        }
        if (request.getUser() == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
    }

    private void validateDeleteUser(User user, String username) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (!user.getUsername().equals(username)) {
            throw new ServerException(AnswerErrorCode.UPDATE_NOT_ROOT_UPDATE);
        }
    }
}
