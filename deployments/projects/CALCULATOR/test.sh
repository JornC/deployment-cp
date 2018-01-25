IPGeoserver=$(docker ps -a | grep calculator-geoserver:test | cut -d ' ' -f 1 | xargs docker inspect | grep IPAddress | tail -1 | cut -d ':' -f 2 | cut -d '"' -f 2)
IPCalculator=$(docker ps -a | grep calculator-wui:test | cut -d ' ' -f 1 | xargs docker inspect | grep IPAddress | tail -1 | cut -d ':' -f 2 | cut -d '"' -f 2)

echo "IPCalculator is:"
echo $IPCalculator
echo "IPGeoserver is:"
echo $IPGeoserver

replaceCalculator="sed -i -- 's/{{host.calculator.ip}}/${IPCalculator}/g' site.conf"
replaceGeoserver="sed -i -- 's/{{host.geoserver.ip}}/${IPGeoserver}/g' site.conf"

eval $replaceCalculator
eval $replaceGeoserver

echo "Replacements complete."
