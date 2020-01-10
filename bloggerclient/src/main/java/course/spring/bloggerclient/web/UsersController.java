package course.spring.bloggerclient.web;

import course.spring.bloggerclient.domain.UsersService;
import course.spring.bloggerclient.exception.InvalidEntityException;
import course.spring.bloggerclient.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
@Slf4j
public class UsersController {
    @Autowired
    UsersService service;
//
//    @GetMapping
//    List<User> getUsers(){
//        return service.findAll();
//    }
    @GetMapping
    public String getPosts(Model model) {
        model.addAttribute("path", "users");
        model.addAttribute("users", service.findAll());
        return "users";
    }

    @GetMapping("/user-form")
    public ModelAndView getUserForm (
            @ModelAttribute("user") User user,
            @RequestParam(value = "mode", required = false) String mode,
            @RequestParam(value = "user", required = false) String userId) {
        String title = "Add new user";
        if ("edit".equals(mode)) {
            title = "Edit user";
            // TODO Get article to edit
        }

        ModelAndView result = new ModelAndView("user-form");
        result.addObject("title", title);
        result.addObject("path", MvcUriComponentsBuilder
                .fromMethodName(PostsController.class,
                        "getUserForm",
                        new User(), "", "").build().getPath());
//        Log.info("PATH: );
        return result;
    }
//
//    @GetMapping("{userId}")
//    User getUserById(@PathVariable("userId") String userId) {
//        return service.findById(userId);
//    }
//
//    @PostMapping
//    ResponseEntity<User> addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            String message = bindingResult.getFieldErrors().stream()
//                    .map(err -> String.format("Invalid %s -> %s\n",
//                            err.getField(), err.getDefaultMessage()))
//                    .reduce("", (acc, err) -> acc + err);
//
//            throw new InvalidEntityException(message);
//        }
//        User created = service.add(user);
//        return ResponseEntity.created(
//                ServletUriComponentsBuilder.fromCurrentRequest()
//                        .pathSegment("{id}").build(created.getId())).body(created);
//    }
//
//    @PutMapping("{userId}")
//    public User update (@PathVariable String userId, @Valid @RequestBody User user, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            String message = bindingResult.getFieldErrors().stream()
//                    .map(err -> String.format("Invalid %s -> %s\n",
//                            err.getField(), err.getDefaultMessage()))
//                    .reduce("", (acc, err) -> acc + err);
//
//            throw new InvalidEntityException(message);
//        }
//        if (!userId.equals(user.getId())) {
//            throw new InvalidEntityException(
//                    String.format("Entity ID=\"%s\" is different from URL resource ID\"%s\"",
//                            user.getId(), userId));
//        }
//        return service.update(user);
//    }
//
//    @DeleteMapping("{userId}")
//    public User delete (@PathVariable String userId) {
//        return service.remove(userId);
//    }
}
