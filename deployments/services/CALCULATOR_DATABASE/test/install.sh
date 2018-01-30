docker build . -t calculator-db-empty:{{service.local.hash}} --build-arg prId={{cp.pr.id}} --build-arg oAuthToken={{cp.github.oath.token}}
docker run --name {{service.local.hash}}-builder \
           -e POSTGRES_PASSWORD=calculator \
           -e POSTGRES_USER=calculator \
           -e PGDATA=/novolumedata \
           -v "/home/jorn/data/db-data/":"/data/db-data/" \
           -d calculator-db-empty:{{service.local.hash}}

sed "/BUILD_DATABASE_COMPLETE/ q" <(docker logs -f {{service.local.hash}}-builder)

docker commit {{service.local.hash}}-builder calculator-db:{{service.local.hash}}
docker stop {{service.local.hash}}-builder
docker rm -vf {{service.local.hash}}-builder {{service.local.hash}}-empty
