package com.diploma.black_fox_ex.service;

import com.diploma.black_fox_ex.model.Book;
import com.diploma.black_fox_ex.model.constant.Genre;
import com.diploma.black_fox_ex.repositories.BookRepo;
import com.diploma.black_fox_ex.repositories.CommentsRepo;
import com.diploma.black_fox_ex.exeptions.AnswerErrorCode;
import com.diploma.black_fox_ex.exeptions.ServerException;
import com.diploma.black_fox_ex.repositories.UserRepo;
import com.diploma.black_fox_ex.io.FileDirectories;
import com.diploma.black_fox_ex.io.FileManager;
import com.diploma.black_fox_ex.model.Comment;
import com.diploma.black_fox_ex.model.User;

import com.diploma.black_fox_ex.dto.*;
import com.diploma.black_fox_ex.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This class performs basic actions on received book interaction requests.
 */
@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final FileManager fileManager = new FileManager();
    private final CommentsRepo commentsRepo;
    private final BookRepo bookRepo;
    private final UserRepo userRepo;


    @Autowired
    public BookService(BookRepo bookRepo, UserRepo userRepo,
                       CommentsRepo commentsRepo) {
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
        this.commentsRepo = commentsRepo;
    }

    /**
     * Book creation method
     *
     * @param user    includes all fields of an authorized user
     * @param bookDto contains the main parameters for create book
     * @return dto response , with an error field
     * @see #validateCreateBook(User, CreateBookDtoReq)
     */
    public CreateBookDtoResp createBook(User user, CreateBookDtoReq bookDto) {
        var response = new CreateBookDtoResp();
        try {
            validateCreateBook(user, bookDto);

            Genre genre = Genre.valueOf(bookDto.getGenre());

            if (genre == null)
                throw new ServerException(AnswerErrorCode.BOOK_TAG_ERROR);

            String fileName = fileManager.createFile(FileDirectories.BOOK_IMG, bookDto.getImgFile());

            Book book = new Book(bookDto.getTitle(),
                    fileName, bookDto.getBigText(), genre);

            bookRepo.save(book);

            List<Book> books = userRepo.findAllById(user.getId());
            books.add(book);
            user.setBooks(books);
            userRepo.save(user);

        } catch (ServerException ex) {
            response.setError(ex.getErrorMessage());
            logger.warn("User ({}) -> (createBook) error {}.", user != null ? user.getId() : "null", ex.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}) -> (createBook) error {}.", user != null ? user.getId() : "null", ex.getMessage());
        }
        return response;
    }

    /**
     * Book update method
     *
     * @param user     includes all fields of an authorized user
     * @param booksDto contains the main parameters for update bookes
     * @return dto response , with an error field
     * @see #validateUpdateBook(User, UpdateBooksDtoReq)
     */
    public UpdateBookDtoResp updateBook(User user, UpdateBooksDtoReq booksDto) {
        var response = new UpdateBookDtoResp();
        try {
            validateUpdateBook(user, booksDto);
            Genre genre = Genre.valueOf(booksDto.getGenre());
            if (genre == null)
                throw new ServerException(AnswerErrorCode.BOOK_TAG_ERROR);

            Book book = userRepo.findBookById(user.getId(), booksDto.getId());

            String fileName;
            if (Objects.requireNonNull(booksDto.getImgFile().getOriginalFilename()).isEmpty()) {
                fileName = book.getBackgroundImg();
            } else {
                fileName = fileManager.createFile(FileDirectories.BOOK_IMG, booksDto.getImgFile());
            }

            Book bookUpdate = new Book(book.getId(), booksDto.getTitle(), fileName, booksDto.getBigText(), genre);
            bookRepo.save(bookUpdate);
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}) -> (updateBook) error {}.", user != null ? user.getId() : "null", e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}) -> (updateBook) error {}.", user != null ? user.getId() : "null", ex.getMessage());
        }
        return response;

    }

    /**
     * Book delete method
     *
     * @param request contains the main parameters for delete book
     * @return dto response , with an error field
     * @see #validateDeleteBook(DeleteBookDtoReq) ()
     */
    public DeleteBookDtoResp deleteBook(DeleteBookDtoReq request) {
        var response = new DeleteBookDtoResp();
        try {
            validateDeleteBook(request);
            User user = request.getUser();
            Book book = userRepo.findBookById(user.getId(), request.getId());

            List<Book> books = userRepo.findAllById(user.getId());
            books.remove(book);
            user.setBooks(books);

            userRepo.save(user);
            bookRepo.delete(book);
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}) -> (deleteBook) error {}.", request.getUser() != null ? request.getUser().getId() : "null", e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}) -> (deleteBook) error {}.", request.getUser() != null ? request.getUser().getId() : "null", ex.getMessage());
        }
        return response;
    }

    /**
     * Method returns all books by genre
     *
     * @param nameGenre includes genre name
     * @param numPage   includes number page
     * @return dto response , with an error field
     * @see #validateGetAllBookByGenre(String, int)
     */
    public GetAllBookResp getAllBookByGenre(String nameGenre, int numPage) {
        var response = new GetAllBookResp();
        try {
            validateGetAllBookByGenre(nameGenre, numPage);
            List<Book> listBook;

            if (nameGenre.equals("all"))
                listBook = bookRepo.findAll();
            else
                listBook = bookRepo.findByGenre(Genre.valueOf(nameGenre));

            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = 0; i < (listBook.size() / 21) + 1; i++) {
                pageNumbers.add(i);
            }
            response.setPageNumbers(pageNumbers);

            if (listBook.size() == 0)
                throw new ServerException(AnswerErrorCode.PAGE_IS_EMPTY);
            else if (listBook.size() > (numPage + 1) * 21)
                response.setList(listBook.subList(numPage * 21, (numPage + 1) * 21));
            else {
                response.setList(listBook.subList(numPage * 21, listBook.size()));
            }
        } catch (ServerException ex) {
            response.setError(ex.getErrorMessage());
            logger.warn("(getAllBookByGenre) error -> {}.", ex.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("(getAllBookByGenre) error -> {}.", ex.getMessage());
        }
        return response;
    }

    /**
     * Method gets book by id
     *
     * @param user includes all fields of an authorized user
     * @param id   -> book parameter
     * @return dto response , with an error field
     * @see #validateGetBookById(long)
     */
    public GetBookLookPageDtoResp getBookById(User user, long id) {
        var response = new GetBookLookPageDtoResp();
        try {
            validateGetBookById(id);
            Book book = bookRepo.findById(id).orElseThrow();

            List<GetCommentsDtoResp> commentsDto = new ArrayList<>();
            book.getComments().forEach(comment -> commentsDto.add(new GetCommentsDtoResp(comment)));
            response.setBookDto(new GetBookLookDtoResp(book, commentsDto));

            //If the like was clicked
            if (user != null) {
                List<Book> list = userRepo.findFavoriteBookById(user.getId());
                if (list.contains(book)) {
                    response.setLikeActive("");
                }
            }
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}) , Book ({}) -> (getBookById) error {}.", user != null ? user.getId() : "null", id, e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}), Book ({}) -> (getBookById) error {}.", user != null ? user.getId() : "null", id, ex.getMessage());
        }
        return response;
    }

    /**
     * Method returns a list of saved books from an authorized user
     *
     * @param user includes all fields of an authorized user
     * @return response containing a list of the user's favorite books
     * @see #validateGetAllFavoriteByUser(User)
     */
    public GetAllFavoriteHiResp getAllFavoriteByUser(User user) {
        GetAllFavoriteHiResp response = new GetAllFavoriteHiResp();
        try {
            validateGetAllFavoriteByUser(user);
            List<GetBookCardDtoResp> listBookDto = new ArrayList<>();
            List<Book> listBook = userRepo.findFavoriteBookById(user.getId());
            listBook.forEach(book -> listBookDto.add(new GetBookCardDtoResp(book)));
            response.setListDto(listBookDto);
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
     * Method create comment for a book
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
            Book book = bookRepo.findById(request.getId()).orElseThrow();
            book.addComments(comment);

            commentsRepo.save(comment);
            bookRepo.save(book);
            userRepo.save(user);
        } catch (ServerException e) {
            responseError.setError(e.getErrorMessage());
            logger.warn("User ({}), Book ({}) -> (addComment) error {}.", user != null ? user.getId() : "null", request.getId(), e.getErrorMessage());
        } catch (Exception ex) {
            responseError.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}), Book ({})  -> (addComment) error {}.", user != null ? user.getId() : "null", request.getId(), ex.getMessage());
        }
        return responseError;
    }

    /**
     * Method returns a list of all books from an authorized user
     *
     * @param user includes all fields of an authorized user
     * @return list of all books for a given users
     * @see #validateUser(User)
     */
    public GetProfileViewBooksAllDtoResp getAllBookByUser(User user) {
        GetProfileViewBooksAllDtoResp response = new GetProfileViewBooksAllDtoResp();
        try {
            validateUser(user);
            List<GetBookCardDtoResp> listDto = new ArrayList<>();
            List<Book> listBook = userRepo.findAllById(user.getId());
            listBook.forEach(book -> listDto.add(new GetBookCardDtoResp(book)));
            response.setBooksDto(listDto);
            return response;
        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}) -> (getAllBookByUser) error {}.", user != null ? user.getId() : "null", e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}) -> (getAllBookByUser) error {}.", user != null ? user.getId() : "null", ex.getMessage());
        }
        return response;
    }

    /**
     * Method returns product by id
     *
     * @param user includes all fields of an authorized user
     * @param id   -> book parameter
     * @return bookDto, with an error field
     * @see #validateGetBook(User, long)
     */
    public GetProfileViewHiDtoResp getBookEditById(User user, long id) {
        var response = new GetProfileViewHiDtoResp();
        try {
            validateGetBook(user, id);
            List<Book> list = userRepo.findAllById(user.getId());
            List<Book> bookList = list.stream().filter(h -> h.getId() == id).toList();
            if (bookList.size() == 0)
                throw new ServerException(AnswerErrorCode.BOOK_NOT_FOUND);
            response.setBookDto(new GetBookEditDtoResp(bookList.get(0)));
        } catch (ServerException ex) {
            response.setError(ex.getErrorMessage());
            logger.warn("User ({}), Book ({}) -> (getBook) error {}.", user != null ? user.getId() : "null", id, ex.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}), Book ({}) -> (getBook) error {}.", user != null ? user.getId() : "null", id, ex.getMessage());
        }
        return response;
    }

    /**
     * The method implements adding book to the saved tab for this user
     *
     * @param request contains the main parameters for add favorite book
     * @return dto response, with an error field
     * @see #validateAddFavoriteBook(AddFavoriteBookReq)
     */
    public AddFavoriteResp addFavoriteBook(AddFavoriteBookReq request) {
        var response = new AddFavoriteResp();
        try {
            validateAddFavoriteBook(request);
            User user = request.getUser();
            Book book = bookRepo.findById(request.getBookId()).orElseThrow();
            List<Book> listAllFavorite = userRepo.findFavoriteBookById(user.getId());

            //If the user has already liked
            if (book.getLikes().contains(user)) {
                book.getLikes().remove(user);
                listAllFavorite.remove(book);
            } else {
                book.getLikes().add(user);
                listAllFavorite.add(book);
            }

            user.setFavorite(listAllFavorite);
            bookRepo.save(book);
            userRepo.save(user);

        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}), Book ({}) -> (addFavoriteBook) error {}.", request.getUser() != null ? request.getUser().getId() : "null", request.getBookId(), e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}), Book ({}) -> (addFavoriteBook) error {}.", request.getUser() != null ? request.getUser().getId() : "null", request.getBookId(), ex.getMessage());
        }
        return response;
    }

    /**
     * The method implements deleting book from the saved books tab for this user
     *
     * @param request contains the main parameters for delete favorite book
     * @return dto response, with an error field
     * @see #validateAddFavoriteBook(AddFavoriteBookReq)
     */
    public DeleteFavoriteHiResp deleteFavoriteBook(DeleteFavoriteBookDtoReq request) {
        var response = new DeleteFavoriteHiResp();
        try {
            validateDeleteFavoriteBook(request);

            User user = request.getUser();
            Book book = bookRepo.findById(request.getBookId()).orElseThrow();
            List<Book> listAllFavorite = userRepo.findFavoriteBookById(user.getId());

            listAllFavorite.remove(book);
            book.getLikes().remove(user);
            user.setFavorite(listAllFavorite);

            bookRepo.save(book);
            userRepo.save(user);

        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            logger.warn("User ({}), Book ({}) -> (deleteFavoriteBook) error {}.", request.getUser() != null ? request.getUser().getId() : "null", request.getBookId(), e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            logger.error("User ({}), Book ({}) -> (deleteFavoriteBook) error {}.", request.getUser() != null ? request.getUser().getId() : "null", request.getBookId(), ex.getMessage());
        }
        return response;
    }

    /**
     * The method returns a list of all genres
     *
     * @return list genres
     */
    public List<Genre> getAllGenre() {
        return Arrays.stream(Genre.values()).toList();
    }


    private void validateGetBookById(long id) throws ServerException {
        if (id <= 0) {
            throw new ServerException(AnswerErrorCode.BOOK_ID_NOT_EXIST);
        }
    }

    private void validateAddComment(User user, AddCommentDtoReq request) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (request.getId() <= 0) {
            throw new ServerException(AnswerErrorCode.BOOK_ID_NOT_EXIST);
        }
        if (request.getColor() == null || request.getColor().isEmpty()) {
            throw new ServerException(AnswerErrorCode.COMMENT_COLOR_ERROR);
        }
        if (request.getBigText() == null || request.getBigText().length() < 10) {
            throw new ServerException(AnswerErrorCode.COMMENT_BIG_TEXT_ERROR);
        }
    }

    private void validateGetAllBookByGenre(String nameGenre, int numPage) throws ServerException {
        if (nameGenre == null || nameGenre.isEmpty()) {
            throw new ServerException(AnswerErrorCode.BOOK_TAG_ERROR);
        }
        if (numPage < 0) {
            throw new ServerException(AnswerErrorCode.BOOK_PAGE_ERROR);
        }
    }

    private void validateCreateBook(User user, CreateBookDtoReq dto) throws ServerException {
        if (user == null || user.getUsername() == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (dto.getTitle() == null || dto.getTitle().length() < 3) {
            throw new ServerException(AnswerErrorCode.BOOK_TITLE_ERROR);
        }
        if (!bookRepo.findAllByTitle(dto.getTitle()).isEmpty()) {
            throw new ServerException(AnswerErrorCode.BOOK_TITLE_ALREADY_EXIST);
        }
        if (dto.getBigText() == null || dto.getBigText().length() < 20) {
            throw new ServerException(AnswerErrorCode.BOOK_SHORT_TEXT);
        }
        if (dto.getImgFile() == null) {
            throw new ServerException(AnswerErrorCode.BOOK_IMG_ERROR);
        }
        if (dto.getGenre() == null || dto.getGenre().isEmpty()) {
            throw new ServerException(AnswerErrorCode.BOOK_TAG_ERROR);
        }
    }

    private void validateUpdateBook(User user, UpdateBooksDtoReq dto) throws ServerException {
        if (dto == null) {
            throw new ServerException(AnswerErrorCode.REQUEST_IS_NULL);
        }
        if (dto.getId() < 1) {
            throw new ServerException(AnswerErrorCode.BOOK_ID_NOT_EXIST);
        }
        if (user == null || user.getUsername() == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }

        if (dto.getImgFile() == null) {
            throw new ServerException(AnswerErrorCode.BOOK_IMG_ERROR);
        }

        List<Book> bookByTitle = bookRepo.findAllByTitle(dto.getTitle());
        Optional<Book> bookUpdate = bookRepo.findById(dto.getId());

        if (bookUpdate.isEmpty())
            throw new ServerException(AnswerErrorCode.BOOK_ID_NOT_EXIST);

        if (!bookByTitle.isEmpty() && bookUpdate.get().getId() != bookByTitle.get(0).getId()) {
            throw new ServerException(AnswerErrorCode.BOOK_TITLE_ALREADY_EXIST);
        }
        if (dto.getTitle() == null || dto.getTitle().length() < 3) {
            throw new ServerException(AnswerErrorCode.BOOK_TITLE_ERROR);
        }
        if (dto.getGenre() == null || dto.getGenre().isEmpty()) {
            throw new ServerException(AnswerErrorCode.BOOK_TAG_ERROR);
        }
        if (dto.getBigText() == null || dto.getBigText().length() < 20) {
            throw new ServerException(AnswerErrorCode.BOOK_SHORT_TEXT);
        }
    }

    private void validateDeleteBook(DeleteBookDtoReq request) throws ServerException {
        if (request.getId() <= 0) {
            throw new ServerException(AnswerErrorCode.BOOK_ID_NOT_EXIST);
        }
        if (request.getUser() == null) {
            throw new ServerException(AnswerErrorCode.BOOK_USER_NOT_FOUND);
        }
    }

    private void validateUser(User user) throws ServerException {
        if (user == null || user.getId() <= 0) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
    }

    private void validateGetBook(User user, long id) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (id <= 0) {
            throw new ServerException(AnswerErrorCode.BOOK_USER_NOT_FOUND);
        }
    }

    private void validateAddFavoriteBook(AddFavoriteBookReq request) throws ServerException {
        if (request.getBookId() <= 0) {
            throw new ServerException(AnswerErrorCode.FAVORITE_BOOK_ID_ERROR);
        }
        if (request.getUser() == null) {
            throw new ServerException(AnswerErrorCode.FAVORITE_USER_ERROR);
        }
    }

    private void validateDeleteFavoriteBook(DeleteFavoriteBookDtoReq request) throws ServerException {
        if (request.getBookId() <= 0) {
            throw new ServerException(AnswerErrorCode.FAVORITE_BOOK_ID_ERROR);
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
