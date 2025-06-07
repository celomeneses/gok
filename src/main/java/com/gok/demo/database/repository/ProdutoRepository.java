package com.gok.demo.database.repository;

import com.gok.demo.database.entity.ProdutoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProdutoRepository extends MongoRepository<ProdutoEntity, String> {

    ProdutoEntity findByCodigo(Integer codigo);
}
