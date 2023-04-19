package io.quarkiverse.doma.deployment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.seasar.doma.jdbc.Config;

@Path("/{tenant}")
public class DataSourceResolverResource {

    @Inject
    Config config;

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String name(@PathParam("id") int id) throws Exception {
        DataSource dataSource = config.getDataSource();
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("select name from employee")) {
                    if (resultSet.next()) {
                        return resultSet.getString(1);
                    }
                    throw new IllegalStateException("empty");
                }
            }
        }
    }
}
