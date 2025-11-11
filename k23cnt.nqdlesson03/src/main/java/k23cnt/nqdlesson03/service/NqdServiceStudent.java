package k23cnt.nqdlesson03.service;
import k23cnt.nqdlesson03.entity.NqdStudent;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Service class: StudentService
 * <p>Lớp dịch vụ thực hiện các chức năng thao tác với List
 Object Student</p>
 *
 * @author Chung Trịnh
 * @version 1.0
 */
@Service

public class NqdServiceStudent {
    List<NqdStudent> nqdStudents = new ArrayList<NqdStudent>();

    public NqdServiceStudent(){
        nqdStudents.addAll(Arrays.asList(
                new NqdStudent(1L,"Nguyễn Quốc Duy",21,"Nam","Số 18 HM","098123000",
                        "yudnguyen@gmail.com"),
                new NqdStudent(2L, "Nguyen Van A", 22, "Nam", "Số 25 LTK", "0912345678",
                        "vana@gmail.com"),
                new NqdStudent(3L, "Tran Thi B", 21, "Nữ", "Số 12 QT", "0977654321",
                        "thib@gmail.com")

        ));
    }
    public List<NqdStudent> getStudents() {
        return nqdStudents;
    }
    public NqdStudent getStudent(Long id) {
        return nqdStudents.stream()
                .filter(student -> student
                        .getId().equals(id))
                .findFirst().orElse(null);
    }
    public NqdStudent addStudent(NqdStudent student) {
        nqdStudents.add(student);
        return student;
    }
    public NqdStudent updateStudent(Long id, NqdStudent student) {
        NqdStudent check = getStudent(id);
        if (check == null) {
            return null;
        }

        for (NqdStudent item : nqdStudents) {
            if (Objects.equals(item.getId(), id)) {
                item.setName(student.getName());
                item.setAddress(student.getAddress());
                item.setEmail(student.getEmail());
                item.setPhone(student.getPhone());
                item.setAge(student.getAge());
                item.setGender(student.getGender());
                return item;
            }
        }
        return null;
    }
    public boolean deleteStudent(Long id){
        NqdStudent check = getStudent(id);
        return nqdStudents.remove(check);
    }
}
