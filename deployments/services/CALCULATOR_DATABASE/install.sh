docker build . -t calculator-database-empty:{{service.local.hash}} --build-arg prId={{cp.pr.id}} --build-arg oAuthToken={{cp.github.oath.token}}
docker run --name {{service.local.hash}}-builder \
           -e POSTGRES_PASSWORD=calculator \
           -e POSTGRES_USER=calculator \
           -e PGDATA=/novolumedata \
           -v "{{cp.data.dbdata}}":"/data/db-data/" \
           -d calculator-database-empty:{{service.local.hash}}

sed "/BUILD_DATABASE_COMPLETE/ q" <(docker logs -f {{service.local.hash}}-builder)

docker commit {{service.local.hash}}-builder calculator-database:{{service.local.hash}}
docker stop {{service.local.hash}}-builder
docker rm -vf {{service.local.hash}}-builder
docker rmi calculator-database-empty:{{service.local.hash}}
