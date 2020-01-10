package course.spring.bloggerclient.domain;

import course.spring.bloggerclient.exception.InvalidEntityException;
import course.spring.bloggerclient.exception.NonexisitngEntityException;
import course.spring.bloggerclient.model.User;
import course.spring.bloggerclient.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public User findByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new NonexisitngEntityException(
                String.format("User with email='%s' does not exist.", email)));
    }

    @Override
    public User add(User user) {
        if(user.getRoles() == null || user.getRoles().trim().length() == 0) {
            user.setRoles("ROLE_AUTHOR");
        }
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(true);
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

    @Override
    public long count() {
        return repo.count();
    }
}
