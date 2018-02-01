FROM mdillon/postgis:9.6-alpine

RUN apk add --update \
    git \
    ruby \
    ruby-bundler \
    build-base \
    ruby-dev

RUN gem install net-sftp clbustos-rtf --no-rdoc --no-ri \
    && gem install net-ssh -v "=2.9.4" --no-rdoc --no-ri

# Create git workdir and move permissions to postgres user (who will run init script)
RUN mkdir AERIUS-II
RUN chown -R postgres AERIUS-II
USER postgres
WORKDIR AERIUS-II

ARG oAuthToken=local
ENV oAuthToken ${oAuthToken}

RUN git init \
    && git pull https://${oAuthToken}@github.com/aerius/AERIUS-II.git \
    && git remote add origin https://${oAuthToken}@github.com/aerius/AERIUS-II.git

ARG prId=local
ENV prId ${prId}

RUN git fetch origin pull/${prId}/head:PR-${prId} \
    && git checkout PR-${prId}

# Copy settings and chown to postgres who can otherwise not access it
COPY ./AeriusSettings.User.rb /AERIUS-II/aerius-database-build/config/AeriusSettings.User.rb
USER root
RUN chown postgres /AERIUS-II/aerius-database-build/config/AeriusSettings.User.rb

COPY ./build_database.sh /docker-entrypoint-initdb.d/z90_build_database.sh
RUN chown postgres /docker-entrypoint-initdb.d

COPY ./modify_constants.sql /docker-entrypoint-initdb.d/z91_modify_constants.sql

LABEL nl.aerius.docker.service=true
LABEL nl.aerius.docker.service.type=CALCULATOR_DATABASE
LABEL nl.aerius.docker.service.prId={{cp.pr.id}}
LABEL nl.aerius.docker.service.hash={{service.local.hash}}


RUN echo "echo BUILD_DATABASE_COMPLETE" > /docker-entrypoint-initdb.d/z99_finish.sh
