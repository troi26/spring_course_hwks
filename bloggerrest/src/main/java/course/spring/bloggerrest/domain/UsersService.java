package course.spring.bloggerrest.domain;

import course.spring.bloggerrest.model.User;

import java.util.List;

public interface UsersService {
    List<User> findAll();
    User findById(String postId);
    User add(User post);
    User update(User post);
    User remove(String postId);
}
