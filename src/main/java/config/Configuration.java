package config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class Configuration {
    private static final Map<String, Object> config;

    static {
        Yaml yaml = new Yaml();

        InputStream inputStream = Configuration.class
                .getClassLoader()
                .getResourceAsStream("data.yaml");

        if (inputStream == null) {
            throw new RuntimeException("Arquivo data.yaml não encontrado no classpath.");
        }

        config = yaml.load(inputStream);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getSection(String section){
        return (Map<String, Object>) config.get(section);
    }

    @SuppressWarnings("unchecked")
    public static String getBaseUri(){
        return (String) getSection("api")
                .get("baseUri");
    }

    @SuppressWarnings("unchecked")
    public static String getEndpoint(String endpointName){
        Map<String, Object> api = getSection("api");

        Map<String, Object> endpoints = (Map<String, Object>) api.get("endpoints");
        return (String) endpoints.get(endpointName);
    }

    public static String getUsuario(){
        return (String) getSection("auth")
                .get("usuario");
    }

    public static String getSenha(){
        return (String) getSection("auth")
                .get("senha");
    }
}
