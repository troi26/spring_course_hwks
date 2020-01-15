package course.spring.bloggerclient.init;

import course.spring.bloggerclient.domain.UsersService;
import course.spring.bloggerclient.model.User;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@CommonsLog
@Component
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UsersService usersService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        long cnt = usersService.count();
        if (cnt == 0) {
            User user = new User("Trayan", "Troev", "trayan97@gmail.com",
                    "aDmin123&");
            usersService.add(user);
        }
    }
}
