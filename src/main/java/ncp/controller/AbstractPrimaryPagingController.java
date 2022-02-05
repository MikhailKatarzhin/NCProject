package ncp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class AbstractPrimaryPagingController {
    protected abstract Long pageCount();
    protected abstract String getPrefix();

    @GetMapping("/to_page")
    public String toPage(@RequestParam("toPage") Long toPage){
        return "redirect:" + getPrefix() + "/list/"+toPage;
    }

    @GetMapping("/first_page")
    public String firstPage(){
        return toPage(1L);
    }

    @GetMapping("/list/{numberPageList}/next_page")
    public String nextPage(@PathVariable Long numberPageList){
        return toPage(numberPageList+1L);
    }

    @GetMapping("/list/{numberPageList}/preview_page")
    public String previewPage(@PathVariable Long numberPageList){
        return toPage(numberPageList-1L);
    }

    @GetMapping("/last_page")
    public String lastPage(){
        return toPage(pageCount());
    }
}
