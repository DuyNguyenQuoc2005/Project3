package k23cnt2.nqdproject3.controller;

import k23cnt2.nqdproject3.entity.NqdProduct;
import k23cnt2.nqdproject3.repository.NqdProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NqdHomeController {

    private final NqdProductRepository productRepository;

    @GetMapping({"/", "/home"})
    public String showHomePage(Model model) {
        List<NqdProduct> products = productRepository.findByActiveTrue();

        model.addAttribute("products", products);

        // templates/home/index.html
        return "home/index";
    }
}
