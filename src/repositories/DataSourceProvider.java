package repositories;


import org.apache.commons.dbcp2.BasicDataSource;
import configuration.Config;
import javax.sql.DataSource;

public class DataSourceProvider {
    private static BasicDataSource singleDataSource;

    public static DataSource getSingleDataSource() {
        if (DataSourceProvider.singleDataSource == null) {
            DataSourceProvider.singleDataSource = new BasicDataSource();
            DataSourceProvider.singleDataSource.setUrl(Config.url());
            DataSourceProvider.singleDataSource.setUsername(Config.dbUser);
            DataSourceProvider.singleDataSource.setPassword(Config.dbPassword);
            DataSourceProvider.singleDataSource.setInitialSize(5);

        }
        return DataSourceProvider.singleDataSource;
    }
}
