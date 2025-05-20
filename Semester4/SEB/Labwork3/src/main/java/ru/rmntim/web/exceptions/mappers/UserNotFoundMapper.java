package ru.rmntim.web.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import ru.rmntim.web.dto.ErrorDTO;
import ru.rmntim.web.exceptions.UserNotFoundException;

@Provider
@Slf4j
public class UserNotFoundMapper implements ExceptionMapper<UserNotFoundException> {
    @Override
    public Response toResponse(UserNotFoundException exception) {
        log.error("User not found: {}", exception.getMessage());

        var errorResponse = ErrorDTO.of(exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
    }
}
