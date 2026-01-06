package gr.hua.dit.fittrack.web;

import org.springframework.web.bind.annotation.GetMapping;

public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index"; // Επιστρέφει index.html
    }
}
