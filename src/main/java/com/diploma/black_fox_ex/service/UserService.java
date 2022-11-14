package com.diploma.black_fox_ex.service;

import com.diploma.black_fox_ex.dto.DeleteHelpDtoReq;
import com.diploma.black_fox_ex.dto.user.UserDTO;
import com.diploma.black_fox_ex.exeptions.AnswerErrorCode;
import com.diploma.black_fox_ex.exeptions.ServerException;
import com.diploma.black_fox_ex.io.FileDirectories;
import com.diploma.black_fox_ex.io.FileManager;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.repositories.UserRepo;
import com.diploma.black_fox_ex.response.DeleteHelpResp;
import com.diploma.black_fox_ex.response.GetAllHelpsResp;
import com.diploma.black_fox_ex.dto.user.UserMenuDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * This class performs basic actions on received user interaction requests.
 */
@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final String DEFAULT_PASSWORD = "Password123";
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final FileManager fileManager = new FileManager();

    @Autowired
    public UserService(UserRepo userRepo,
                       PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * The function allows you to get a user by username,
     * used for authorization in Spring Security
     *
     * @param username user parameter
     * @return the user
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails;
        if ((userDetails = userRepo.findByUsername(username)) == null) {
            userDetails = userRepo.findByEmail(username);
        }
        return userDetails;
    }

    /**
     * Function to create the transfer of the required user data in the menu
     *
     * @param user includes all fields of an authorized user
     * @return dto for use in the bottom corner of the menu
     */
    public UserMenuDTO getUserMenu(User user) {
        if (user == null) return null;
        return new UserMenuDTO(user);
    }

    /**
     * function to create the transfer of the required user data in the profile
     *
     * @param user includes all fields of an authorized user
     * @return dto to display the user on the main profile pageNum
     */
    public UserDTO getUserProfile(User user) {
        if (user == null) return null;
        var userDto = new UserDTO(user.getUsername(), user.getEmail(),
                DEFAULT_PASSWORD, user.getAge(), user.getSex().getTitle(),
                user.getInfo(), user.getTelegram(), null);
        userDto.setFilename(FileDirectories.USER_IMG_DIR.getPath() + user.getImgFile());
        return userDto;
    }

    /**
     * User registration method
     *
     * @param dto contains the main parameters for registration user
     */
    public void registrationUser(UserDTO dto) {
        dto.setFilename(fileManager.createFile(FileDirectories.USER_IMG_DIR, dto.getImg()));
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        var user = new User(dto);
        userRepo.save(user);
    }

    /**
     * User update method
     *
     * @param user    includes all fields of an authorized user
     * @param userDto contains the main parameters for update user
     */
    public void updateUser(User user, UserDTO userDto) {

        //If the user update a password
        String password = userDto.getPassword();
        userDto.setPassword(password.equals(DEFAULT_PASSWORD) ? user.getPassword() : passwordEncoder.encode(password));

        //If the user adds a file
        String filePath = fileManager.createFile(FileDirectories.USER_IMG_DIR, userDto.getImg());
        userDto.setFilename(filePath.equals("user.jpeg") ? user.getImgFile() : filePath);

        user.update(userDto);
        userRepo.save(user);
    }


    public void deleteUser(User user) {
        if (user != null) {
            user.setDateTimeDelete(LocalDateTime.now());
            user.setActive(false);
            userRepo.save(user);
        }
    }

    /**
     * Method for getting all answers to question from the support team
     *
     * @param user includes all fields of an authorized user
     * @return dto response containing a list of messages from the support service
     * @see #validateGetAllAnswersSupport(User)
     */
    public GetAllHelpsResp getAllAnswersSupportByUserDto(User user) {
        GetAllHelpsResp response = new GetAllHelpsResp();
        try {
            validateGetAllAnswersSupport(user);
//            List<Support> listSupp = userRepo.fin(user.getId());
//            response.setAnswers(listSupp);
        } catch (ServerException e) {
            response.setErrors(e.getErrorMessage());
            logger.warn("User ({}) -> (getAllAnswersSupportByUserDto) error {}.", user != null ? user.getId() : "null", e.getErrorMessage());
        } catch (Exception ex) {
            logger.error("User ({}) -> (getAllAnswersSupportByUserDto) error {}.", user != null ? user.getId() : "null", ex.getMessage());
        }
        return response;
    }

    /**
     * Method for deleting a message from the helpdesk
     *
     * @param request contains the main parameters for delete answer
     * @return dto response, with an error field
     * @see #validateDeleteAnswerSupport(DeleteHelpDtoReq)
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
            logger.warn("User ({}) -> (deleteAnswerByUser) error {}.", request.getUser() != null ? request.getUser().getId() : "null", e.getErrorMessage());
        } catch (Exception ex) {
            logger.error("User ({}) -> (deleteAnswerByUser) error {}.", request.getUser() != null ? request.getUser().getId() : "null", ex.getMessage());
        }
        return response;
    }

    private void validateGetAllAnswersSupport(User user) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.ERROR_ANSWER_BY_USER);
        }
    }

    private void validateDeleteAnswerSupport(DeleteHelpDtoReq request) throws ServerException {
        if (request.getId() <= 0) {
            throw new ServerException(AnswerErrorCode.ERROR_ANSWER_BY_USER);
        }
        if (request.getUser() == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
    }

    public boolean isExistUser(String username) {
        return userRepo.findByUsername(username) != null;
    }
}
