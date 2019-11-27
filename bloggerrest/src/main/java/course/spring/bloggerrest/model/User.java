package course.spring.bloggerrest.model;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Document("users")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    @NonNull
    @NotNull
    @Size(min = 1)
    private String name;
    @NonNull
    @NotNull
    @Size(min = 1)
    private String surname;
    @NonNull
    @NotNull
    @Email
    private String email;
    @NonNull
    @NotNull
    @Size(min=6)
    @Pattern(regexp = "(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
            message = "Password must be at least 6 symbols long, containing at least one of lowercase," +
                    " uppercase letters, digit and a special symbol")
    private String password;
    @NonNull
    @NotNull
    private String role = "Blogger";
    @URL
    private String avatar;
}
