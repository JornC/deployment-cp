docker rm extract-test
docker create --name extract-test test
docker cp extract-test:artifact.war artifact.war
docker-compose up
