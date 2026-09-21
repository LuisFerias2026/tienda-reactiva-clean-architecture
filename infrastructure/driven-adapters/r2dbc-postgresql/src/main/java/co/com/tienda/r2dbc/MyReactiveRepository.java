package co.com.tienda.r2dbc;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import co.com.tienda.r2dbc.entity.ProductData;

public interface MyReactiveRepository extends ReactiveCrudRepository<ProductData, String>, ReactiveQueryByExampleExecutor<ProductData> {

}
