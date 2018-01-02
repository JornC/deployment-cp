docker create --name extract-{{service.CALCULATOR_WUI.hash}} {{service.CALCULATOR_WUI.hash}}
docker cp extract-{{service.CALCULATOR_WUI.hash}}:artifact.war artifact.war
docker-compose up
