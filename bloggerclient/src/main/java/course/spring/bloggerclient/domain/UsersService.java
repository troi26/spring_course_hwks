package course.spring.bloggerclient.domain;

import course.spring.bloggerclient.model.User;

import java.util.List;

public interface UsersService {
    List<User> findAll();
    User findById(String userId);
    User findByEmail(String email);
    User add(User user);
    User update(User user);
    User remove(String userId);
    long count();
}
