docker build . -t calculator-wui:{{service.local.hash}} --build-arg prId={{cp.pr.id}} --build-arg oAuthToken={{cp.github.oath.token}}
