package constants.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Endpoint {

    LOGIN("/auth/login"),
    USUARIOS("/usuarios"),
    PRODUTOS("/produtos"),
    PEDIDOS("/pedidos"),
    HEALTH_CHECK("/actuator/health");

    private final String url;


    public String byId(Integer id) {
        return url + "/" + id;
    }

    public String estoque(Integer id) {
        return url + "/" + id + "/estoque";
    }
}
