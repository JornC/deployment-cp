docker build . -t calculator-geoserver:{{service.local.hash}} --build-arg prId={{cp.pr.id}} --build-arg oAuthToken={{cp.github.oath.token}} --build-arg geoserverPassword={{cp.geoserver.password}}
