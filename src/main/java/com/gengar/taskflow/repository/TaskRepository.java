package com.gengar.taskflow.repository;

import com.gengar.taskflow.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TaskRepository maps domain operations to MongoDB interactions.
 * 
 * INTERNAL BEHAVIOR:
 * 1. Proxy Generation: Spring Data provides a SimpleMongoRepository proxy at runtime. 
 * 2. Query Parsing: Method names like `findByStatus` are intercepted by the PartTreeQueryParser building actual BSON queries (e.g. {"status": status})
 * 3. Exception Translation: Throws unchecked DataAccessException subclass instead of MongoException directly, abstracting vendor-specific DB errors.
 */
@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

    // Example of a derived query executed by the PartTree.
    List<Task> findByStatus(String status);
}
