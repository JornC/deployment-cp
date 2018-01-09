docker build . -t {{service.local.hash}}-empty --build-arg prId={{cp.pr.id}} --build-arg oAuthToken={{cp.oAuth.token}}
docker run --name {{service.local.hash}}-builder \
           -e POSTGRES_PASSWORD=calculator \
           -e POSTGRES_USER=calculator \
           -v "{{cp.data.dbdata}}":"/data/db-data/" \
           --user root {{service.local.hash}}-empty

docker commit {{service.local.hash}}-builder {{service.local.hash}}
docker rm -vf {{service.local.hash}}-builder {{service.local.hash}}-empty
