package com.gok.demo.database.repository;

import com.gok.demo.database.entity.ClienteCompraEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClienteCompraRepository extends MongoRepository<ClienteCompraEntity, String> {
}
