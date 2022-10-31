package com.diploma.black_fox_ex.controllers.books;

import com.diploma.black_fox_ex.dto.AddFavoriteBookReq;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.dto.DeleteFavoriteBookDtoReq;
import com.diploma.black_fox_ex.service.BookService;
import com.diploma.black_fox_ex.service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * This is the class for interacting with the "favorite book" page.
 */
@Controller
@RequestMapping("/favorite")
public class FavoriteBookController {
    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public FavoriteBookController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    /**
     * Function to go to the 'favorite book' page
     *
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @return user books to the 'favorite book' page
     */
    @GetMapping
    public String startFavoriteBook(@AuthenticationPrincipal User user, Model model) {
        var responseDto = bookService.getAllFavoriteByUser(user);
        if (responseDto.getError() != null) {
            model.addAttribute("errorFavorite", responseDto.getError());
        }
        model.addAttribute("userMenu", userService.getUserMenu(user));
        model.addAttribute("books", responseDto.getListDto());
        return "/book/favorite-book";
    }

    /**
     * Function to add book to the saved section
     *
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param id    contains the applied indicator for mutable book
     * @param model for creating attributes sent to the server as a response
     * @return the 'book look' page otherwise error page
     */
    @GetMapping("/{id}/add")
    public String addFavoriteBook(@AuthenticationPrincipal User user, @PathVariable long id, Model model) {
        var request = new AddFavoriteBookReq(user, id);
        var response = bookService.addFavoriteBook(request);
        if (response.getError() != null) {
            System.out.println(response.getError());
            model.addAttribute("error", response.getError());
            return "/errorPage";
        }
        return "redirect:/book/%d/look".formatted(id);
    }

    /**
     * Function to delete book from section "Favorites"
     *
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param id    contains the applied indicator for mutable book
     * @param model for creating attributes sent to the server as a response
     * @return the 'favorite books' page otherwise error page
     */
    @GetMapping("/{id}/delete")
    public String deleteFavoriteBook(@AuthenticationPrincipal User user, @PathVariable long id, Model model) {
        var request = new DeleteFavoriteBookDtoReq(id, user);
        var response = bookService.deleteFavoriteBook(request);
        if (response.getError() != null) {
            model.addAttribute("error", response.getError());
            return "errorPage";
        }
        return "redirect:/favorite";
    }
}
