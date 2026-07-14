package health;

import java.util.logging.Logger;

import static org.hamcrest.Matchers.equalTo;
import org.testng.annotations.Test;

import base.BaseTest;
import static constants.endpoints.Endpoint.HEALTH_CHECK;
import static io.restassured.RestAssured.given;

public class HealthCheckTest extends BaseTest {

    private static final Logger logger = Logger.getLogger(HealthCheckTest.class.getName());

@Test(description = "Deve verificar o status de saúde da API")
    public void verificarStatusSaude() {
        logger.info("Executando teste: verificarStatusSaude");

        given()
                .when()
                .get(HEALTH_CHECK.getUrl())
                .then()
                .statusCode(200)
                .body("status", equalTo("UP"));

        logger.info("Teste concluído: verificarStatusSaude");
    }









}
