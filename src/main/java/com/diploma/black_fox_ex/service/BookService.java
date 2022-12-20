package com.diploma.black_fox_ex.service;

import com.diploma.black_fox_ex.dto.AddCommentDtoReq;
import com.diploma.black_fox_ex.dto.CommentDto;
import com.diploma.black_fox_ex.dto.DeleteFavoriteBookDtoReq;
import com.diploma.black_fox_ex.dto.PageSplitView;
import com.diploma.black_fox_ex.dto.book.BookDto;
import com.diploma.black_fox_ex.dto.book.BookEditDto;
import com.diploma.black_fox_ex.dto.book.BookReqDto;
import com.diploma.black_fox_ex.dto.book.ReferenceBookDto;
import com.diploma.black_fox_ex.exeptions.AnswerErrorCode;
import com.diploma.black_fox_ex.exeptions.ServerException;
import com.diploma.black_fox_ex.io.ImgManager;
import com.diploma.black_fox_ex.mappers.EntityMapper;
import com.diploma.black_fox_ex.model.Book;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.model.constant.Genre;
import com.diploma.black_fox_ex.repositories.BookRepo;
import com.diploma.black_fox_ex.repositories.CommentsRepo;
import com.diploma.black_fox_ex.repositories.UserRepo;
import com.diploma.black_fox_ex.response.DeleteFavoriteHiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.diploma.black_fox_ex.io.ImgDirectories.BOOK_IMG_DIR;
import static com.diploma.black_fox_ex.mappers.EntityMapper.toComment;

/**
 * This class performs basic actions on received book interaction requests.
 */
@Slf4j
@Service
public class BookService extends PageService {

    int MAX_SYMBOLS = 1800;
    public final int PAGE_BOOKS_SIZE = 50;

    private final BookRepo bookRepo;
    private final UserRepo userRepo;
    private final CommentsRepo commentsRepo;

    @Autowired
    public BookService(BookRepo bookRepo, UserRepo userRepo,
                       CommentsRepo commentsRepo) {
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
        this.commentsRepo = commentsRepo;
    }

    public void create(Long authorId, BookReqDto bookDto) {
        String img = ImgManager.save(BOOK_IMG_DIR, bookDto.getImgFile());
        User author = userRepo.getById(authorId);
        bookRepo.save(EntityMapper.toBook(author, bookDto, img));
    }

    public void update(Long bookId, BookReqDto bookDto, User user) {
        var book = bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        validationAccessUser(book, user);

        String filename = ImgManager.save(BOOK_IMG_DIR, bookDto.getImgFile());
        filename = ImgManager.isDefaultImg(filename) ? book.getImg() : filename;
        bookRepo.save(EntityMapper.updateBook(book, bookDto, filename));
    }

    public void delete(Long id, User user) {
        Book book = bookRepo.getById(id);
        validationAccessUser(book, user);
        book.setDateTimeDeletion(LocalDateTime.now());
        bookRepo.save(book);
    }

    public PageSplitView<List<ReferenceBookDto>> getAllBookByGenre(String genreName, int numPage) {
        validateGetAllBookByGenre(genreName, numPage);
        List<Long> booksId = getBooksIdByGenre(genreName, numPage, PAGE_BOOKS_SIZE * 3);

        return getPageSplitViewByBookIds(booksId, numPage);
    }

    public PageSplitView<BookDto> getLookBookById(Long bookId, int numPage) {
        int fromSymbols = (numPage - 1) * MAX_SYMBOLS;
        int toSymbols = numPage * MAX_SYMBOLS;

        var pageCount = bookRepo.getPageCountByBookId(bookId, MAX_SYMBOLS);
        var dtoBook = bookRepo.getSplitBookDto(bookId, fromSymbols, toSymbols);
        var pages = getPageNumbers(numPage, pageCount);

        return new PageSplitView<>(dtoBook, pages);
    }

    //If the like was clicked
    public boolean isLikeUserId(Long bookId, User user) {
        return user != null && bookRepo.getLikesIdByBookId(bookId).contains(user.getId());
    }


    public PageSplitView<List<ReferenceBookDto>> getAllFavoriteBooksByUser(User user, int numPage) {
        var booksId = bookRepo.getFavoriteBooksId(user, getSkipRows(numPage), PAGE_BOOKS_SIZE * 3);

        return getPageSplitViewByBookIds(booksId, numPage);
    }

    /**
     * Method create comment for a book
     *
     * @param user    includes all fields of an authorized user
     * @param commentDto contains the main parameters for create comment
     * @return dto response , with an error field
     * @see #validateAddComment(User, AddCommentDtoReq)
     */
    public void addComment(long bookId, CommentDto commentDto, User user) {
        var comment = toComment(commentDto, user);
        var book = bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        book.addComments(comment);

        commentsRepo.save(comment);
        bookRepo.save(book);
    }


    public PageSplitView<List<ReferenceBookDto>> getAllBookByUser(User user, int numPage) {
        var booksId = bookRepo.getBooksIdByUser(user, getSkipRows(numPage), PAGE_BOOKS_SIZE * 3);
        return getPageSplitViewByBookIds(booksId, numPage);
    }


    public BookEditDto getBookEditById(User user, Long bookId) throws ServerException {
        validateGetBook(user, bookId);

        BookEditDto bookEdit = bookRepo.getBookEditDto(bookId);

        if (!bookEdit.getAuthor().equals(user)) {
            throw new RuntimeException(
                    String.format("user %d is not the author of the book", user.getId())
            );
        }

        return bookEdit;
    }

    public void addFavoriteBook(long userId, Long bookId) {
        var book = bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        var user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getFavorite().contains(book)) user.removeFavorite(book);
        else user.addFavorite(book);

        userRepo.save(user);
        bookRepo.save(book);
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
            List<Book> listAllFavorite = List.of();//userRepo.findFavoriteBooksUsingUserId(user.getId());

            listAllFavorite.remove(book);
            book.getLikes().remove(user);
            user.setFavorite(listAllFavorite);

            bookRepo.save(book);
            userRepo.save(user);

        } catch (ServerException e) {
            response.setError(e.getErrorMessage());
            log.warn("User ({}), Book ({}) -> (deleteFavoriteBook) error {}.", request.getUser() != null ? request.getUser().getId() : "null", request.getBookId(), e.getErrorMessage());
        } catch (Exception ex) {
            response.setError(AnswerErrorCode.EXCEPTION_ERROR.getMsg());
            log.error("User ({}), Book ({}) -> (deleteFavoriteBook) error {}.", request.getUser() != null ? request.getUser().getId() : "null", request.getBookId(), ex.getMessage());
        }
        return response;
    }

    public List<Genre> getAllGenres() {
        return Arrays.stream(Genre.values()).toList();
    }

    private List<ReferenceBookDto> getRefBookById(List<Long> booksId) {
        List<ReferenceBookDto> refBooks = new ArrayList<>();
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

    private PageSplitView<List<ReferenceBookDto>> getPageSplitViewByBookIds(List<Long> booksId, int numPage) {
        int bookIdCount = Math.min(booksId.size(), PAGE_BOOKS_SIZE);
        List<ReferenceBookDto> refBooks = getRefBookById(booksId.subList(0, bookIdCount));

        int pageNumCountView = (int) (numPage + Math.ceil((double) booksId.size() / PAGE_BOOKS_SIZE));
        List<Integer> pageNumbers = getPageNumbers(numPage, pageNumCountView);

        return new PageSplitView<>(refBooks, pageNumbers);
    }
}
