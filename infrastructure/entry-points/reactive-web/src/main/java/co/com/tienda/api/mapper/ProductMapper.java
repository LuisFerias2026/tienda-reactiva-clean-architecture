package co.com.tienda.api.mapper;

import co.com.tienda.api.dto.ProductRequest;
import co.com.tienda.model.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toProduct(ProductRequest request) {
        return Product.builder()
                .name(request.name())
                .price(request.price())
                .stock(request.stock())
                .build();
    }
}
