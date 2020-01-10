package course.spring.bloggerclient.domain;

import course.spring.bloggerclient.exception.InvalidEntityException;
import course.spring.bloggerclient.exception.NonexisitngEntityException;
import course.spring.bloggerclient.model.Post;
import course.spring.bloggerclient.repo.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostsServiceImplementation implements PostsService {
    @Autowired
    PostsRepository repo;

    @Override
    public List<Post> findAll() {
        return repo.findAll();
    }

    @Override
    public Post findById(String postId) {
        return repo.findById(postId).orElseThrow(() -> new NonexisitngEntityException(
                String.format("Post with ID %s does not exist.", postId)));
    }

    @Override
    public Post add(Post post) {
        return repo.insert(post);
    }

    @Override
    public Post update(Post post) {
        Optional<Post> old = repo.findById(post.getId());

        if (!old.isPresent()) {
            throw new InvalidEntityException(
                    String.format("Post with ID=\"%s\" does not exist.", post.getId()));
        }
        post.setPublicityTime(old.get().getPublicityTime());

        return repo.save(post);
    }

    @Override
    public Post remove(String postId) {
        Optional<Post> target = repo.findById(postId);

        if (!target.isPresent()) {
            throw new NonexisitngEntityException(
                    String.format("Post with ID=\"%s\" does not exist.", postId));
        }
        repo.deleteById(postId);

        return target.get();
    }
}
