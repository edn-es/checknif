package rest2soap.repository;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.data.repository.GenericRepository;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.MYSQL)
public abstract class MemberRepository implements CrudRepository<Member, Long> {
    public abstract Optional<Member> findByApiKey(String apiKey);
}
