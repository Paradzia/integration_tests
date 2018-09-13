package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    DataMapper dataMapper;

    @Autowired
    BlogService blogService;

    @MockBean
    BlogPostRepository blogPostRepository;

    @MockBean
    LikePostRepository likePostRepository;

    private User newUser;
    private User owner;
    @Before
    public void setUp() throws Exception {
        owner = new User();
        owner.setId(1L);

        newUser = new User();
        newUser.setAccountStatus(AccountStatus.NEW);
        newUser.setId(2L);
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test(expected = DomainError.class)
    public void likingPostByNotConfirmedUserShouldThrowException(){

        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(owner);

        Mockito.when(userRepository.findOne(1L)).thenReturn(owner);
        Mockito.when(userRepository.findOne(2L)).thenReturn(newUser);
        Mockito.when(blogPostRepository.findOne(1L)).thenReturn(blogPost);

        blogService.addLikeToPost(newUser.getId(), blogPost.getId());
    }
}
