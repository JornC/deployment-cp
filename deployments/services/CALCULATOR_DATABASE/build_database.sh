cd /AERIUS-II/aerius-database-build/config

ruby /AERIUS-II/aerius-database-build/bin/SyncDBData.rb \
     /AERIUS-II/aerius-database-calculator/settings.rb \
     --from-sftp --to-local

ruby /AERIUS-II/aerius-database-build/bin/Build.rb \
     /AERIUS-II/aerius-database-build/scripts/default.rb \
     /AERIUS-II/aerius-database-calculator/settings.rb \
     --database-name=calculator \
     --flags=QUICK

# Remove the build script so it won't build again when starting the container
rm /docker-entrypoint-initdb.d/z90_build_database.sh
