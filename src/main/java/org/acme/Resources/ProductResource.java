package org.acme.Resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.Dto.Requests.ProductRequest;
import org.acme.Services.impl.ProductServiceImpl;

import java.util.UUID;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {
    @Inject
    ProductServiceImpl productService;

    @GET
    public Response list(
            @QueryParam("name") String name,
            @QueryParam("isActive") Boolean isActive
    ) {
        return productService.list(name, isActive);
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        return productService.getById(id);
    }

    @POST
    public Response create(ProductRequest productRequest) {
        return productService.create(productRequest);
    }

    @PATCH
    @Path("/{id}")
    public Response patch(UUID id , ProductRequest productRequest) {
        return productService.patch(id , productRequest);
    }
}
