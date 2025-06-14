package deber.comentarios.exceptions;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof CustomExceptions.NotFoundException) {
            return new CustomGraphQLError(CustomErrorType.NOT_FOUND, ex.getMessage());
        } else if (ex instanceof CustomExceptions.BadRequestException) {
            return new CustomGraphQLError(CustomErrorType.BAD_REQUEST, ex.getMessage());
        } else if (ex instanceof CustomExceptions.UnauthorizedException) {
            return new CustomGraphQLError(CustomErrorType.UNAUTHORIZED, ex.getMessage());
        } else if (ex instanceof CustomExceptions.ForbiddenException) {
            return new CustomGraphQLError(CustomErrorType.FORBIDDEN, ex.getMessage());
        } else if (ex instanceof CustomExceptions.MethodNotAllowed) {
            return new CustomGraphQLError(CustomErrorType.METHOD_NOT_ALLOWED, ex.getMessage());
        } else {
            return new CustomGraphQLError(CustomErrorType.INTERNAL_SERVER_ERROR, "Error interno del servidor: " + ex.getMessage());
        }
    }

    public static class CustomGraphQLError implements GraphQLError {
        private final ErrorClassification errorType;
        private final String message;

        public CustomGraphQLError(ErrorClassification errorType, String message) {
            this.errorType = errorType;
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public List<SourceLocation> getLocations() {
            return Collections.emptyList();
        }

        @Override
        public List<Object> getPath() {
            return Collections.emptyList();
        }

        @Override
        public ErrorClassification getErrorType() {
            return errorType;  // Devuelve el tipo de error de ErrorClassification
        }
    }

    public enum CustomErrorType implements ErrorClassification {
        NOT_FOUND,
        BAD_REQUEST,
        UNAUTHORIZED,
        FORBIDDEN,
        METHOD_NOT_ALLOWED,
        INTERNAL_SERVER_ERROR;
    }
}

