package k23cnt2.nqdlesson06.dto;

import lombok.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NqdStudentDTO {
    private Long id;
    private String name;
    private String email;
    private int age;
}
