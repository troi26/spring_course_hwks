package course.spring.bloggerrest.web;

import course.spring.bloggerrest.domain.PostsService;
import course.spring.bloggerrest.exception.InvalidEntityException;
import course.spring.bloggerrest.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {
    @Autowired
    PostsService service;

    @GetMapping
    List<Post> getPosts() {
        return service.findAll();
    }

    @GetMapping("{postId}")
    Post getPostById(@PathVariable("postId") String postId) {
        return service.findById(postId);
    }

    @PostMapping
    ResponseEntity<Post> addPost(@Valid @RequestBody Post post, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .map(err -> String.format("Invalid %s -> %s\n",
                            err.getField(), err.getDefaultMessage()))
                    .reduce("", (acc, err) -> acc + err);

            throw new InvalidEntityException(message);
        }
        Post created = service.add(post);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .pathSegment("{id}").build(created.getId())).body(created);
    }

    @PutMapping("{postId}")
    public Post update (@PathVariable String postId, @Valid @RequestBody Post post, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .map(err -> String.format("Invalid %s -> %s\n",
                            err.getField(), err.getDefaultMessage()))
                    .reduce("", (acc, err) -> acc + err);

            throw new InvalidEntityException(message);
        }
        if (!postId.equals(post.getId())) {
            throw new InvalidEntityException(
                    String.format("Entity ID=\"%s\" is different from URL resource ID\"%s\"",
                            post.getId(), postId));
        }
        return service.update(post);
    }

    @DeleteMapping("{postId}")
    public Post delete (@PathVariable String postId) {
        return service.remove(postId);
    }

}
