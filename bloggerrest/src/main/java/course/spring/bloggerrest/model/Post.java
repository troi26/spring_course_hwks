package course.spring.bloggerrest.model;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Document("posts")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    private String id;
    @NonNull
    @NotNull
    @Size(min = 2, max = 60)
    private String title;
    @NonNull
    @NotNull
    @Size(min = 10, max = 2048)
    private String text;
    private ArrayList<String> keyWords;
    private String author;
    private LocalDateTime publicityTime = LocalDateTime.now();
    @URL
    private String imageUrl;
    @NonNull
    private String status = "active";
}
