package com.diploma.black_fox_ex.controllers.books;

import com.diploma.black_fox_ex.dto.DeleteFavoriteBookDtoReq;
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
 * This is the class for interacting with the "favorite book" pageNum.
 */
@Controller
@RequestMapping("/book/favorite")
@RequiredArgsConstructor
public class FavoriteBookController {

    private final UserService userService;
    private final BookService bookService;

    /**
     * Function to go to the 'favorite book' pageNum
     *
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @return user books to the 'favorite book' pageNum
     */
    @GetMapping("/{page}")
    public String getPage(@AuthenticationPrincipal User user,
                                    @PathVariable("page") int numPage,
                                    Model model) {
        var page = bookService.getAllFavoriteBooksByUser(user, numPage);
        model.addAttribute("userMenu", userService.getUserMenu(user));
        model.addAttribute("pageNumbers", page.pageNumbers());
        model.addAttribute("books", page.elem());
        return "/book/favorite-book";
    }

    /**
     * Function to add book to the saved section
     *
     * @param user   retrieving Authorized User Data Using Spring Security
     * @param bookId contains the applied indicator for mutable book
     * @return the 'book look' pageNum otherwise error pageNum
     */
    @GetMapping("{id}/add")
    public String addFavoriteBook(@AuthenticationPrincipal User user, @PathVariable("id") long bookId) {
        bookService.addFavoriteBook(user.getId(), bookId);
        return "redirect:/book/%d/1".formatted(bookId);
    }

    /**
     * Function to delete book from section "Favorites"
     *
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param id    contains the applied indicator for mutable book
     * @param model for creating attributes sent to the server as a response
     * @return the 'favorite books' pageNum otherwise error pageNum
     */
    @GetMapping("{id}/delete")
    public String deleteFavoriteBook(@AuthenticationPrincipal User user, @PathVariable long id, Model model) {
        var request = new DeleteFavoriteBookDtoReq(id, user);
        var response = bookService.deleteFavoriteBook(request);
        if (response.getError() != null) {
            model.addAttribute("error", response.getError());
            return "error";
        }
        return "redirect:/book/favorite/1";
    }
}
