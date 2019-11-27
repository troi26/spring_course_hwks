package course.spring.bloggerrest.web;

import course.spring.bloggerrest.domain.UsersService;
import course.spring.bloggerrest.exception.InvalidEntityException;
import course.spring.bloggerrest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    User getUserById(@PathVariable("userId") String userId) {
        return service.findById(userId);
    }

    @PostMapping
    ResponseEntity<User> addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .map(err -> String.format("Invalid %s -> %s\n",
                            err.getField(), err.getDefaultMessage()))
                    .reduce("", (acc, err) -> acc + err);

            throw new InvalidEntityException(message);
        }
        User created = service.add(user);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .pathSegment("{id}").build(created.getId())).body(created);
    }

    @PutMapping("{userId}")
    public User update (@PathVariable String userId, @Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .map(err -> String.format("Invalid %s -> %s\n",
                            err.getField(), err.getDefaultMessage()))
                    .reduce("", (acc, err) -> acc + err);

            throw new InvalidEntityException(message);
        }
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
