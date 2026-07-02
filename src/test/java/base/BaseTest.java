package base;

import org.testng.annotations.BeforeClass;

import static auth.TokenManager.getToken;
import static config.Configuration.getBaseUri;
import static io.restassured.RestAssured.baseURI;

public class BaseTest {

    protected String token;

    @BeforeClass
    public void setup() {
        baseURI = getBaseUri();
        token = getToken();
    }
}
