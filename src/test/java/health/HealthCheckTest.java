package health;

import base.BaseTest;
import constants.endpoints.Endpoint;
import org.testng.annotations.Test;

import java.util.logging.Logger;

import static config.Configuration.getEndpoint;
import static constants.endpoints.Endpoint.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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
