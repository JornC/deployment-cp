echo "BUILDING DOCKER COMPOSITION"

docker-compose -p CALCULATOR-{{cp.pr.id}}-{{cp.pr.hash}} up -d

docker ps -a
echo "Replacing IPs.."

IPGeoserver=$(docker ps -a | grep calculator-geoserver:{{service.SCENARIO_BASE_GEOSERVER.hash}} | cut -d ' ' -f 1 | xargs docker inspect | grep IPAddress | tail -1 | cut -d ':' -f 2 | cut -d '"' -f 2)
IPCalculator=$(docker ps -a | grep calculator-wui:{{service.CALCULATOR_WUI.hash}} | cut -d ' ' -f 1 | xargs docker inspect | grep IPAddress | tail -1 | cut -d ':' -f 2 | cut -d '"' -f 2)

echo "IPCalculator is:"
echo $IPCalculator
echo "IPGeoserver is:"
echo $IPGeoserver

replaceCalculator="sed -i -- 's/{{host.calculator.ip}}/${IPCalculator}/g' site.conf"
replaceGeoserver="sed -i -- 's/{{host.geoserver.ip}}/${IPGeoserver}/g' site.conf"

eval $replaceCalculator
eval $replaceGeoserver

echo "Replacements complete."

cp site.conf /etc/apache2/sites-available/{{cp.pr.id}}.conf
a2dissite {{cp.pr.id}}
a2ensite {{cp.pr.id}}
sudo /etc/init.d/apache2 reload
sudo /usr/sbin/service apache2 reload

echo "Site enabled"
