FROM mdillon/postgis:10

ARG prId=local
ENV prId ${prId}

ARG oAuthToken=local
ENV oAuthToken ${oAuthToken}

ARG dbDataUser=local
ENV dbDataUser=${dbDataUser}

ARG dbDataPass=local
ENV dbDataPass=${dbDataPass}

RUN apt update \
    && apt install -y \
    git \
    maven \
    ruby \
    ftp \
    sshpass

RUN mkdir AERIUS-II
WORKDIR AERIUS-II

RUN git init \
    && git pull https://${oAuthToken}@github.com/aerius/AERIUS-II.git \
    && git remote add origin https://${oAuthToken}@github.com/aerius/AERIUS-II.git

RUN mkdir -p /db-data/aeriusII \
    && sshpass -p ${dbDataPass} sftp -oStrictHostKeyChecking=no -r ${dbDataUser}@aerius-sftp.rivm.nl:/db-data/aeriusII /db-data/aeriusII
