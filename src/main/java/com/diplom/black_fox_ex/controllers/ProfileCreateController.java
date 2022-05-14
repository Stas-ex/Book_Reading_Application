package com.diplom.black_fox_ex.controllers;
import com.diplom.black_fox_ex.request.HistoryCreateDtoReq;
import com.diplom.black_fox_ex.response.HistoryCreateDtoResponse;
import com.diplom.black_fox_ex.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
@Controller
@RequestMapping("/profile-create")
public class ProfileCreateController {
    private final HistoryService historyService;

    @Autowired
    public ProfileCreateController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public String profileCreateHistory(Model model) {
        model.addAttribute("tags",historyService.getAllTag());
        return "/profile/profile-createHistory";
    }

    @PostMapping("/add")
    public String createHistory(@RequestParam String title, @RequestParam String bigText,
                                @RequestParam("img") MultipartFile img, @RequestParam(defaultValue = "") String tag,
                                Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        HistoryCreateDtoReq dto = new HistoryCreateDtoReq(title, img, bigText,tag, username);
        HistoryCreateDtoResponse response = historyService.createHistory(dto);

        if (response.getError() != null) {
            model.addAttribute("bigText", bigText);
            model.addAttribute("title", title);
            model.addAttribute("error", response.getError());
            model.addAttribute("button", "Create");

            return "/profile/profile-createHistory";
        }
        return "redirect:/profile-history";
    }
}
