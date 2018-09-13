package edu.iis.mto.blog.rest.test;

import com.jayway.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.BeforeClass;

import com.jayway.restassured.RestAssured;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;

public class FunctionalTests {

    private static final String USER_API = "/blog/user";

    @BeforeClass
    public static void setup() {
        String port = System.getProperty("server.port");
        if (port == null) {
            RestAssured.port = Integer.valueOf(8080);
        } else {
            RestAssured.port = Integer.valueOf(port);
        }

        String basePath = System.getProperty("server.base");
        if (basePath == null) {
            basePath = "";
        }
        RestAssured.basePath = basePath;

        String baseHost = System.getProperty("server.host");
        if (baseHost == null) {
            baseHost = "http://localhost";
        }
        RestAssured.baseURI = baseHost;

    }

    @Test
    public void creatingPostWithConfirmedStatusShouldPass(){
        JSONObject jsonObject = new JSONObject().put("entry", "good");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect().log().all().statusCode(201).when().post(USER_API + "/1/post");
    }
    @Test
    public void creatingPostWithoutConfirmedStatusShouldNotPass(){
        JSONObject jsonObject = new JSONObject().put("entry", "bad");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect().log().all().statusCode(400).when().post(USER_API + "/2/post");
    }
    @Test
    public void likingYourOwnPostIsBadAndShouldNotHappen(){
        JSONObject jsonObject = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect().log().all().statusCode(400).when().post(USER_API + "/1/like/1");
    }

    @Test
    public void likingPostsMoreThanOneBySameUserShouldNotChangePostStatus() {
        JSONObject jsonObject = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(200).when().post(USER_API + "/3/like/1");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(200).when().post(USER_API + "/3/like/1");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8").when()
                .get(USER_API + "/1/post").then().body("likesCount", hasItem(1));
    }

    @Test
    public void searchingUserPostsWithRemovedStatusShouldFail(){
        JSONObject jsonObject = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect().log().all().statusCode(404).when().get(USER_API + "/blog/user/4/post");
    }
    @Test
    public void searchingPostsShouldReturnCorrectNumberOfLikes(){
        JSONObject jsonObject = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(200).when().post(USER_API + "/3/like/1");
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(200).and()
                .body("likesCount", hasItem(1)).when().get(USER_API + "/1/post").then();
    }
    @Test
    public void searchingPostsShouldReturnCorrectNumberOfLikesWhenNoOneLikesIt(){
        JSONObject jsonObject = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(200).and()
                .body("likesCount", hasItem(0)).when().get(USER_API + "/1/post");
    }

    @Test
    public void shouldReturnNothingWhenUserWithNoPostsIsProvided() {
        JSONObject jsonObject = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(200).and().body("size()", is(0))
                .when().get(USER_API + "/2/post");
    }

    @Test
    public void searchingUserWithRemovedStatusShouldFail(){
        JSONObject jsonObject = new JSONObject();
        RestAssured.given().accept(ContentType.JSON).header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString()).expect().log().all().statusCode(200).and()
                .body("size()", is(0)).when()
                .get(USER_API + "/find?searchString=konrad");
    }

}
