package io.quarkiverse.doma.deployment;

import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Paths;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlFile;

import io.quarkiverse.doma.runtime.FileUtil;

@Path("/hot")
public class HotReplacementResource {

    public static final String SQL_FILE = "META-INF/io/quarkiverse/doma/deployment/model/MyEntityDao/select.sql";

    public static final String SCRIPT_FILE = "META-INF/io/quarkiverse/doma/deployment/model/MyEntityDao/create.script";

    @Inject
    Config config;

    @GET
    @Path("/sql")
    @Produces(MediaType.TEXT_PLAIN)
    public String sql() throws Exception {
        Method method = getClass().getMethod("sql");
        SqlFile sqlFile = config.getSqlFileRepository().getSqlFile(method, SQL_FILE, config.getDialect());
        return sqlFile.getSql();
    }

    @GET
    @Path("/script")
    @Produces(MediaType.TEXT_PLAIN)
    public String script() throws Exception {
        URL url = config.getScriptFileLoader().loadAsURL(SCRIPT_FILE);
        return FileUtil.readString(Paths.get(url.toURI()));
    }
}
