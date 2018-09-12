package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository likePostRepository;

    private User user;
    private LikePost likePost;
    private BlogPost blogPost;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.CONFIRMED);

        entityManager.persist(user);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("test");
        entityManager.persist(blogPost);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

    }
}
