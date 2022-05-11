package com.diplom.black_fox_ex.controllers;
/*-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.response.ResponseAllHistory;
import com.diplom.black_fox_ex.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
/**-----------------------------------------------------------------------------------**/

@Controller
public class HistoriesController {
    /**-----------------------------------------------------------------------------------**/
    @Autowired
    private HistoryService historyService;

    @Autowired
    LogInController logIn;
    /**-----------------------------------------------------------------------------------**/
    @GetMapping("/histories/{numPage}")
    public String getHistories(@PathVariable int numPage, Model model){
        ResponseAllHistory response = historyService.getAllHistory();
        if(response.getError() != null){
            model.addAttribute("error",response.getError());
        }

        model.addAttribute("histories",response.getMap().get(numPage));
        model.addAttribute("numPage",numPage);
        model.addAttribute("allNumPageList", response.getAllBtnNum());
        model.addAttribute("allTags", response.getTags());
        model.addAttribute("user",logIn.logInViewUser());

        return "histories";
    }
    /**-----------------------------------------------------------------------------------**/
    @GetMapping("/histories/tag/{nameTag}/{numPage}")
    public String getHistoriesByTag(@PathVariable int numPage, @PathVariable String nameTag, Model model){
        ResponseAllHistory response = historyService.getAllHistoryByTag(nameTag);

        if(response.getError() != null){
            model.addAttribute("error",response.getError());
        }

        model.addAttribute("histories",response.getMap().get(numPage));
        model.addAttribute("numPage",numPage);
        model.addAttribute("allNumPageList", response.getAllBtnNum());
        model.addAttribute("allTags", response.getTags());
        model.addAttribute("nameTag", nameTag);
        model.addAttribute("user",logIn.logInViewUser());

        return "histories";
    }
}
