package course.spring.bloggerrest.domain;

import course.spring.bloggerrest.model.Post;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PostsService {
    List<Post> findAll();
    Post findById(String postId);
    Post add(Post post);
    Post update(Post post);
    Post remove(String postId);
}
