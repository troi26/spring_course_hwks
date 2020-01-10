package course.spring.bloggerclient.init;

//import jdk.internal.jline.internal.Log;

import course.spring.bloggerclient.domain.PostsService;
import course.spring.bloggerclient.domain.UsersService;
import course.spring.bloggerclient.model.User;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@CommonsLog
@Component
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UsersService usersService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        List<Article> articles = articlesService.findAll();
//        for (Article ar : articles) {
//            articles.remove(ar);
//        }

//        if (articlesService.count() == 0) {
//            Article article = new Article("Article 1", "Article 1 content");
////            Log.info("Initial Root user: {} is being created...", article.getTitle());
//            articlesService.add(article);
//        }
        long cnt = usersService.count();
        if (cnt == 0) {
            User user = new User("Trayan", "Troev", "trayan97@gmail.com",
                    "aDmin123&", "ROLE_ADMIN");
//            log.info("Creating root admin user: {}", user.getUsername());
            usersService.add(user);
        }
    }
}
