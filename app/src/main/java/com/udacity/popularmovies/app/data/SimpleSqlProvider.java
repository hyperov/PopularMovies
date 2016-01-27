package com.udacity.popularmovies.app.data;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

/**
 * Created by DELL I7 on 1/23/2016.
 */
@SimpleSQLConfig(
        name = "PopularProvider",
        authority = "com.udacity.popularmovies.app",
        database = "movies.db",
        version = 1)
public class SimpleSqlProvider implements ProviderConfig {
    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}
