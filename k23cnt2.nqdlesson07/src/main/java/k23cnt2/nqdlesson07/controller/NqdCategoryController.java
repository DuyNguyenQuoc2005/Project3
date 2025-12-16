package k23cnt2.nqdlesson07.controller;

import k23cnt2.nqdlesson07.entity.NqdCategory;
import k23cnt2.nqdlesson07.service.NqdCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/category")
public class NqdCategoryController {
    @Autowired
    private NqdCategoryService categoryService;
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "category/category-list";
    }
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new NqdCategory());
        return "category/category-form";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id,
                               Model model) {
        model.addAttribute("category",
                categoryService.getCategoryById(id).orElse(null));
        return "category/category-form";
    }
    @PostMapping("/create")
    public String saveCategory(@ModelAttribute("category")
                               NqdCategory category) {
        categoryService.saveCategory(category);
        return "redirect:/category";
    }
    @PostMapping("/create/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute NqdCategory category) {
        category.setId(id);
        categoryService.saveCategory(category);
        return "redirect:/category";
    }
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/category";
    }
}
