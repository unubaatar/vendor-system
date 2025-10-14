package org.acme.Services;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.ws.rs.core.Response;
import org.acme.Dto.Requests.ProductRequest;
import org.acme.Entities.Product;

import java.util.UUID;

public interface ProductService extends PanacheRepository<Product> {
    public Response list();
    public Response getById(UUID id);
    public Response create(ProductRequest productRequest);
    public Response patch(UUID id , ProductRequest productRequest);
}
