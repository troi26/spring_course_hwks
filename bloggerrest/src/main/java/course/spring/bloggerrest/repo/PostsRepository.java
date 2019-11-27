package course.spring.bloggerrest.repo;

import course.spring.bloggerrest.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostsRepository extends MongoRepository<Post, String> {

}