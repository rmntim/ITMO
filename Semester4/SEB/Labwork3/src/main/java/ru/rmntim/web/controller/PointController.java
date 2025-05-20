package ru.rmntim.web.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.ContextResolver;
import lombok.extern.slf4j.Slf4j;
import ru.rmntim.web.auth.UserPrincipal;
import ru.rmntim.web.dto.ErrorDTO;
import ru.rmntim.web.dto.PointDTO;
import ru.rmntim.web.exceptions.PointNotFoundException;
import ru.rmntim.web.service.PointService;
import ru.rmntim.web.util.MessageUtil;

@Path("/points")
@Slf4j
public class PointController {
    @Inject
    private PointService pointService;

    @Inject
    private MessageUtil messageUtil;

    @Context
    private SecurityContext securityContext;

    @Context
    private ContextResolver<String> localeResolver;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPoints() {
        String language = localeResolver.getContext(String.class);
        try {
            var points = pointService.getPoints();
            return Response.ok(points).build();
        } catch (Exception e) {
            log.error("Error while getting points {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDTO.of(messageUtil.getMessage("error.internal.server", language)))
                    .build();
        }
    }

    @GET
    @Path("/self")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPoints() {
        String language = localeResolver.getContext(String.class);
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            var points = pointService.getUserPoints(userPrincipal.getUserId());
            return Response.ok(points).build();
        } catch (Exception e) {
            log.error("Error while retrieving points for user: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDTO.of(messageUtil.getMessage("error.internal.server", language)))
                    .build();
        }
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUserPoint(PointDTO pointDTO) {
        String language = localeResolver.getContext(String.class);
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            var createdPoint = pointService.addUserPoint(userPrincipal.getUserId(), pointDTO);
            return Response.ok(createdPoint).build();
        } catch (Exception e) {
            log.error("Error adding point for user {}: {}", userPrincipal.getUserId(), e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDTO.of(messageUtil.getMessage("error.internal.server", language)))
                    .build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserPoints() {
        String language = localeResolver.getContext(String.class);
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            var userId = userPrincipal.getUserId();
            pointService.deleteUserPoints(userId);
            return Response.ok(ErrorDTO.of(messageUtil.getMessage("point.all.deleted", language))).build();
        } catch (Exception e) {
            log.error("Error deleting all points: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDTO.of(messageUtil.getMessage("error.internal.server", language)))
                    .build();
        }
    }

    @PATCH
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePoint(PointDTO pointDTO) {
        String language = localeResolver.getContext(String.class);
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            pointService.deleteSinglePoint(userPrincipal.getUserId(), pointDTO);
            return Response.ok().entity(ErrorDTO.of(messageUtil.getMessage("point.deleted", language))).build();
        } catch (PointNotFoundException e) {
            log.error("Point not found: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorDTO.of(messageUtil.getMessage("point.not.found", language)))
                    .build();
        } catch (Exception e) {
            log.error("Error deleting single point: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDTO.of(messageUtil.getMessage("error.internal.server", language)))
                    .build();
        }
    }
}
