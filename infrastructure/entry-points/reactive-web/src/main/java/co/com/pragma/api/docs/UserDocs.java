package co.com.pragma.api.docs;

import co.com.pragma.api.config.UserPath;
import co.com.pragma.api.dto.request.LogInDTO;
import co.com.pragma.api.dto.request.TokenDTO;
import co.com.pragma.api.dto.request.UserRequest;
import co.com.pragma.api.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static co.com.pragma.api.docs.DocsHelper.errs;
import static co.com.pragma.api.docs.DocsHelper.jsonResp;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;

@Component
public class UserDocs {

    public Consumer<Builder> save() {
        return builder -> {
            builder.operationId("saveUser").tag("User").description("Save user");
            builder.requestBody(requestBodyBuilder().required(true)
                    .content(contentBuilder().mediaType(APPLICATION_JSON_VALUE)
                            .schema(schemaBuilder().implementation(UserRequest.class))));
            jsonResp(builder, "201", UserResponse.class);
            errs(builder);
        };
    }

    public Consumer<Builder> existsByEmail() {
        return builder -> {
            builder.operationId("existsByEmail").tag("User").description("Exists user");
            builder.parameter(parameterBuilder().name("email").in(ParameterIn.PATH).required(true)
                    .schema(schemaBuilder().implementation(UserRequest.class)));
            jsonResp(builder, "200", Boolean.class);
            errs(builder);
        };
    }

    public Consumer<Builder> existsByIdNumber() {
        return builder ->{
            builder.operationId("existsByIdNumber").tag("User").description("Exists user");
            builder.parameter(parameterBuilder().name("number").in(ParameterIn.PATH).required(true)
                    .schema(schemaBuilder().implementation(UserRequest.class)));
            jsonResp(builder, "200", Boolean.class);
            errs(builder);
        };
    }

    public Consumer<Builder> login() {
        return builder -> {
            builder.operationId("login").tag("User").description("Login user");
            builder.parameter(parameterBuilder().name("email").in(ParameterIn.PATH).required(true)
                            .schema(schemaBuilder().implementation(LogInDTO.class)));
            jsonResp(builder, "200", TokenDTO.class);
            errs(builder);
        };
    }
}
