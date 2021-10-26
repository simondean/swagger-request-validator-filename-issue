import com.atlassian.oai.validator.OpenApiInteractionValidator;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueDemoTest {

    @Test
    public void swaggerWillFail() {
        OpenAPI api = loadSpecViaSwaggerLib();
        assertRequestBodyDescription(api);
    }

    @Test
    public void swaggerRequestValidatorLibWillFail() throws NoSuchFieldException, IllegalAccessException {
        OpenAPI api = loadSpecViaSwaggerRequestValidatorLib();
        assertRequestBodyDescription(api);
    }

    private OpenAPI loadSpecViaSwaggerLib() {
        final ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);
        parseOptions.setResolveCombinators(false);
        OpenAPIParser openAPIParser = new OpenAPIParser();
        SwaggerParseResult swaggerParseResult = openAPIParser.readLocation("example-openapi.yaml", List.of(), parseOptions);
        assertEquals(0, swaggerParseResult.getMessages().size());
        OpenAPI api = swaggerParseResult.getOpenAPI();
        return api;
    }

    private OpenAPI loadSpecViaSwaggerRequestValidatorLib() throws NoSuchFieldException, IllegalAccessException {
        OpenApiInteractionValidator.Builder builder =
                OpenApiInteractionValidator.createFor("example-openapi.yaml");
        builder.build();

        Field apiField = builder.getClass().getDeclaredField("api");
        apiField.setAccessible(true);
        OpenAPI api = (OpenAPI) apiField.get(builder);
        return api;
    }

    private void assertRequestBodyDescription(OpenAPI api) {
        assertEquals("POST Example", api.getPaths()
                .get("/example")
                .getPost()
                .getRequestBody()
                .getContent()
                .get("application/json")
                .getSchema()
                .getTitle());
    }

}
