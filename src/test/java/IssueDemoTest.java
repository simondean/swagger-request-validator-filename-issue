import com.atlassian.oai.validator.OpenApiInteractionValidator;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class IssueDemoTest {

    @Test
    public void willFail() throws NoSuchFieldException, IllegalAccessException {
        OpenApiInteractionValidator.Builder builder =
                OpenApiInteractionValidator.createFor("example-openapi.yaml");
        builder.build();

        Field apiField = builder.getClass().getDeclaredField("api");
        apiField.setAccessible(true);
        OpenAPI api = (OpenAPI) apiField.get(builder);

        assert api.getPaths()
                .get("/pears")
                .getGet()
                .getResponses()
                .get("200")
                .getContent()
                .get("application/json")
                .getSchema()
                .getTitle()
                .equals("GET Pears Response");

        assert api.getPaths()
                .get("/oranges")
                .getGet()
                .getResponses()
                .get("200")
                .getContent()
                .get("application/json")
                .getSchema()
                .getTitle()
                .equals("GET Oranges Response");
    }

}
