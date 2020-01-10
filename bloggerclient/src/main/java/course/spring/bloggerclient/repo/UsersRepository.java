package course.spring.bloggerclient.repo;

import course.spring.bloggerclient.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsersRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
