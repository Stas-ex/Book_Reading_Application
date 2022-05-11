package com.diplom.black_fox_ex.controllers;
/*-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.request.HistoryDeleteDtoReq;
import com.diplom.black_fox_ex.request.HistoryUpdateDtoReq;
import com.diplom.black_fox_ex.response.*;
import com.diplom.black_fox_ex.service.HistoryService;
import com.diplom.black_fox_ex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
/**-----------------------------------------------------------------------------------**/
@Controller
@RequestMapping("/profile-history")
public class ProfileViewHistoryController {
    /**-----------------------------------------------------------------------------------**/
    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    LogInController logIn;
    /**-----------------------------------------------------------------------------------**/
    @GetMapping
    public String profileViewHistory(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfileViewHiAllDtoResponse response = userService.getAllHistoryByUser(username);

        if (response.getError() != null) {
            model.addAttribute("error", response.getError());
        } else {
            model.addAttribute("histories", response.getHistoryDto());
        }
        model.addAttribute("user",logIn.logInViewUser());
        return "/profile/profile-viewHistory";
    }
    /**-----------------------------------------------------------------------------------**/
    @GetMapping("/{id}/edit")
    public String profileViewHistory(@PathVariable long id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfileViewHiDtoResponse response = userService.getHistory(username,id);
        String bigText = response.getHistoryDto().getBigText().replaceAll("<br>","\n");
        response.getHistoryDto().setBigText(bigText);
        if(response.getError() != null){
            System.out.println(response.getError());
            return "redirect:/profile-history";
        }

        model.addAttribute("history",response.getHistoryDto());
        return "/profile/profile-updateHistory";
    }
    /**-----------------------------------------------------------------------------------**/
    @PostMapping("/{id}/update")
    public String createHistory(@RequestParam String title, @RequestParam String bigText,
                                @RequestParam("img") MultipartFile img, @PathVariable long id,
                                Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        HistoryUpdateDtoReq dtoReq = new HistoryUpdateDtoReq(id,title,bigText,img, username);
        HistoryUpdateDtoResponse response = historyService.updateHistory(dtoReq);

        if (response.getError() != null) {
            model.addAttribute("history",dtoReq);
            model.addAttribute("error",response.getError());
            return "/profile/profile-updateHistory";
        }
        return "/profile/profile-viewHistory";
    }
    /**-----------------------------------------------------------------------------------**/
    @GetMapping("/{id}/delete")
    public String deleteHistory(@PathVariable long id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        HistoryDeleteDtoReq request = new HistoryDeleteDtoReq(id,username);
        HistoryDeleteDtoResponse response = historyService.deleteByIdAndUsername(request);
        if (response.getError() != null) {
            model.addAttribute("error",response.getError());
        }
        return "redirect:/profile-history/";
    }
}
