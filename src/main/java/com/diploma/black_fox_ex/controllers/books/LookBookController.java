package com.diploma.black_fox_ex.controllers.books;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.dto.AddCommentDtoReq;
import com.diploma.black_fox_ex.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

/**
 * This is the class for interacting with the 'comment creator' pageNum.
 */
@Controller
@RequestMapping("/book/{id}")
public class LookBookController {

    private final BookService bookService;

    @Autowired
    public LookBookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Go to book pageNum function
     *
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param id    id contains a unique book identifier
     * @param model for creating attributes sent to the server as a response
     * @return like, book to the 'book look' pageNum
     */
    @GetMapping("/look/{numPage}")
    public String lookBook(@PathVariable long id, @PathVariable int numPage,
                           @AuthenticationPrincipal User user, Model model) {
        var page = bookService.getLookBookById(id, numPage);

        model.addAttribute("bookLook", page.elem());
        model.addAttribute("bookId", id);
        model.addAttribute("pageNumbers", page.pageNumbers());
        model.addAttribute("likeActive", bookService.isLikeUserId(id, user));
//        model.addAttribute("comments", bookService.getComments(id, numPage));
        return "/book/book-look";
    }

    /**
     * @param commentDto creating a null model to render the pageNum
     * @param id         id contains a unique book identifier
     * @param model      for creating attributes sent to the server as a response
     * @return id to the 'comment creator' pageNum
     */
    @GetMapping("/comment")
    public String comment(@ModelAttribute AddCommentDtoReq commentDto, @PathVariable long id, Model model) {
        model.addAttribute("commentDto", commentDto);
        model.addAttribute("id", id);
        return "comment-add";
    }

    /**
     * Comment creation function
     *
     * @param user    retrieving Authorized User Data Using Spring Security
     * @param request containing a request to create a comment from the user
     * @param model   for creating attributes sent to the server as a response
     * @return the 'book look' page
     */
    @PostMapping("/comment/add")
    public String commentAdd(@AuthenticationPrincipal User user,
                             @ModelAttribute AddCommentDtoReq request,
                             Model model) {
        var response = bookService.addComment(user, request);
        if (response.getError() != null) {
            model.addAttribute("errorComment", response.getError());
            model.addAttribute("commentDto", request);
            model.addAttribute("id", request.getId());
            return "comment-add";
        }
        return MessageFormat.format("redirect:/book/{0}/look", request.getId());
    }
}
