package com.diplom.black_fox_ex.controllers;
/*-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.request.HelpDeleteAnswerRequest;
import com.diplom.black_fox_ex.response.HelpDeleteAnswerResponse;
import com.diplom.black_fox_ex.response.HelpGetAllAnswersResponse;
import com.diplom.black_fox_ex.response.UserDto;
import com.diplom.black_fox_ex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
/**-----------------------------------------------------------------------------------**/
@Controller
@RequestMapping("/help")
public class HelpController {
    /**-----------------------------------------------------------------------------------**/
    @Autowired
    LogInController logIn;

    @Autowired
    UserService userService;
    /**-----------------------------------------------------------------------------------**/
    @GetMapping
    public String start(Model model){
        UserDto userDto = logIn.logInViewUser();
        HelpGetAllAnswersResponse response = userService.getAllAnswersSupportByUserDto(userDto);
        if(response.getErrors() != null){
            model.addAttribute("error",response.getErrors());
        }
        else{
            model.addAttribute("answers", response.getAnswers());
        }
        model.addAttribute("user", userDto);
        return "help";
    }
    /**-----------------------------------------------------------------------------------**/
    @GetMapping("/{id}/delete")
    public String deleteAnswerSupport(@PathVariable long id, Model model){
        UserDto userDto = logIn.logInViewUser();
        HelpDeleteAnswerRequest request = new HelpDeleteAnswerRequest(userDto,id);
        HelpDeleteAnswerResponse response = userService.deleteAnswerByUser(request);
        if(response.getErrors() != null){
            model.addAttribute("error",response.getErrors());
            return "help";
        }
        return "redirect:/help";
    }
}
