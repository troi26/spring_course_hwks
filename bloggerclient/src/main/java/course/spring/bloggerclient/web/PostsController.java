package course.spring.bloggerclient.web;

import course.spring.bloggerclient.domain.PostsService;
import course.spring.bloggerclient.model.Post;
import course.spring.bloggerclient.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/posts")
@Slf4j
public class PostsController {
    @Autowired
    private PostsService service;

//    @GetMapping
//    List<Post> getPosts() {
//        return service.findAll();
//    }
    @GetMapping
    public String getPosts(Model model) {
        model.addAttribute("path", "posts");
        model.addAttribute("posts", service.findAll());
        return "posts";
    }

    @PostMapping
    public String updatePost(
            @ModelAttribute("edit") String postId,
            @ModelAttribute("delete") String deleteId,
            Model model) {
        if (!deleteId.equals("")) {
            service.remove(deleteId);
            return "redirect:/posts";
        }

        model.addAttribute("path", "posts/post-form");
        model.addAttribute("mode", "edit");
        model.addAttribute("post", service.findById(postId));
        Post post = service.findById(postId);
        return "redirect:/posts/post-form?mode=edit&post=" + postId;
    }

    @GetMapping("/post-form")
    public ModelAndView getPostForm (
            @ModelAttribute("post") Post post,
            @RequestParam(value = "mode", required = false) String mode,
            @RequestParam(value = "post", required = false) String postId) {
        String title = "Add new post";
        ModelAndView result = new ModelAndView("post-form");
        if ("edit".equals(mode)) {
            title = "Edit post";
            Post editPost = service.findById(postId);
            result.addObject("title", title);
            result.addObject("path", MvcUriComponentsBuilder
                    .fromMethodName(PostsController.class,
                            "getPostForm",
                            editPost, "", "").build().getPath());
        } else {
            result.addObject("title", title);
            result.addObject("path", MvcUriComponentsBuilder
                    .fromMethodName(PostsController.class,
                            "getPostForm",
                            new Post(), "", "").build().getPath());

        }
//        Log.info("PATH: );
        return result;
    }


    @PostMapping("/post-form")
    public String addArticle(@Valid @ModelAttribute("post") Post post,
                             BindingResult errors,
                             @RequestParam("file") MultipartFile file,
                             Model model,
                             Authentication auth) {
        if (errors.hasErrors()) {
            model.addAttribute("fileError", null);
            return "post-form";
        } else {
            if (!file.isEmpty() && file.getOriginalFilename().length() > 0) {
                if (Pattern.matches(".+\\.(jpg|png|jpeg)", file.getOriginalFilename())) {
                    handleMultipartFile(file);
                    post.setImageUrl(file.getOriginalFilename());
                } else {
                    model.addAttribute("fileError", "Submit PNG ot JPEG picture, please");
                    return "post-form";
                }
            }
            User author = (User) auth.getPrincipal();
            if (post.getId() == null) { // Create new article
//                System.out.printf("Article: ", article);
                post.setAuthor(author);
                service.add(post);
            } else { // Update
                post.setAuthor(service.findById(post.getId()).getAuthor());
                if (file.isEmpty()) {
                    post.setImageUrl(service.findById(post.getId()).getImageUrl());
                }
                service.update(post);
            }

            return "redirect:/posts";
        }
    }

    private void handleMultipartFile(MultipartFile file) {
        String name = file.getOriginalFilename();
        long size = file.getSize();
//        log.info("File: " + name + ", Size: " + size);
        String path = name;
        try {
            File currentDir = new File("uploads");
            if(!currentDir.exists()) {
                currentDir.mkdirs();
            }
            path = currentDir.getAbsolutePath() + "/" + file.getOriginalFilename();
            path = new File(path).getAbsolutePath();
//            log.info(path);
            File f = new File(path);
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(f));
        } catch (IOException ex) {
            System.out.printf("ERROR copying file!!! %s [%d]", path, file.getSize());
        }
    }

//    @GetMapping("{postId}")
//    Post getPostById(@PathVariable("postId") String postId) {
//        return service.findById(postId);
//    }
//
//    @PostMapping
//    ResponseEntity<Post> addPost(@Valid @RequestBody Post post, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            String message = bindingResult.getFieldErrors().stream()
//                    .map(err -> String.format("Invalid %s -> %s\n",
//                            err.getField(), err.getDefaultMessage()))
//                    .reduce("", (acc, err) -> acc + err);
//
//            throw new InvalidEntityException(message);
//        }
//        Post created = service.add(post);
//        return ResponseEntity.created(
//                ServletUriComponentsBuilder.fromCurrentRequest()
//                        .pathSegment("{id}").build(created.getId())).body(created);
//    }
//
//    @PutMapping("{postId}")
//    public Post update (@PathVariable String postId, @Valid @RequestBody Post post, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            String message = bindingResult.getFieldErrors().stream()
//                    .map(err -> String.format("Invalid %s -> %s\n",
//                            err.getField(), err.getDefaultMessage()))
//                    .reduce("", (acc, err) -> acc + err);
//
//            throw new InvalidEntityException(message);
//        }
//        if (!postId.equals(post.getId())) {
//            throw new InvalidEntityException(
//                    String.format("Entity ID=\"%s\" is different from URL resource ID\"%s\"",
//                            post.getId(), postId));
//        }
//        return service.update(post);
//    }
//
//    @DeleteMapping("{postId}")
//    public Post delete (@PathVariable String postId) {
//        return service.remove(postId);
//    }

}
