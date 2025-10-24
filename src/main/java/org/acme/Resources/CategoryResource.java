package org.acme.Resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.Dto.Requests.CategoryRequest;
import org.acme.Services.impl.CategoryServiceImpl;

import java.util.UUID;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {
    @Inject
    CategoryServiceImpl categoryService;

    @GET
    public Response list(@QueryParam("name") String name) {
        return categoryService.list(name);
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        return categoryService.getById(id);
    }

    @POST
    public Response create(CategoryRequest categoryRequest) {
        return categoryService.create(categoryRequest);
    }

    @PATCH
    @Path("/{id}")
    public Response patch(@PathParam("id") UUID id , CategoryRequest categoryRequest) {
        return categoryService.patch(id , categoryRequest);
    }
}
