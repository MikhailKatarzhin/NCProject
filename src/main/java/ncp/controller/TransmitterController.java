package ncp.controller;

import ncp.model.Transmitter;
import ncp.service.TransmitterService;
import ncp.service.TransmitterStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transmitter")
public class TransmitterController {
    @Autowired
    private TransmitterService transmitterService;
    @Autowired
    private TransmitterStatusService transmitterStatusService;
    @GetMapping
    public String management(){
        return "redirect:/transmitter/list/1";
    }

    @GetMapping("/list/{numberPageList}")
    public String managementByOffset(@PathVariable Long numberPageList, ModelMap model){
        if (numberPageList < 1L)
            return "redirect:/transmitter/list/1";
        long nPage=transmitterService.pageCount();
        if (numberPageList > nPage)
            return "redirect:/transmitter/list/"+nPage;
        model.addAttribute("nPage", nPage);
        model.addAttribute("currentPage", numberPageList);
        List<Transmitter> transmitterList =transmitterService.transmitterListByNumberPageList(numberPageList);
        model.addAttribute("transmitters", transmitterList);
        return "transmitter/transmitter_management";
    }

    @GetMapping("/to_page")
    public String toPage(@RequestParam("toPage") Long toPage){
        return "redirect:/transmitter/list/"+toPage;
    }

    @GetMapping("/list/{numberPageList}/next_page")
    public String nextPage(@PathVariable Long numberPageList, ModelMap model){
        return "redirect:/transmitter/list/"+(numberPageList+1L);
    }

    @GetMapping("/list/{numberPageList}/preview_page")
    public String previewPage(@PathVariable Long numberPageList, ModelMap model){
        return "redirect:/transmitter/list/"+(numberPageList-1L);
    }

    @GetMapping("/last_page")
    public String lastPage(){
        return "redirect:/transmitter/list/"+transmitterService.pageCount();
    }

    @GetMapping("/add")
    public String transmitterAddForm(ModelMap model){
        model.addAttribute("transmitter", new Transmitter());
        return "transmitter/add";
    }

    @PostMapping("/add")
    public String transmitterAdd(@RequestParam String description){
        transmitterService.saveNew(description);
        return "redirect:/transmitter/list/1";
    }

    @PostMapping("/list/{numberPageList}/{id}/remove")
    public String removeById(@PathVariable Long numberPageList, @PathVariable Long id){
        transmitterService.deleteById(id);
        return "redirect:/transmitter/list/"+numberPageList;
    }

    @GetMapping("/list/{numberPageList}/{id}/setup")
    public String setupById(@PathVariable Long numberPageList, @PathVariable Long id, ModelMap model){
        model.addAttribute("numberPageList", numberPageList);
        model.addAttribute("transmitter", transmitterService.getById(id));
        model.addAttribute("statusList", transmitterStatusService.getAll());
        return "transmitter/setup";
    }

    @PostMapping("/list/{numberPageList}/{id}/setup/status")
    public String setupById(@PathVariable Long numberPageList, @PathVariable Long id
            , @RequestParam long statusSelect){
        transmitterService.setStatus(id, statusSelect);
        return "redirect:/transmitter/list/"+numberPageList+"/"+id+"/setup";
    }
}
