package com.diploma.black_fox_ex.service;

import com.diploma.black_fox_ex.dto.AddCommentDtoReq;
import com.diploma.black_fox_ex.dto.DeleteFavoriteBookDtoReq;
import com.diploma.black_fox_ex.dto.PageSplitView;
import com.diploma.black_fox_ex.dto.book.AbstractBookDTO;
import com.diploma.black_fox_ex.dto.book.BookEditDTO;
import com.diploma.black_fox_ex.dto.book.BookReqDTO;
import com.diploma.black_fox_ex.dto.book.ReferenceBookDTO;
import com.diploma.black_fox_ex.exeptions.AnswerErrorCode;
import com.diploma.black_fox_ex.exeptions.ServerException;
import com.diploma.black_fox_ex.io.FileDirectories;
import com.diploma.black_fox_ex.io.FileManager;
import com.diploma.black_fox_ex.model.Book;
import com.diploma.black_fox_ex.model.Comment;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.model.constant.Genre;
import com.diploma.black_fox_ex.repositories.BookRepo;
import com.diploma.black_fox_ex.repositories.CommentsRepo;
import com.diploma.black_fox_ex.repositories.UserRepo;
import com.diploma.black_fox_ex.response.AddCommentDtoResp;
import com.diploma.black_fox_ex.response.DeleteFavoriteHiResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class performs basic actions on received book interaction requests.
 */
@Service
public class BookService extends AbstractService {

    private final static Logger logger = LoggerFactory.getLogger(BookService.class);

    int MAX_SYMBOLS = 1800;
    public final int PAGE_BOOKS_SIZE = 50;

    private final BookRepo bookRepo;
    private final UserRepo userRepo;
    private final CommentsRepo commentsRepo;
    private final FileManager fileManager;

    @Autowired
    public BookService(BookRepo bookRepo, UserRepo userRepo,
                       CommentsRepo commentsRepo) {
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
        this.commentsRepo = commentsRepo;
        fileManager = new FileManager();
    }

    public void createBook(Long authorId, BookReqDTO bookReqDto) {
        String filename = fileManager.createFile(FileDirectories.BOOK_IMG_DIR, bookReqDto.getImgFile());
        User author = userRepo.getById(authorId);
        Book book = new Book(bookReqDto, filename, author);
        bookRepo.save(book);
    }

    public void updateBook(Long bookId, BookReqDTO bookReqDTO, User user) {
        Book book = bookRepo.getById(bookId);
        validationAccessUser(book, user);

        String filePath = fileManager.createFile(FileDirectories.BOOK_IMG_DIR, bookReqDTO.getImgFile());
        filePath = filePath.equals("book.jpeg") ? book.getFilenameBg() : filePath;

        book.updateBook(bookReqDTO, filePath);
        bookRepo.save(book);
    }

    public void deleteBook(Long id, User user) {
        Book book = bookRepo.getById(id);
        validationAccessUser(book, user);
        book.setDateTimeDelete(LocalDateTime.now());
        bookRepo.save(book);
    }

    public PageSplitView<List<ReferenceBookDTO>> getAllBookByGenre(String genreName, int numPage) {
        validateGetAllBookByGenre(genreName, numPage);
        List<Long> booksId = getBooksIdByGenre(genreName, numPage, PAGE_BOOKS_SIZE * 3);

        return getPageSplitViewByBookIds(booksId, numPage);
    }

    public PageSplitView<AbstractBookDTO> getLookBookById(Long bookId, int numPage) {
        int fromSymbols = (numPage - 1) * MAX_SYMBOLS;
        int toSymbols = numPage * MAX_SYMBOLS;

        var pageCount = bookRepo.getPageCountByBookId(bookId, MAX_SYMBOLS);
        var dtoBook = bookRepo.getSplitBookDTO(bookId, fromSymbols, toSymbols);
        var pages = getPageNumbers(numPage, pageCount);

        return new PageSplitView<>(dtoBook, pages);
    }

    //If the like was clicked
    public boolean isLikeUserId(Long bookId, User user) {
        return user != null && bookRepo.getLikesIdByBookId(bookId).contains(user.getId());
    }


    public PageSplitView<List<ReferenceBookDTO>> getAllFavoriteBooksByUser(User user, int numPage) {
        var booksId = bookRepo.getFavoriteBooksId(user, getSkipRows(numPage), PAGE_BOOKS_SIZE * 3);

        return getPageSplitViewByBookIds(booksId, numPage);
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


    public PageSplitView<List<ReferenceBookDTO>> getAllBookByUser(User user, int numPage) {
        var booksId = bookRepo.getBooksIdByUser(user, getSkipRows(numPage), PAGE_BOOKS_SIZE * 3);
        return getPageSplitViewByBookIds(booksId, numPage);
    }


    public BookEditDTO getBookEditById(User user, Long bookId) throws ServerException {
        validateGetBook(user, bookId);

        BookEditDTO bookEdit = bookRepo.getBookEditDTO(bookId);

        if (!bookEdit.getAuthor().equals(user)) {
            throw new RuntimeException(
                    String.format("user %d is not the author of the book", user.getId())
            );
        }

        return bookEdit;
    }

    public void addFavoriteBook(Long userId, Long bookId) {
        var likesId = bookRepo.getLikesIdByBookId(bookId);
        var book = bookRepo.getById(bookId);

        if (likesId.contains(userId)) {
            userRepo.findById(book.getId());
        }

    }

    /**
     * The method implements deleting book from the saved books tab for this user
     *
     * @param request contains the main parameters for delete favorite book
     * @return dto response, with an error field
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

    public List<Genre> getAllGenres() {
        return Arrays.stream(Genre.values()).toList();
    }

    private List<ReferenceBookDTO> getRefBookById(List<Long> booksId) {
        List<ReferenceBookDTO> refBooks = new ArrayList<>();
        for (Long aLong : booksId) {
            refBooks.add(bookRepo.getReferenceBook(aLong));
        }
        return refBooks;
    }

    private List<Long> getBooksIdByGenre(String genreName, int numPage, int limit) {
        if (genreName.equals("All")) {
            return bookRepo.getBooksIdAllGenres(getSkipRows(numPage), limit);
        }
        return bookRepo.getBooksIdByGenre(Genre.valueOf(genreName.toUpperCase()), getSkipRows(numPage), limit);
    }

    private int getSkipRows(int numPage) {
        return (Math.max(numPage, 1) - 1) * PAGE_BOOKS_SIZE;
    }

    private void validationAccessUser(Book book, User user) {
        if (!book.getAuthor().equals(user)) {
            throw new RuntimeException("The user %d is not the author of the book.".formatted(user.getId()));
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

    private void validateGetBook(User user, Long id) throws ServerException {
        if (user == null) {
            throw new ServerException(AnswerErrorCode.USER_NOT_REGISTERED);
        }
        if (id != null && id <= 0) {
            throw new ServerException(AnswerErrorCode.BOOK_USER_NOT_FOUND);
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

    private void validateGetAllBookByGenre(String genreName, int numPage) {
        if (genreName == null) {
            throw new RuntimeException("Genre is null");
        }
        if (numPage <= 0) {
            throw new RuntimeException("Error number page");
        }
    }

    private PageSplitView<List<ReferenceBookDTO>> getPageSplitViewByBookIds(List<Long> booksId, int numPage) {
        int bookIdCount = Math.min(booksId.size(), PAGE_BOOKS_SIZE);
        List<ReferenceBookDTO> refBooks = getRefBookById(booksId.subList(0, bookIdCount));

        int pageNumCountView = (int) (numPage + Math.ceil((double) booksId.size() / PAGE_BOOKS_SIZE));
        List<Integer> pageNumbers = getPageNumbers(numPage, pageNumCountView);

        return new PageSplitView<>(refBooks, pageNumbers);
    }
}
