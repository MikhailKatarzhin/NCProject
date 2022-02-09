package ncp.controller.paging;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class AbstractSecondaryPagingController extends AbstractPrimaryPagingController {

    protected abstract Long primaryPageCount();

    protected abstract String getPrimaryPrefix();

    protected abstract Long secondaryPageCount(Long id);

    protected abstract String getSecondaryPrefix();

    @Override
    protected Long pageCount() {
        return primaryPageCount();
    }

    @Override
    protected String getPrefix() {
        return getPrimaryPrefix();
    }

    @GetMapping("/secondary/{primaryId}/to_page")
    public String toSecondaryPage(@RequestParam("toPage") Long toPage, @PathVariable Long primaryId) {
        return "redirect:" + getPrimaryPrefix() + getSecondaryPrefix() + "/" + primaryId + "/list/" + toPage;
    }

    @GetMapping("/secondary/{primaryId}/first_page")
    public String firstSecondaryPage(@PathVariable Long primaryId) {
        return toSecondaryPage(1L, primaryId);
    }

    @GetMapping("/secondary/{primaryId}/list/{numberPageList}/next_page")
    public String nextSecondaryPage(@PathVariable Long primaryId, @PathVariable Long numberPageList) {
        return toSecondaryPage(numberPageList + 1L, primaryId);
    }

    @GetMapping("/secondary/{primaryId}/list/{numberPageList}/preview_page")
    public String previousSecondaryPage(@PathVariable Long primaryId, @PathVariable Long numberPageList) {
        return toSecondaryPage(numberPageList - 1L, primaryId);
    }

    @GetMapping("/secondary/{primaryId}/last_page")
    public String lastSecondaryPage(@PathVariable Long primaryId) {
        return toSecondaryPage(secondaryPageCount(primaryId), primaryId);
    }
}
