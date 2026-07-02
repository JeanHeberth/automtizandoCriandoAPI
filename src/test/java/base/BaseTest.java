package base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

import static config.Configuration.getBaseUri;
import static io.restassured.RestAssured.*;

public class BaseTest {

    @BeforeClass
    public void setup() {
        baseURI = getBaseUri();
    }
}
