package com.diploma.black_fox_ex.controllers.histories;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.service.HistoryService;
import com.diploma.black_fox_ex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * This is the class for interacting with the "user history" page.
 */
@Controller
public class AllHistoriesController {
    private final HistoryService historyService;
    private final UserService userService;

    @Autowired
    public AllHistoriesController(HistoryService historyService, UserService userService) {
        this.historyService = historyService;
        this.userService = userService;
    }

    /**
     * The function of displaying stories by tags
     * @param user retrieving Authorized User Data Using Spring Security
     * @param numPage contains the serial number of the page of displayed stories
     * @param nameTag contains the name of the tag that is used in the search filter
     * @param model for creating attributes sent to the server as a response
     * @return histories, user, tags, number page, name tag to the 'histories' page
     */
    @GetMapping("/histories/{nameTag}/{numPage}")
    public String getHistoriesByTag(@AuthenticationPrincipal User user, @PathVariable int numPage, @PathVariable String nameTag, Model model){
        var response = historyService.getAllHistoryByTag(nameTag, numPage);

        if(response.getError() != null){
            model.addAttribute("errorHistories",response.getError());
        }
        model.addAttribute("userMenu",userService.getUserMenu(user));
        model.addAttribute("allTags", response.getTags());
        model.addAttribute("histories",response.getListDto());
        model.addAttribute("pageNumbers", response.getPageNumbers());
        model.addAttribute("numPage", numPage);
        model.addAttribute("nameTag", nameTag);

        return "/history/histories";
    }
}
