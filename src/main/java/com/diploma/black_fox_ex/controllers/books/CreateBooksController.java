package com.diploma.black_fox_ex.controllers.books;

import com.diploma.black_fox_ex.dto.UpdateBooksDtoReq;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.service.BookService;
import com.diploma.black_fox_ex.dto.CreateBookDtoReq;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

/**
 * This is the class for interacting with the "create books" page.
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
     * The function to go to the book creation page
     *
     * @param bookDto designed to send a non-null element to the server
     * @param model   for creating attributes sent to the server as a response
     * @return list genres, title, to the 'book editing' page
     */
    @GetMapping("/createPage")
    public String profileCreateBook(@ModelAttribute CreateBookDtoReq bookDto, Model model) {
        model.addAttribute("genres", bookService.getAllGenre());
        model.addAttribute("title", "Create");
        model.addAttribute("bookDto", bookDto);
        return "/book/book_editing";
    }

    /**
     * The function needed to go to the page of all books
     *
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param id    contains the unique indicator for mutable book
     * @param model for creating attributes sent to the server as a response
     * @return page title, list genres, book to the 'book editing' page
     */
    @GetMapping("/{id}/editPage")
    public String editingBookPage(@AuthenticationPrincipal User user,
                                  @PathVariable long id, Model model) {
        var response = bookService.getBookEditById(user, id);
        if (response.getError() != null) {
            return "redirect:/profile-books";
        }

        model.addAttribute("title", "Update");
        model.addAttribute("genres", bookService.getAllGenre());
        model.addAttribute("bookDto", response.getBookDto());
        return "/book/book_editing";
    }

    /**
     * Function to create book
     *
     * @param user    retrieving Authorized User Data Using Spring Security
     * @param bookDto containing a request from the user
     * @param model   for creating attributes sent to the server as a response
     * @return the 'user books' page
     */
    @PostMapping("/create")
    public String createBook(@AuthenticationPrincipal User user,
                             @ModelAttribute CreateBookDtoReq bookDto,
                             Model model) {
        var response = bookService.createBook(user, bookDto);
        if (response.getError() != null) {
            model.addAttribute("title", "Create");
            model.addAttribute("bookDto", bookDto);
            model.addAttribute("genres", bookService.getAllGenre());
            model.addAttribute("errorCreateBook", response.getError());
            return "/book/book_editing";
        }
        return "redirect:/profile-books";
    }

    /**
     * The function needed to update the generated book
     *
     * @param user    retrieving Authorized User Data Using Spring Security
     * @param bookDto containing a request from the user
     * @param model   for creating attributes sent to the server as a response
     * @return 'user books' page
     */
    @PostMapping("/{id}/update")
    public String updateBook(@AuthenticationPrincipal User user,
                             @ModelAttribute UpdateBooksDtoReq bookDto, Model model) {
        var response = bookService.updateBook(user, bookDto);
        if (response.getError() != null) {
            model.addAttribute("bookDto", bookDto);
            model.addAttribute("title", "Update");
            model.addAttribute("genres", bookService.getAllGenre());
            model.addAttribute("id", bookDto.getId());
            model.addAttribute("errorCreateBook", response.getError());
            return "book/book_editing";
        }
        return "redirect:/profile-books";
    }

}
