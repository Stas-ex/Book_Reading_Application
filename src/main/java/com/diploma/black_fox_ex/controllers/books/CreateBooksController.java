package com.diploma.black_fox_ex.controllers.books;

import com.diploma.black_fox_ex.dto.book.BookReqDTO;
import com.diploma.black_fox_ex.exeptions.ServerException;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This is the class for interacting with the "create books" pageNum.
 */
@Controller
@RequestMapping("/book")
public class CreateBooksController {
    private final BookService bookService;

    @Autowired
    public CreateBooksController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * The function to go to the book creation pageNum
     *
     * @param book  designed to send a non-null element to the server
     * @param model for creating attributes sent to the server as a response
     * @return list genres, title, to the 'book editing' pageNum
     */
    @GetMapping("/createPage")
    public String profileCreateBook(@ModelAttribute("book") BookReqDTO book, Model model) {
        model.addAttribute("genres", bookService.getAllGenres());
        model.addAttribute("title", "book-create.title");
        return "/book/book_editing";
    }

    /**
     * The function needed to go to the pageNum of all books
     *
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param id    contains the unique indicator for mutable book
     * @param model for creating attributes sent to the server as a response
     * @return pageNum title, list genres, book to the 'book editing' pageNum
     */
    @GetMapping("/{id}/editPage")
    public String editingBookPage(@AuthenticationPrincipal User user,
                                  @PathVariable long id, Model model) throws ServerException {
        var book = bookService.getBookEditById(user, id);

        model.addAttribute("title", "book-edit.title");
        model.addAttribute("genres", bookService.getAllGenres());
        model.addAttribute("book", book);
        model.addAttribute("id", id);
        return "/book/book_editing";
    }

    /**
     * Function to create book
     *
     * @param user       retrieving Authorized User Data Using Spring Security
     * @param bookReqDto containing a request from the user
     * @param model      for creating attributes sent to the server as a response
     * @return the 'user books' pageNum
     */
    @PostMapping("/create")
    public String createBook(@AuthenticationPrincipal User user,
                             @Valid @ModelAttribute("book") BookReqDTO bookReqDto,
                             BindingResult valid,
                             Model model) {
        if (valid.hasErrors()) {
            model.addAttribute("title", "book-create.title");
            model.addAttribute("genres", bookService.getAllGenres());
            model.addAttribute("warnings", valid.getAllErrors());
            return "book/book_editing";
        }
        bookService.createBook(user.getId(), bookReqDto);

        return "redirect:/profile-books/page-1";
    }

    /**
     * The function needed to update the generated book
     *
     * @param user       retrieving Authorized User Data Using Spring Security
     * @param bookReqDto containing a request from the user
     * @param model      for creating attributes sent to the server as a response
     * @return 'user books' pageNum
     */
    @PostMapping("/{id}/edit")
    public String editBook(@AuthenticationPrincipal User user,
                           @ModelAttribute("book") @Valid BookReqDTO bookReqDto,
                           @PathVariable("id") Long bookId,
                           BindingResult valid,
                           Model model) {
        if (valid.hasErrors()) {
            model.addAttribute("title", "book-edit.title");
            model.addAttribute("genres", bookService.getAllGenres());
            model.addAttribute("warnings", valid.getAllErrors());
            return "book/book_editing";
        }
        bookService.updateBook(bookId, bookReqDto, user);
        return "redirect:/profile-books/page-1";
    }

}
