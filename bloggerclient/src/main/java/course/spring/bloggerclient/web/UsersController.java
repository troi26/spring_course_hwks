package course.spring.bloggerclient.web;

import course.spring.bloggerclient.domain.UsersService;
import course.spring.bloggerclient.exception.InvalidEntityException;
import course.spring.bloggerclient.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/users")
@Slf4j
public class UsersController {
    @Autowired
    UsersService service;

    @GetMapping
    public String getUsers(Model model,
                           Authentication auth) {
        model.addAttribute("path", "users");
        model.addAttribute("users", service.findAll());

        User logged = (User) auth.getPrincipal();
        model.addAttribute("logged", logged);
        return "users";
    }

    @PostMapping
    public String updateUser(
            @ModelAttribute("edit") String userId,
            @ModelAttribute("delete") String deleteId,
            Model model) {
        if (!deleteId.equals("")) {
            service.remove(deleteId);
            return "redirect:/users";
        }

        model.addAttribute("path", "users/user-form");
        model.addAttribute("mode", "edit");
        model.addAttribute("user", service.findById(userId));
        return "redirect:/users/user-form?mode=edit&user=" + userId;
    }
    @GetMapping("/user-form")
    public ModelAndView getUserForm (
            @ModelAttribute("user") User user,
            @RequestParam(value = "mode", required = false) String mode,
            @RequestParam(value = "user", required = false) String userId,
            Authentication auth) {
        String title = "Add new user";
        ModelAndView result = new ModelAndView("user-form");
        User logged = (User) auth.getPrincipal();
        result.addObject("logged", logged);

        if ("edit".equals(mode)) {
            title = "Edit user";
            User editUser = service.findById(userId);

            result.addObject("title", title);
            result.addObject("path", MvcUriComponentsBuilder
                    .fromMethodName(UsersController.class,
                            "getUserForm",
                            editUser, "", "", auth).build().getPath());
        } else {
            result.addObject("title", title);
            result.addObject("path", MvcUriComponentsBuilder
                    .fromMethodName(UsersController.class,
                            "getUserForm",
                            new User(), "", "", auth).build().getPath());
        }
//        Log.info("PATH: );
        return result;
    }

    @PostMapping("/user-form")
    public String addUser(@Valid @ModelAttribute("user") User user,
                             BindingResult errors,
                             @RequestParam("file") MultipartFile file,
                             Model model,
                             Authentication auth) {
        User logged = (User) auth.getPrincipal();
        model.addAttribute("logged", logged);

        System.out.println(user.getPassword());

        if (errors.hasErrors()) {
            model.addAttribute("fileError", null);
            return "user-form";
        } else {
            if (!file.isEmpty() && file.getOriginalFilename().length() > 0) {
                if (Pattern.matches(".+\\.(jpg|png|jpeg)", file.getOriginalFilename())) {
                    handleMultipartFile(file);
                    user.setAvatarUrl(file.getOriginalFilename());
                } else {
                    model.addAttribute("fileError", "Submit PNG ot JPEG picture, please");
                    return "user-form";
                }
            }

            if (user.getId() == null) { // Create new user
                service.add(user);
            } else { // user
                if (file.isEmpty()) { // Check if new file has been uploaded!!!
                    user.setAvatarUrl(service.findById(user.getId()).getAvatarUrl());
                }
                service.update(user);
            }

            return "redirect:/users";
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
