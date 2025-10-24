package org.acme.Services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.acme.Dto.Requests.ProductRequest;
import org.acme.Dto.Responses.ProductResponse;
import org.acme.Entities.Product;
import org.acme.Services.ProductService;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {
    @Override
    public Response list(@QueryParam("name") String name,
                         @QueryParam("isActive") Boolean isActive) {
        try {
            StringBuilder queryStr = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            if (name != null && !name.isEmpty()) {
                queryStr.append("lower(name) like :name");
                params.put("name", "%" + name.toLowerCase() + "%");
            }

            if (isActive != null) {
                if (!queryStr.isEmpty()) queryStr.append(" and ");
                queryStr.append("isActive = :isActive");
                params.put("isActive", isActive);
            }

            String finalQuery = !queryStr.isEmpty() ? queryStr.toString() : "";

            List<ProductResponse> productList = Product.find(finalQuery, params)
                    .list()
                    .stream()
                    .map(p -> new ProductResponse().fromEntity((Product) p))
                    .collect(Collectors.toList());

            return Response.ok(productList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving products: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getById(UUID id) {
        try {
            Optional<Product> foundProduct = Product.find("id"  , id).firstResultOptional();
            if(foundProduct.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Product not found")
                        .build();
            }
            Product existingProduct = foundProduct.get();
            return Response.ok(new ProductResponse().fromEntity((Product) existingProduct)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving products: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional
    public Response create(ProductRequest productRequest) {
        try {
            Product product = new Product();
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setImage(productRequest.getImage());
            product.setPrice(productRequest.getPrice());
            product.persist();
            ProductResponse dto = new ProductResponse().fromEntity(product);
            return Response.status(Response.Status.CREATED)
                    .entity(dto)
                    .build();
        } catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error create product: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional
    public Response patch(UUID id, ProductRequest productRequest) {
        try {
            Optional<Product> foundProduct = Product.find("id"  , id).firstResultOptional();
            if(foundProduct.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Product not found")
                        .build();
            }

            Product existingProduct = createProduct(productRequest, foundProduct.get());
            ProductResponse productResponse = new ProductResponse().fromEntity(existingProduct);
            return Response.ok(productResponse).build();
        } catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error patch: " + e.getMessage())
                    .build();
        }
    }

    private Product createProduct(ProductRequest productRequest, Product foundProduct) {
        if (productRequest.getName() != null) {
            foundProduct.setName(productRequest.getName());
        }
        if (productRequest.getDescription() != null) {
            foundProduct.setDescription(productRequest.getDescription());
        }
        if (productRequest.getImage() != null) {
            foundProduct.setImage(productRequest.getImage());
        }
        if (productRequest.getPrice() != null) {
            foundProduct.setPrice(productRequest.getPrice());
        }

        if (productRequest.getActive() != null) {
            foundProduct.setActive(productRequest.getActive());
        }
        foundProduct.persist();
        return foundProduct;
    }
}
