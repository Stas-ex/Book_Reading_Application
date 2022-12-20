package com.diploma.black_fox_ex.controllers.user;

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
 * This is the class for interacting with the "user books" pageNum.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/profile-books")
public class ProfileBooksController {

    private final BookService bookService;
    private final UserService userService;

    /**
     * Function to display all books of a given user
     *
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @return all books to the 'books' pageNum
     */
    @GetMapping("/{numPage}")
    public String viewPage(@AuthenticationPrincipal User user,
                           @PathVariable int numPage,
                           Model model) {
        var bookDtoResp = bookService.getAllBookByUser(user, numPage);

        model.addAttribute("books", bookDtoResp.elem());
        model.addAttribute("pageNumbers", bookDtoResp.pageNumbers());
        model.addAttribute("userMenu", userService.getUserMenu(user));
        return "/profile/profile-viewBook";
    }

    /**
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param id    contains the applied indicator for mutable book
     * @return the 'user book' pageNum otherwise error pageNum
     */
    @GetMapping("/{id}/delete")
    public String deleteBook(@AuthenticationPrincipal User user, @PathVariable long id)  {
        bookService.delete(id, user);
        return "redirect:/profile-books/1";
    }
}
