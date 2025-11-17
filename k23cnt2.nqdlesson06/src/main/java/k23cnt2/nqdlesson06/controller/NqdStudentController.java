package k23cnt2.nqdlesson06.controller;

import org.springframework.ui.Model;
import k23cnt2.nqdlesson06.dto.NqdStudentDTO;
import k23cnt2.nqdlesson06.entity.NqdStudent;
import k23cnt2.nqdlesson06.service.NqdStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/students")
public class NqdStudentController {
    @Autowired
    private NqdStudentService studentService;
    public NqdStudentController(NqdStudentService studentService) {
        this.studentService = studentService;
    }
    @GetMapping
    public String getStudents(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "students/student-list";
    }
    @GetMapping("/add-new")
    public String addNewStudent(Model model) {
        model.addAttribute("student", new NqdStudent());
        return "students/student-add";
    }
    @GetMapping("/edit/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long
                                            id, Model model) {
        NqdStudentDTO student =
                studentService.findById(id).orElseThrow(() -> new
                        IllegalArgumentException("Invalid student Id:" + id));
        model.addAttribute("student", student);
        return "students/student-edit";
    }
    @PostMapping
    public String saveStudent(@ModelAttribute("student") NqdStudentDTO
                                      student) {
        studentService.save(student);
        return "redirect:/students";
    }
    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable(value = "id") Long
                                        id,@ModelAttribute("student") NqdStudentDTO student) {
        studentService.updateStudentById(id,student);
        return "redirect:/students";
    }
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable(value = "id") Long id)
    {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
