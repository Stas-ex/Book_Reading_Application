package com.diplom.black_fox_ex.service;

import com.diplom.black_fox_ex.exeptions.AnswerErrorCode;
import com.diplom.black_fox_ex.exeptions.ServerException;
import com.diplom.black_fox_ex.model.History;
import com.diplom.black_fox_ex.model.SupportAnswer;
import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.repositories.HistoryRepo;
import com.diplom.black_fox_ex.repositories.UserRepo;
import com.diplom.black_fox_ex.request.*;
import com.diplom.black_fox_ex.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Path;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserService implements UserDetailsService {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserRepo userRepo;
    private final HistoryRepo historyRepo;

    @Autowired
    public UserService(UserRepo userRepo, HistoryRepo historyRepo) {
        this.userRepo = userRepo;
        this.historyRepo = historyRepo;
    }


    public ProfileViewHiDtoResponse getHistory(String username, long id) {
        ProfileViewHiDtoResponse response = new ProfileViewHiDtoResponse();
        try {
            validateGetHistory(username, id);
            List<History> list = userRepo.findAllByUsername(username);
            for (History history : list) {
                if (history.getId() == id) {
                    response.setHistoryDto(new HistoryDto(history));
                    return response;
                }
            }
            throw new ServerException(AnswerErrorCode.HISTORY_NOT_FOUND);
        } catch (ServerException ex) {
            response.setError(ex.getErrorMessage());
        }
        return response;
    }


    public UserRegDtoResponse registrationUser(UserDtoRegDtoRequest dto) {
        var dtoResponse = new UserRegDtoResponse();
        try {
            validateRegistrationUser(dto);

            String fileName = createFile(dto.getImg());
            dto.setFileName(fileName);

            User user = new User(
                    dto.getUsername(), dto.getEmail(), dto.getPassword(),
                    Integer.parseInt(dto.getAge()), dto.getSex(), dto.getInfo(),
                    dto.getFileName(), dto.isActive()
            );
            userRepo.save(user);

        } catch (ServerException ex) {
            dtoResponse.setErrors(ex.getErrorMessage());
        }
        return dtoResponse;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    public void deleteById(long id) {
        userRepo.deleteById(id);
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    public UserUpdateDtoResponse updateUser(UserDto userOld, UpdateDtoRequest userDto) {
        UserUpdateDtoResponse response = new UserUpdateDtoResponse();

        try {
            validateUpdateUser(userOld, userDto);
            userDto.setSex(userDto.getSex());
            String fileName;
            if ((fileName = createFile(userDto.getImgFile())).equals("")) {
                userDto.setFileName(userOld.getImgFile());
            } else {
                userDto.setFileName(fileName);
            }
            User user = userRepo.findByUsername(userDto.getUsername());
            user.updateUserByDto(userDto);
            userRepo.save(user);
        } catch (ServerException ex) {
            response.setErrors(ex.getErrorMessage());
        }
        return response;
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    public ProfileViewHiAllDtoResponse getAllHistoryByUser(String username) {
        ProfileViewHiAllDtoResponse response = new ProfileViewHiAllDtoResponse();
        try {
            validateUsername(username);
            List<History> listHistory = userRepo.findAllByUsername(username);

            List<HistoryDto> listDto = new ArrayList<>();
            listHistory.forEach(elem -> listDto.add(new HistoryDto(elem, uploadPath)));

            response.setHistoryDto(listDto);
            return response;
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
        }
        return response;
    }

    private String createFile(MultipartFile imgFile) throws ServerException {
        //Проверка и сохранение картинки
        if (Objects.equals(imgFile.getOriginalFilename(), "")) {
            return "user.jpg";
        }

        File uploadDir = new File(uploadPath);
        //If the file does not exist
        if (!uploadDir.exists()) {
            uploadDir.mkdir();//We will create it
        }
        String fileName = UUID.randomUUID().toString().substring(3, 7) + "." + imgFile.getOriginalFilename();

        try {
            imgFile.transferTo(new File(uploadPath + fileName));
        } catch (IOException e) {
            throw new ServerException(AnswerErrorCode.FILE_CREATE_ERROR);
        }

        return fileName;
    }

    public UserDto getUserByUsername(String name) {
        try {
            validateUsername(name);
            return new UserDto(userRepo.findByUsername(name));
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    public UserDto getUserByUsernameView(String name) {
        try {
            validateUsername(name);
            return new UserDto(userRepo.findByUsername(name), uploadPath);
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    public FavoriteAddResponse addFavoriteHistory(FavoriteAddRequest request) {
        FavoriteAddResponse response = new FavoriteAddResponse();
        try {
            validateAddFavoriteHistory(request);
            User user = userRepo.findByUsername(request.getUser().getUsername());
            History history = historyRepo.findById(request.getIdHistory()).orElseThrow();

            //If the user has already liked
            if (history.getUserLike().contains(user)) {
                history.getUserLike().remove(user);
                historyRepo.save(history);

                User userNew = userRepo.findByUsername(request.getUser().getUsername());
                userNew.removeFavoriteHistory(history);
                userRepo.save(userNew);
                return response;
            }


            history.getUserLike().add(user);
            historyRepo.save(history);

            User userNew = userRepo.getById(request.getUser().getId());
            userNew.addFavoriteHistory(history);
            userRepo.save(userNew);

        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(ex.getMessage());
        }
        return response;
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    public HelpGetAllAnswersResponse getAllAnswersSupportByUserDto(UserDto userDto) {
        HelpGetAllAnswersResponse response = new HelpGetAllAnswersResponse();
        try {
            validateGetAllAnswersSupport(userDto);
            List<SupportAnswer> listSupp = userRepo.findSupportAnswerById(userDto.getId());
            response.setAnswers(listSupp);
        } catch (ServerException e) {
            response.setErrors(e.getMessage());
        }
        return response;
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    public FavoriteDelResponse deleteFavoriteHistory(FavoriteDelRequest request) {
        FavoriteDelResponse response = new FavoriteDelResponse();
        try {
            validateFavoriteDelResponse(request);

            User user = userRepo.getById(request.getUser().getId());
            History history = historyRepo.findById(request.getHistoryId()).orElseThrow();

            history.getUserLike().remove(user);
            historyRepo.save(history);

            User userNew = userRepo.getById(request.getUser().getId());
            userNew.removeFavoriteHistory(history);
            userRepo.save(userNew);

        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
        }
        return response;
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    public HelpDeleteAnswerResponse deleteAnswerByUser(HelpDeleteAnswerRequest request) {
        HelpDeleteAnswerResponse response = new HelpDeleteAnswerResponse();
        try {
            validateDeleteAnswerSupport(request);
            User user = userRepo.findByUsername(request.getUserDto().getUsername());
            user.removeSupportAnswer(request.getId());
            userRepo.save(user);
        } catch (ServerException e) {
            response.setErrors(e.getErrorMessage());
        }
        return response;
    }

    /**
     * -----------------------------------------------------------------------------------
     **/

    public FavoriteGetAllUserResponse getAllFavoriteByUser(FavoriteGetAllUserRequest request) {
        FavoriteGetAllUserResponse response = new FavoriteGetAllUserResponse();
        try {
            validateFavoriteGetAllUserResponse(request);
            List<History> list = userRepo.findFavoriteHistoryById(request.getUserId());
            List<HistoryDto> listDto = new ArrayList<>();
            list.forEach(elem -> listDto.add(new HistoryDto(elem, uploadPath)));
            response.setListDto(listDto);
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
        }
        return response;
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    private void validateFavoriteGetAllUserResponse(FavoriteGetAllUserRequest request) throws ServerException {
        if (Long.valueOf(request.getUserId()) == null) {
            throw new ServerException(AnswerErrorCode.FAVORITE_USER_ERROR);
        }
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    private void validateUpdateUser(UserDto userOld, UpdateDtoRequest dto) throws ServerException {
        if (userOld == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (dto.getUsername().length() < 3) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_USERNAME);
        }

        if (!userOld.getUsername().equals(dto.getUsername()) && userRepo.findByUsername(dto.getUsername()) != null) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_USERNAME_ALREADY_EXIST);
        }
        if (!dto.getEmail().matches("([A-z0-9_.-]{1,})@([A-z0-9_.-]{1,}).([A-z]{2,8})")) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_EMAIL);
        }
        if (dto.getPassword() == null) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PASSWORD);
        }
        if (dto.getImgFile() == null) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_IMG);
        }
        if (dto.getSex() == null || Objects.equals(dto.getSex(), "")) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_SEX);
        }
        if (dto.getInfo() == null || dto.getInfo().length() < 10) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_INFO);
        }
        if (!dto.getTelegramUsername().equals("") && !dto.getTelegramUsername().matches("@([A-z0-9_.-]{2,})")) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_TELEGRAM_BOT);
        }
        if (dto.getRoles() == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_ROLE);
        }
        int age;
        try {
            age = Integer.parseInt(dto.getAge());
        } catch (Exception ex) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_AGE);
        }
        if (age > 110 || age < 1) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_AGE);
        }
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    private void validateRegistrationUser(UserDtoRegDtoRequest dto) throws ServerException {
        if (Pattern.matches("^[A-zА-я]{3,20}",dto.getUsername())) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_USERNAME);
        }
        if (userRepo.findByUsername(dto.getUsername()) != null) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_USERNAME_ALREADY_EXIST);
        }
        if (!dto.getEmail().matches("([A-z0-9_.-]{1,})@([A-z0-9_.-]{1,}).([A-z]{2,8})")) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_EMAIL);
        }
        if (Pattern.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,15})", dto.getPassword())) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PASSWORD);
        }
        if (dto.getSex() == null || Objects.equals(dto.getSex(), "")) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_SEX);
        }
        if (dto.getInfo() == null || dto.getInfo().length() < 10) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_INFO);
        }
        if (dto.getImg() == null) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_IMG);
        }

        int age;
        try {
            age = Integer.parseInt(dto.getAge());
        } catch (Exception ex) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_AGE);
        }
        if (age > 110 || age < 3) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_AGE);
        }
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    private void validateUsername(String username) throws ServerException {
        if (username == null || userRepo.findByUsername(username) == null) {
            throw new ServerException(AnswerErrorCode.HISTORY_USER_NOT_FOUND);
        }
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    private void validateGetHistory(String username, long id) throws ServerException {
        if (username == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (Long.valueOf(id) == null) {
            throw new ServerException(AnswerErrorCode.HISTORY_USER_NOT_FOUND);
        }
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    private void validateAddFavoriteHistory(FavoriteAddRequest request) throws ServerException {
        if (Long.valueOf(request.getIdHistory()) == null) {
            throw new ServerException(AnswerErrorCode.FAVORITE_HISTORY_ID_ERROR);
        }

        if (request.getUser() == null) {
            throw new ServerException(AnswerErrorCode.FAVORITE_USER_ERROR);
        }
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    private void validateFavoriteDelResponse(FavoriteDelRequest request) throws ServerException {
        if (Long.valueOf(request.getHistoryId()) == null) {
            throw new ServerException(AnswerErrorCode.FAVORITE_HISTORY_ID_ERROR);
        }

        if (request.getUser() == null) {
            throw new ServerException(AnswerErrorCode.FAVORITE_USER_ERROR);
        }
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    private void validateGetAllAnswersSupport(UserDto userDto) throws ServerException {
        if (userDto == null) {
            throw new ServerException(AnswerErrorCode.ERROR_ANSWER_BY_USER);
        }
    }

    /**
     * -----------------------------------------------------------------------------------
     **/
    private void validateDeleteAnswerSupport(HelpDeleteAnswerRequest request) throws ServerException {
        if (Long.valueOf(request.getId()) == null) {
            throw new ServerException(AnswerErrorCode.ERROR_ANSWER_BY_USER);
        }
        if (request.getUserDto() == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
    }
}
