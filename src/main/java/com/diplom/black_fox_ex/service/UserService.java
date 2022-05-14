package com.diplom.black_fox_ex.service;

import com.diplom.black_fox_ex.exeptions.AnswerErrorCode;
import com.diplom.black_fox_ex.exeptions.ServerException;
import com.diplom.black_fox_ex.io.FileDirectories;
import com.diplom.black_fox_ex.io.FileManager;
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

import java.util.*;
import java.util.regex.Pattern;


@Service
public class UserService implements UserDetailsService {
    private final String USERNAME_PATTERN = "^[A-z]{3,20}";
    private final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,15})";
    private final String EMAIL_PATTERN = "([A-z0-9_.-]{1,})@([A-z0-9_.-]{1,}).([A-z]{2,8})";
    private final String TELEGRAM_PATTERN = ".*\\B@(?=\\w{5,32}\\b)[a-zA-Z0-9]+(?:_[a-zA-Z0-9]+)*.*";


    @Value("${upload.path}")
    private String uploadPath;
    private final UserRepo userRepo;
    private final HistoryRepo historyRepo;
    private final FileManager fileManager = new FileManager();

    @Autowired
    public UserService(UserRepo userRepo, HistoryRepo historyRepo) {
        this.userRepo = userRepo;
        this.historyRepo = historyRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }


    public UserMenuDto getUserMenu(User user) {
        if (user == null) return null;
        return new UserMenuDto(user);
    }

    public UserProfileDto getUserProfile(User user) {
        if (user == null) return null;
        return new UserProfileDto(user);
    }

    public UserRegDtoResponse registrationUser(UserDtoRegDtoRequest dto) {
        var dtoResponse = new UserRegDtoResponse();
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
        }
        return dtoResponse;
    }

    public UserUpdateDtoResponse updateUser(User userOld, UpdateDtoRequest userDto) {
        var response = new UserUpdateDtoResponse();
        try {
            validateUpdateUser(userOld, userDto);
            if (userDto.getImgFile() != null && !userDto.getImgFile().isEmpty())
                userDto.setFileName(fileManager.createFile(FileDirectories.USER_IMG,userDto.getImgFile()));
            else
                userDto.setFileName(userOld.getImgFile());
            userOld.updateUserByDto(userDto);
            userRepo.save(userOld);
        } catch (ServerException ex) {
            response.setErrors(ex.getErrorMessage());
        }
        return response;
    }

    public DeleteUserDtoResp deleteUser(User user, String username) {
        var deleteResp = new DeleteUserDtoResp();
        try {
            validateDeleteUser(user, username);
            userRepo.delete(user);
        }catch (ServerException ex) {
            deleteResp.setError(ex.getErrorMessage());
        }
        return deleteResp;
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

    public ProfileViewHiDtoResponse getHistory(String username, long id) {
        var response = new ProfileViewHiDtoResponse();
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

//    private String createFile(MultipartFile imgFile) throws ServerException {
//        //Проверка и сохранение картинки
//        if (Objects.equals(imgFile.getOriginalFilename(), "")) {
//            return "user.jpg";
//        }
//
//        File uploadDir = new File(uploadPath);
//        //If the file does not exist
//        if (!uploadDir.exists()) {
//            uploadDir.mkdir();//We will create it
//        }
//        String fileName = UUID.randomUUID().toString().substring(3, 7) + "." + imgFile.getOriginalFilename();
//
//        try {
//            imgFile.transferTo(new File(uploadPath + fileName));
//        } catch (IOException e) {
//            throw new ServerException(AnswerErrorCode.FILE_CREATE_ERROR);
//        }
//
//        return fileName;
//    }

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
    private void validateUpdateUser(User userOld, UpdateDtoRequest userDto) throws ServerException {
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
        if(userDto.getTelegramUsername() != null && !Pattern.matches(TELEGRAM_PATTERN, userDto.getTelegramUsername())){
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

    /**
     * -----------------------------------------------------------------------------------
     **/
    private void validateRegistrationUser(UserDtoRegDtoRequest dto) throws ServerException {
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

    private void validateDeleteUser(User user, String username) throws ServerException {
        if(user == null){
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if(!user.getUsername().equals(username)){
            throw new ServerException(AnswerErrorCode.UPDATE_NOT_ROOT_UPDATE);
        }
    }
}
