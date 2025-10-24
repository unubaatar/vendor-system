package org.acme.Services;

import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.acme.Dto.Requests.CategoryRequest;
import org.acme.Dto.Requests.ProductRequest;

import java.util.UUID;

public interface CategoryService {
    public Response list(@QueryParam("name") String name);
    public Response getById(UUID id);
    public Response create(CategoryRequest categoryRequest);
    public Response patch(UUID id , CategoryRequest CategoryRequest);
}
