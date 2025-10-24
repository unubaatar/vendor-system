package org.acme.Services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.Dto.Requests.CategoryRequest;
import org.acme.Dto.Responses.CategoryResponse;
import org.acme.Entities.Category;
import org.acme.Services.CategoryService;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.util.*;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {
    @Override
    public Response list(@QueryParam("name") String name) {
        try {
            StringBuilder queryStr = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            if (name != null && !name.isEmpty()) {
                queryStr.append("lower(name) like :name");
                params.put("name", "%" + name.toLowerCase() + "%");
            }
            String finalQuery = !queryStr.isEmpty() ? queryStr.toString() : "";
            List<CategoryResponse> categoryList = Category.find( finalQuery , params )
                    .list()
                    .stream()
                    .map(c-> new CategoryResponse().fromEntity( (Category) c))
                    .toList();
            return Response.ok(categoryList).build();
        } catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving products: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getById(UUID id) {
        try {
            Optional<Category> foundCategory = Category.find("id" , id).firstResultOptional();
            if(foundCategory.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Category not found").build();
            }
            Category existingCategory = foundCategory.get();
            return Response.ok(new CategoryResponse().fromEntity(existingCategory)).build();
        } catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving products: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional
    public Response create(CategoryRequest categoryRequest) {
        try {
            Category category = new Category();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());
            category.persist();
            return Response.status(Response.Status.CREATED).entity(category).build();
        } catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving products: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response patch(UUID id, CategoryRequest categoryRequest) {
        try {
            Optional<Category> foundCategory = Category.find("id" , id).firstResultOptional();
            if(foundCategory.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Category not found").build();
            }
            Category existingCategory = createCategory(categoryRequest ,foundCategory.get());
            return Response.ok(existingCategory).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving products: " + e.getMessage())
                    .build();
        }
    }

    private Category createCategory(CategoryRequest categoryRequest, Category foundCategory) {
        if (categoryRequest.getName() != null) {
            foundCategory.setName(categoryRequest.getName());
        }
        if (categoryRequest.getDescription() != null) {
            foundCategory.setDescription(categoryRequest.getDescription());
        }
        foundCategory.persist();
        return foundCategory;
    }

}
