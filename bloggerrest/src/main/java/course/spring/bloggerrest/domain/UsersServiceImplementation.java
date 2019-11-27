package course.spring.bloggerrest.domain;

import course.spring.bloggerrest.exception.InvalidEntityException;
import course.spring.bloggerrest.exception.NonexisitngEntityException;
import course.spring.bloggerrest.model.User;
import course.spring.bloggerrest.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImplementation implements UsersService {
    @Autowired
    UsersRepository repo;

    @Override
    public List<User> findAll() {
        return repo.findAll();
    }

    @Override
    public User findById(String userId) {
        return repo.findById(userId).orElseThrow(() -> new NonexisitngEntityException(
                String.format("User with ID %s does not exist.", userId)));
    }

    @Override
    public User add(User user) {
        return repo.insert(user);
    }

    @Override
    public User update(User user) {
        Optional<User> old = repo.findById(user.getId());

        if (!old.isPresent()) {
            throw new InvalidEntityException(
                    String.format("User with ID=\"%s\" does not exist.", user.getId()));
        }

        return repo.save(user);
    }

    @Override
    public User remove(String userId) {
        Optional<User> target = repo.findById(userId);

        if (!target.isPresent()) {
            throw new NonexisitngEntityException(
                    String.format("User with ID=\"%s\" does not exist.", userId));
        }
        repo.deleteById(userId);

        return target.get();
    }
}
