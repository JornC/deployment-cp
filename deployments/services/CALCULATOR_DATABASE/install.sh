docker build . -t {{service.local.hash}} --build-arg prId={{pr.id}} --build-arg oAuthToken={{oAuth.token}}
