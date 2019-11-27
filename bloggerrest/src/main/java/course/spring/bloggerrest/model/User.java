package course.spring.bloggerrest.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.net.URL;

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
    @Size(min = 5)
    private String name;
    @NonNull
    @NotNull
    @Size(min = 5)
    private String surname;
    @NonNull
    @NotNull
    @Email
    private String email;
    @NonNull
    @NotNull
    @Size(min=6)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$")
    private String password;
    @NonNull
    @NotNull
    private String role = "Blogger";
    private URL avatar;
}
