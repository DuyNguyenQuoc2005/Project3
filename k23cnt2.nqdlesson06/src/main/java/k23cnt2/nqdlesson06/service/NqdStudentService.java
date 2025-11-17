package k23cnt2.nqdlesson06.service;

import k23cnt2.nqdlesson06.dto.NqdStudentDTO;
import k23cnt2.nqdlesson06.entity.NqdStudent;
import k23cnt2.nqdlesson06.repository.NqdStudentRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@NoArgsConstructor
public class NqdStudentService {
    private NqdStudentRepository studentRepository;
    @Autowired
    public NqdStudentService(NqdStudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public List<NqdStudent> findAll() {
        return studentRepository.findAll();
    }
    public Optional<NqdStudentDTO> findById(Long id) {
        NqdStudent student =
                studentRepository.findById(id).orElse(null);
        NqdStudentDTO studentDTO = new NqdStudentDTO();
        studentDTO.setId(id);
        studentDTO.setName(student.getName());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setAge(student.getAge());
        return Optional.of(studentDTO);
    }
    public Boolean save(NqdStudentDTO studentDTO) {
        NqdStudent student = new NqdStudent();
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setAge(studentDTO.getAge());
        try {
            studentRepository.save(student);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
    public NqdStudent updateStudentById(Long id, NqdStudentDTO
            updatedStudent) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(updatedStudent.getName());
                    student.setEmail(updatedStudent.getEmail());
                    student.setAge(updatedStudent.getAge());
                    return studentRepository.save(student);

                })
                .orElseThrow(() -> new
                        IllegalArgumentException("Invalid student ID: " + id));
    }
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
