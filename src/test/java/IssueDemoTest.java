import com.atlassian.oai.validator.OpenApiInteractionValidator;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueDemoTest {

    @Test
    public void willFail() throws NoSuchFieldException, IllegalAccessException {
        OpenApiInteractionValidator.Builder builder =
                OpenApiInteractionValidator.createFor("example-openapi.yaml");
        builder.build();

        Field apiField = builder.getClass().getDeclaredField("api");
        apiField.setAccessible(true);
        OpenAPI api = (OpenAPI) apiField.get(builder);

        assertEquals("POST example", api.getPaths()
                .get("/example")
                .getPost()
                .getRequestBody()
                .getContent()
                .get("application/json")
                .getSchema()
                .getTitle());
    }

}
