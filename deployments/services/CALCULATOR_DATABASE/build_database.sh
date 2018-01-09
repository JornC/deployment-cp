cd /AERIUS-II/aerius-database-build/config

ruby /AERIUS-II/aerius-database-build/bin/SyncDBData.rb \
     /AERIUS-II/aerius-database-calculator/settings.rb \
     --from-sftp --to-local

ruby /AERIUS-II/aerius-database-build/bin/Build.rb \
     /AERIUS-II/aerius-database-build/scripts/test_structure.rb \
     /AERIUS-II/aerius-database-calculator/settings.rb \
     --database-name calculator

exit
