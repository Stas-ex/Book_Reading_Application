package com.diploma.black_fox_ex.controllers.books;

import com.diploma.black_fox_ex.dto.CommentDto;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.MessageFormat;

/**
 * This is the class for interacting with the 'comment creator' pageNum.
 */
@Controller
@RequestMapping("/book/{id}")
@RequiredArgsConstructor
public class LookBookController {

    private final BookService bookService;

    /**
     * Go to book pageNum function
     *
     * @param user  retrieving Authorized User Data Using Spring Security
     * @param id    id contains a unique book identifier
     * @param model for creating attributes sent to the server as a response
     * @return like, book to the 'book look' pageNum
     */
    @GetMapping("/{numPage}")
    public String lookBook(@PathVariable long id,
                           @PathVariable int numPage,
                           @AuthenticationPrincipal User user,
                           Model model) {
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
     * @param model      for creating attributes sent to the server as a response
     * @return id to the 'comment creator' pageNum
     */
    @GetMapping("/comment-add")
    public String comment(@ModelAttribute CommentDto commentDto,
                              @PathVariable long id,
                              Model model) {
        return "/comment";
    }

    /**
     * Comment creation function
     *
     * @param user    retrieving Authorized User Data Using Spring Security
     * @param request containing a request to create a comment from the user
     * @param model   for creating attributes sent to the server as a response
     * @return the 'book look' page
     */
    @PostMapping("/comment")
    public String commentAdd(@AuthenticationPrincipal User user,
                             @Valid @ModelAttribute CommentDto commentDto,
                             @PathVariable long id,
                             BindingResult valid,
                             Model model) {
        if(valid.hasErrors()){
            model.addAttribute("warnings", valid.getAllErrors());
            return "/comment";
        }
        bookService.addComment(id, commentDto, user);
        return MessageFormat.format("redirect:/book/{0}", id);
    }
}
