package base;

import auth.AuthService;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

import static config.Configuration.getBaseUri;
import static io.restassured.RestAssured.*;

public class BaseTest {

    protected String token;

    @BeforeClass
    public void setup() {
        baseURI = getBaseUri();
        token = AuthService.getToken();
    }
}
