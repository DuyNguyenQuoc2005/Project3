package k23cnt.nqdlesson03.controller;
import k23cnt.nqdlesson03.entity.NqdStudent;
import k23cnt.nqdlesson03.service.NqdServiceStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
public class NqdStudentController {
    @Autowired
    private NqdServiceStudent studentService;
    @GetMapping("/student-list")
    public List<NqdStudent> getAllStudents() {
        return studentService.getStudents();
    }
    @GetMapping("/student/{id}")
    public NqdStudent getAllStudents(@PathVariable String id)
    {
        Long param = Long.parseLong(id);
        return studentService.getStudent(param);
    }
    @PostMapping("/student-add")
    public NqdStudent addStudent(@RequestBody NqdStudent student)
    {
        return studentService.addStudent(student);
    }
    @PutMapping("/student/{id}")
    public NqdStudent updateStudent(@PathVariable String id,
                                 @RequestBody NqdStudent student) {
        Long param = Long.parseLong(id);
        return studentService.updateStudent(param,
                student);
    }
    @DeleteMapping("/student/{id}")
    public boolean deleteStudent(@PathVariable String id) {
        Long param = Long.parseLong(id);
        return studentService.deleteStudent(param);
    }
}
