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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
@Slf4j
public class PostsController {
    @Autowired
    private PostsService service;

    @GetMapping
    public String getPosts(Model model,
                             Authentication auth) {
        model.addAttribute("path", "posts");
        model.addAttribute("posts", service.findAll().stream()
                .sorted((p1, p2) -> -p1.getPublicityTime().compareTo(p2.getPublicityTime()))
                .limit(15)
                .collect(Collectors.toList()));
        User logged = (User) auth.getPrincipal();
        model.addAttribute("logged", logged);

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
        return "redirect:/posts/post-form?mode=edit&post=" + postId;
    }

    @GetMapping("/post-form")
    public ModelAndView getPostForm (
            @ModelAttribute("post") Post post,
            @RequestParam(value = "mode", required = false) String mode,
            @RequestParam(value = "post", required = false) String postId,
            Authentication auth) {
        String title = "Add new post";
        ModelAndView result = new ModelAndView("post-form");
        User logged = (User) auth.getPrincipal();
        result.addObject("logged", logged);

        if ("edit".equals(mode)) {
            title = "Edit post";
            Post editPost = service.findById(postId);
            result.addObject("title", title);
            result.addObject("path", MvcUriComponentsBuilder
                    .fromMethodName(PostsController.class,
                            "getPostForm",
                            editPost, "", "", auth).build().getPath());
        } else {
            result.addObject("title", title);
            result.addObject("path", MvcUriComponentsBuilder
                    .fromMethodName(PostsController.class,
                            "getPostForm",
                            new Post(), "", "", auth).build().getPath());

        }
//        Log.info("PATH: );
        return result;
    }


    @PostMapping("/post-form")
    public String addPost(@Valid @ModelAttribute("post") Post post,
                             BindingResult errors,
                             @RequestParam("file") MultipartFile file,
                             Model model,
                             Authentication auth) {
        User logged = (User) auth.getPrincipal();
        model.addAttribute("logged", logged);
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
}
