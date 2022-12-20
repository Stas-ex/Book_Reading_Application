package com.diploma.black_fox_ex.service;

import com.diploma.black_fox_ex.dto.DeleteHelpDtoReq;
import com.diploma.black_fox_ex.dto.user.UserDto;
import com.diploma.black_fox_ex.dto.user.UserMenuDto;
import com.diploma.black_fox_ex.dto.user.UserProfileDto;
import com.diploma.black_fox_ex.exeptions.AnswerErrorCode;
import com.diploma.black_fox_ex.exeptions.ServerException;
import com.diploma.black_fox_ex.io.ImgManager;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.repositories.UserRepo;
import com.diploma.black_fox_ex.response.DeleteHelpResp;
import com.diploma.black_fox_ex.response.GetAllHelpsResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.diploma.black_fox_ex.io.ImgDirectories.USER_IMG_DIR;
import static com.diploma.black_fox_ex.mappers.EntityMapper.*;
import static org.apache.logging.log4j.util.Strings.isBlank;

/**
 * This class performs basic actions on received user interaction requests.
 */
@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo,
                       PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = encoder;
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
        return userRepo.findByEmail(username)
                .orElse(
                        userRepo.findByUsername(username)
                                .orElse(null)
                );
    }

    /**
     * Function to create the transfer of the required user data in the menu
     *
     * @param user includes all fields of an authorized user
     * @return dto for use in the bottom corner of the menu
     */
    public UserMenuDto getUserMenu(User user) {
        return user == null ? null : toUserMenuDto(user);
    }

    /**
     * function to create the transfer of the required user data in the profile
     *
     * @param user includes all fields of an authorized user
     * @return dto to display the user on the main profile pageNum
     */
    public UserProfileDto getUserProfile(User user) {
        if (user == null) {
            return null;
        }

        var userDto = toUserProfileDto(user);
        userDto.setFilename(USER_IMG_DIR.getPath() + user.getImg());
        return userDto;
    }

    /**
     * User registration method
     *
     * @param userDto contains the main parameters for registration user
     */
    public void register(UserDto userDto) {
        userDto.setFilename(ImgManager.save(USER_IMG_DIR, userDto.getImg()));
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepo.save(toUser(userDto));
    }

    /**
     * User update method
     *
     * @param user    includes all fields of an authorized user
     * @param userDto contains the main parameters for update user
     */
    public void update(User user, UserProfileDto userDto) {

        //If the user update a password
        String password = userDto.getPassword();

        userDto.setPassword(isBlank(password) ? user.getPassword() : passwordEncoder.encode(password));

        //If the user adds a file
        String filename = ImgManager.save(USER_IMG_DIR, userDto.getImg());
        userDto.setFilename(ImgManager.isDefaultImg(filename) ? filename : user.getImg());

        userRepo.save(updateUser(user, userDto));
    }


    public void delete(User user) {
        if (user != null) {
            user.setDateTimeDeletion(LocalDateTime.now());
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
    public GetAllHelpsResp getAllAnswersSupport(User user) {
        GetAllHelpsResp response = new GetAllHelpsResp();
        try {
            validateGetAllAnswersSupport(user);
//            List<Support> listSupp = userRepo.fin(user.getId());
//            response.setAnswers(listSupp);
        } catch (ServerException e) {
            response.setErrors(e.getErrorMessage());
            log.warn("User ({}) -> (getAllAnswersSupport) error {}.", user != null ? user.getId() : "null", e.getErrorMessage());
        } catch (Exception ex) {
            log.error("User ({}) -> (getAllAnswersSupport) error {}.", user != null ? user.getId() : "null", ex.getMessage());
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
    public DeleteHelpResp deleteAnswerSupport(DeleteHelpDtoReq request) {
        DeleteHelpResp response = new DeleteHelpResp();
        try {
            validateDeleteAnswerSupport(request);
            User user = userRepo.findByUsername(request.getUser().getUsername()).orElseThrow();
            user.removeSupportAnswer(request.getId());
            userRepo.save(user);
        } catch (ServerException e) {
            response.setErrors(e.getErrorMessage());
            log.warn("User ({}) -> (deleteAnswer) error {}.", request.getUser() != null ? request.getUser().getId() : "null", e.getErrorMessage());
        } catch (Exception ex) {
            log.error("User ({}) -> (deleteAnswer) error {}.", request.getUser() != null ? request.getUser().getId() : "null", ex.getMessage());
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
        return userRepo.findByUsername(username).isPresent();
    }
}
