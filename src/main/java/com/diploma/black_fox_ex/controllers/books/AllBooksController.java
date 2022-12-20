package com.diploma.black_fox_ex.controllers.books;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.service.BookService;
import com.diploma.black_fox_ex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This is the class for interacting with the "Books" pageNum.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/books/{genreName}/{numPage}")
public class AllBooksController {

    private final BookService bookService;
    private final UserService userService;

    /**
     * The function of displaying books by genres
     *
     * @param user      retrieving Authorized User Data Using Spring Security
     * @param numPage   contains the serial number of the pageNum of displayed books
     * @param genreName contains the name of the genre that is used in the search filter
     * @param model     for creating attributes sent to the server as a response
     * @return books, user, genres, number pageNum, name genre to the 'books' pageNum
     */
    @GetMapping
    public String getBooksByGenre(@AuthenticationPrincipal User user,
                                  @PathVariable String genreName,
                                  @PathVariable int numPage,
                                  Model model) {
        var booksPage = bookService.getAllBookByGenre(genreName, numPage);
        model.addAttribute("userMenu", userService.getUserMenu(user));
        model.addAttribute("allGenres", bookService.getAllGenres());
        model.addAttribute("pageNumbers", booksPage.pageNumbers());
        model.addAttribute("books", booksPage.elem());
        model.addAttribute("genreName", genreName);
        model.addAttribute("numPage", numPage);
        return "/book/books";
    }
}
