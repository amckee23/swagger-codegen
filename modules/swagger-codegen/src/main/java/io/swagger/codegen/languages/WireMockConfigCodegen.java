package io.swagger.codegen.languages;

import com.samskivert.mustache.Escapers;
import com.samskivert.mustache.Mustache;
import io.swagger.codegen.*;
import io.swagger.models.*;
import jdk.internal.jline.internal.Log;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.*;

import static com.samskivert.mustache.Escapers.NONE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeJson;

public class WireMockConfigCodegen extends DefaultCodegen implements CodegenConfig {
    protected String userInfoPath = "/";

    @Override
    public CodegenType getTag() {
        return CodegenType.CONFIG;
    }

    @Override
    public String getName() {
        return "wire-mock";
    }

    @Override
    public String getHelp() {
        return "Generates a Wire Mock Config file";
    }

    public WireMockConfigCodegen() {
        super();
        apiTemplateFiles.put("wire-mock.mustache", ".json");
        embeddedTemplateDir = templateDir = "wire-mock";

    }


    /**
     * Wiremock requires a single endpoint per file so use operation Id as the unique file name rather than tags
     * which are generally shared amongst multiple API's
     *
     * @param swagger
     */
    @Override
    public void preprocessSwagger(final Swagger swagger) {
        final Set<String> paths = swagger.getPaths().keySet();

        for (final String pathName : paths) {
            final Path path = swagger.getPath(pathName);
            final Set<HttpMethod> methods = path.getOperationMap().keySet();

            for (final HttpMethod httpMethod : methods) {
                final Operation operation = path.getOperationMap().get(httpMethod);
                operation.setTags(singletonList(operation.getOperationId()));
            }
        }
    }

    @Override
    public String escapeText(String input) {
        if (input == null) {
            return input;
        }

        return input;
    }

    @Override
    public String escapeUnsafeCharacters(String input) {
        return input;
    }

    @Override
    public Mustache.Compiler processCompiler(Mustache.Compiler compiler) {
        return compiler.withEscaper(NONE).nullValue("{}");
    }
}
