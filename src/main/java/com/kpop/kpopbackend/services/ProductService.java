    package com.kpop.kpopbackend.services;

    import com.kpop.kpopbackend.models.Product;
    import com.kpop.kpopbackend.repository.ProductRepository;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class ProductService {

        private final ProductRepository repository;

        public ProductService(ProductRepository repository) {
            this.repository = repository;
        }

        public List<Product> getAllProducts() {
            return repository.findAll();
        }

        public Product getProductById(int id) {
            return repository.findById(id).orElse(null);
        }

        public Product addProduct(Product product) {
            return repository.save(product);
        }

        public Product updateProduct(int id, Product product) {

            Product existing = repository.findById(id).orElse(null);

            if (existing == null)
                return null;

            existing.setName(product.getName());
            existing.setDescription(product.getDescription());
            existing.setPrice(product.getPrice());
            existing.setRating(product.getRating());
            existing.setCategory(product.getCategory());
            existing.setQuantity(product.getQuantity());
            existing.setImage(product.getImage());

            return repository.save(existing);
        }

        public boolean deleteProduct(int id) {

            if (!repository.existsById(id))
                return false;

            repository.deleteById(id);

            return true;
        }

    }