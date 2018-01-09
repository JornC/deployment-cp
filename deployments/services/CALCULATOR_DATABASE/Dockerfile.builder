FROM test-db-builder:latest

COPY ./build_database.sh /docker-entrypoint-initdb.d/build_database.sh
CMD ["postgres"]
