package com.diplom.black_fox_ex.controllers;
/**-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.request.CommentAddDtoRequest;
import com.diplom.black_fox_ex.request.HistoryLookDtoRequest;
import com.diplom.black_fox_ex.response.CommentAddDtoResponse;
import com.diplom.black_fox_ex.response.HistoryLookDtoResponse;
import com.diplom.black_fox_ex.response.UserDto;
import com.diplom.black_fox_ex.service.LookHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**-----------------------------------------------------------------------------------**/
@Controller
@RequestMapping("/history/{id}")
public class LookHistoryController {
    /**-----------------------------------------------------------------------------------**/
    @Autowired
    private LookHistoryService lookHistoryService;

    @Autowired
    private LogInController logIn;
    /**-----------------------------------------------------------------------------------**/

    @GetMapping("/look")
    public String lookHistory(@PathVariable long id, Model model){

        UserDto userDto = logIn.logIn();

        HistoryLookDtoRequest request = new HistoryLookDtoRequest(userDto,id);
        HistoryLookDtoResponse response = lookHistoryService.getHistoryById(request);

        if(response.getError() != null){
            model.addAttribute("error",response.getError());
            System.out.println("error");
            return "redirect:/histories";
        }
        model.addAttribute("likeActive",response.getLikeActive());
        model.addAttribute("history",response.getHistoryDto());
        return "history-look";
    }
    /**-----------------------------------------------------------------------------------**/
    @GetMapping("/comment")
    public String comment(@PathVariable long id, Model model){
        model.addAttribute("id",id);
        return "comment-add";
    }
    /**-----------------------------------------------------------------------------------**/
    @PostMapping("/comment/add")
    public String commentAdd(@PathVariable long id,
                             @RequestParam(defaultValue = "warning") String color,
                             @RequestParam String bigText,
                             Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        CommentAddDtoRequest request = new CommentAddDtoRequest(id,username,bigText,color);
        CommentAddDtoResponse response = lookHistoryService.addComment(request);

        if(response.getError() != null){
            model.addAttribute("error",response.getError());
            model.addAttribute("id",id);
            return "comment-add";
        }
        return "redirect:/history/"+id+"/look";
    }

}
