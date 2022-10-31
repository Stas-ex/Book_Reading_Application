package com.diploma.black_fox_ex.controllers.books;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.service.BookService;
import com.diploma.black_fox_ex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * This is the class for interacting with the "Books" page.
 */
@Controller
public class AllBooksController {
    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public AllBooksController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    /**
     * The function of displaying books by genres
     * @param user retrieving Authorized User Data Using Spring Security
     * @param numPage contains the serial number of the page of displayed books
     * @param nameGenre contains the name of the genre that is used in the search filter
     * @param model for creating attributes sent to the server as a response
     * @return books, user, genres, number page, name genre to the 'books' page
     */
    @GetMapping("/books/{nameGenre}/{numPage}")
    public String getBooksByGenre(@AuthenticationPrincipal User user, @PathVariable int numPage, @PathVariable String nameGenre, Model model) {
        var response = bookService.getAllBookByGenre(nameGenre, numPage);

        if (response.getError() != null) {
            model.addAttribute("errorBooks", response.getError());
        }
        model.addAttribute("userMenu", userService.getUserMenu(user));
        model.addAttribute("allGenres", response.getGenres());
        model.addAttribute("books", response.getListDto());
        model.addAttribute("pageNumbers", response.getPageNumbers());
        model.addAttribute("numPage", numPage);
        model.addAttribute("nameGenre", nameGenre);

        return "/book/books";
    }
}
