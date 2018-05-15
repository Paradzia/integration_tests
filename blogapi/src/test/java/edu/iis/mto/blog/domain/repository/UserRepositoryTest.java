package edu.iis.mto.blog.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setEmail("johnAlpha@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        List<User> users = repository.findAll();
                //Przed uruchomieniem testu tworzymy nowego uzytkownika, wiec albo na poczatku czyscimy cala liste,
                // albo uwzgledniamy go i nowe repozytorium powinno miec wtedy jednego, a nie zero userow.
                //2018-05-15 16:49:18.741  INFO 10788 --- [           main] org.hibernate.tool.hbm2ddl.SchemaExport  : HHH000476: Executing import script '/import.sql'
       // System.out.println(users.get(0).getEmail());
        Assert.assertThat(users, Matchers.hasSize(1));
        //Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(2));
        Assert.assertThat(users.get(1).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }


    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindTwoUsers(){
        User jan = new User();
        jan.setFirstName("Jan");
        jan.setEmail("JanAlpha@domain.com");
        jan.setAccountStatus(AccountStatus.NEW);

        entityManager.persist(user);
        entityManager.persist(jan);
        List<User> users = repository.findAll();
        List<User> testers = new ArrayList<>();

        for(User user : users){
            if(user.getFirstName().equalsIgnoreCase("jan")){
                testers.add(user);
            }
        }
        Assert.assertThat(testers.size(),Matchers.is(2));
    }

    @Test
    public void shouldNotFindAnyUser(){
        entityManager.persist(user);
        List<User> users = repository.findAll();
        List<User> testers = new ArrayList<>();
        for(User user : users){
            if(user.getEmail().equalsIgnoreCase("testowyuser01@domena.ork")){
                testers.add(user);
            }
        }
        Assert.assertThat(testers.size(),Matchers.is(0));
    }

}
