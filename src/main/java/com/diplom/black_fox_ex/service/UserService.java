package com.diplom.black_fox_ex.service;

import com.diplom.black_fox_ex.exeptions.AnswerErrorCode;
import com.diplom.black_fox_ex.exeptions.ServerException;
import com.diplom.black_fox_ex.repositories.UserRepo;
import com.diplom.black_fox_ex.model.SupportAnswer;
import com.diplom.black_fox_ex.io.FileDirectories;
import com.diplom.black_fox_ex.io.FileManager;
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

/**
 * This class performs basic actions on received user interaction requests.
 */
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

    /**
     *The function allows you to get a user by username,
     *  used for authorization in Spring Security
     * @param username user parameter
     * @return the user
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    /**
     * Function to create the transfer of the required user data in the menu
     * @param user includes all fields of an authorized user
     * @return dto for use in the bottom corner of the menu
     */
    public GetUserMenuDtoResp getUserMenu(User user) {
        if (user == null) return null;
        return new GetUserMenuDtoResp(user);
    }

    /**
     * function to create the transfer of the required user data in the profile
     * @param user includes all fields of an authorized user
     * @return dto to display the user on the main profile page
     */
    public GetUserProfileDtoResp getUserProfile(User user) {
        if (user == null) return null;
        return new GetUserProfileDtoResp(user);
    }

    /**
     * User registration method
     * @param dto contains the main parameters for registration user
     * @see #validateRegistrationUser(RegistrationUserDtoReq)
     * @return dto response , with an error field
     */
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

    /**
     * User update method
     * @param userOld includes all fields of an authorized user
     * @param userDto contains the main parameters for update user
     * @see #validateUpdateUser(User, UpdateUserDtoReq)
     * @return dto response, with an error field
     */
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

    /**
     * User delete method
     * @param user includes all fields of an authorized user
     * @param username parameter from the deleted profile
     * @see #validateDeleteUser(User, String)
     * @return dto response, with an error field
     */
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

    /**
     * Method for getting all answers to questions from the support team
     * @param user includes all fields of an authorized user
     * @see #validateGetAllAnswersSupport(User)
     * @return dto response containing a list of messages from the support service
     */
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

    /**
     * Method for deleting a message from the helpdesk
     * @param request contains the main parameters for delete answer
     * @see #validateDeleteAnswerSupport(DeleteHelpDtoReq)
     * @return dto response, with an error field
     */
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
