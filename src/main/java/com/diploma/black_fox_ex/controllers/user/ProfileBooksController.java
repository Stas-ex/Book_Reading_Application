package com.diploma.black_fox_ex.controllers.user;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.dto.DeleteBookDtoReq;
import com.diploma.black_fox_ex.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

/**
 * This is the class for interacting with the "user books" page.
 */
@Controller
public class ProfileBooksController {
    private final BookService bookService;

    @Autowired
    public ProfileBooksController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Function to display all books of a given user
     *
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @return all books to the 'books' page
     */
    @GetMapping("/profile-books")
    public String viewPage(@AuthenticationPrincipal User user, Model model) {
        var bookDtoResp = bookService.getAllBookByUser(user);
        if (bookDtoResp.getError() != null)
            model.addAttribute("errorView", bookDtoResp.getError());
        else
            model.addAttribute("books", bookDtoResp.getBooksDto());
        return "/profile/profile-viewBook";
    }

    /**
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param id    contains the applied indicator for mutable book
     * @param model for creating attributes sent to the server as a response
     * @return the 'user book' page otherwise error page
     */
    @GetMapping("/profile-books/{id}/delete")
    public String deleteBook(@AuthenticationPrincipal User user, @PathVariable long id, Model model) {
        var request = new DeleteBookDtoReq(id, user);
        var response = bookService.deleteBook(request);
        if (response.getError() != null) {
            model.addAttribute("error", response.getError());
            return "errorPage";
        }
        return "redirect:/profile-books";
    }
}
