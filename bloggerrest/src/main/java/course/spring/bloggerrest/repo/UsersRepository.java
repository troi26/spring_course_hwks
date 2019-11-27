package course.spring.bloggerrest.repo;

import course.spring.bloggerrest.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<User, String> {

}
