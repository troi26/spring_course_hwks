package course.spring.bloggerrest.domain;

import course.spring.bloggerrest.model.User;

import java.util.List;

public interface UsersService {
    List<User> findAll();
    User findById(String userId);
    User add(User user);
    User update(User user);
    User remove(String userId);
}
