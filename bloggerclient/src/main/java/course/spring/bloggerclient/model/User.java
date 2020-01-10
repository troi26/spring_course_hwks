package course.spring.bloggerclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Document("users")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
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
    @Pattern(regexp = "((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]).{6,16})")
    @JsonProperty(access = WRITE_ONLY)
    private String password;
//    @Pattern(regexp = "((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]).{6,16})")
////    @Pattern(regexp = "(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
////            message = "Password must be at least 6 symbols long, containing at least one of lowercase," +
////                    " uppercase letters, digit and a special symbol")
//    private String password;
    @NonNull
    @NotNull
//    @Pattern(regexp = "ROLE_ADMIN|ROLE_BLOGGER",
//            message = "User`s role can be set only to ADMIN or BLOGGER")
    private String roles;
    private boolean active = true;
    @URL
    private String avatarUrl;

    @Override
    public String toString() {
        return String.format("%s %s\n%s", name, surname, email);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(roles.split("\\s*,\\s*")).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
