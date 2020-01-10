package course.spring.bloggerclient.repo;

import course.spring.bloggerclient.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostsRepository extends MongoRepository<Post, String> {

}