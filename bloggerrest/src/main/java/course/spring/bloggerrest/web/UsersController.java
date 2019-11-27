package course.spring.bloggerrest.web;

import course.spring.bloggerrest.domain.UsersService;
import course.spring.bloggerrest.exception.InvalidEntityException;
import course.spring.bloggerrest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    UsersService service;


    @GetMapping
    List<User> getUsers(){
        return service.findAll();
    }

    @GetMapping("{userId}")
    User getPostById(@PathVariable("userId") String postId) {
        return service.findById(postId);
    }

    @PostMapping
    ResponseEntity<User> addPost(@Valid @RequestBody User user) {
        User created = service.add(user);
        return ResponseEntity.created(MvcUriComponentsBuilder
                .fromMethodName(PostsController.class, "addUser", User.class)
                .pathSegment("{id}").build(created.getId())).body(created);
    }

    @PutMapping("{userId}")
    public User update (@PathVariable String userId, @Valid @RequestBody User user) {
//        if (errors.hasErrors()) {
//            throw new InvalidEntityException(errors.toString());
//        }
        if (!userId.equals(user.getId())) {
            throw new InvalidEntityException(
                    String.format("Entity ID=\"%s\" is different from URL resource ID\"%s\"",
                            user.getId(), userId));
        }
        return service.update(user);
    }

    @DeleteMapping("{userId}")
    public User delete (@PathVariable String userId) {
        return service.remove(userId);
    }
}
