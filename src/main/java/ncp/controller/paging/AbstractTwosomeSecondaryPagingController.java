package ncp.controller.paging;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class AbstractTwosomeSecondaryPagingController extends AbstractSecondaryPagingController{

    protected abstract Long primarySinglePageCount();
    protected abstract String getPrimarySinglePrefix();
    protected abstract Long firstSecondaryPageCount(Long id);
    protected abstract String getFirstSecondaryPrefix();
    protected abstract Long secondSecondaryPageCount(Long id);
    protected abstract String getSecondSecondaryPrefix();

    @Override
    protected Long primaryPageCount() {
        return primarySinglePageCount();
    }

    @Override
    protected String getPrimaryPrefix() {
        return getPrimarySinglePrefix();
    }

    @Override
    protected Long secondaryPageCount(Long id) {
        return firstSecondaryPageCount(id);
    }

    @Override
    protected String getSecondaryPrefix() {
        return getFirstSecondaryPrefix();
    }

    @GetMapping("/second_secondary/{primaryId}/to_page")
    public String toSecondSecondaryPage(@RequestParam("toPage") Long toPage, @PathVariable Long primaryId){
        return "redirect:" + getPrimaryPrefix() + getSecondSecondaryPrefix() + "/" + primaryId + "/list/"+toPage;
    }

    @GetMapping("/second_secondary/{primaryId}/first_page")
    public String firstSecondSecondaryPage(@PathVariable Long primaryId){
        return toSecondSecondaryPage(1L, primaryId);
    }

    @GetMapping("/second_secondary/{primaryId}/list/{numberPageList}/next_page")
    public String nextSecondSecondaryPage(@PathVariable Long primaryId, @PathVariable Long numberPageList){
        return toSecondSecondaryPage(numberPageList+1L, primaryId);
    }

    @GetMapping("/second_secondary/{primaryId}/list/{numberPageList}/preview_page")
    public String previousSecondSecondaryPage(@PathVariable Long primaryId, @PathVariable Long numberPageList){
        return toSecondSecondaryPage(numberPageList-1L, primaryId);
    }

    @GetMapping("/second_secondary/{primaryId}/last_page")
    public String lastSecondSecondaryPage(@PathVariable Long primaryId){
        return toSecondSecondaryPage(secondSecondaryPageCount(primaryId), primaryId);
    }
}
