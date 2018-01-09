FROM mdillon/postgis:9.6-alpine

ARG prId=local
ENV prId ${prId}

ARG oAuthToken=local
ENV oAuthToken ${oAuthToken}

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

RUN git init \
    && git pull https://${oAuthToken}@github.com/aerius/AERIUS-II.git \
    && git remote add origin https://${oAuthToken}@github.com/aerius/AERIUS-II.git

RUN git fetch origin pull/${prId}/head:PR-${prId} \
    && git checkout PR-${prId}

# Copy settings and chown to postgres who can otherwise not access it
COPY ./AeriusSettings.User.rb /AERIUS-II/aerius-database-build/config/AeriusSettings.User.rb
USER root
RUN chown postgres /AERIUS-II/aerius-database-build/config/AeriusSettings.User.rb

COPY ./build_database.sh /docker-entrypoint-initdb.d/build_database.sh
