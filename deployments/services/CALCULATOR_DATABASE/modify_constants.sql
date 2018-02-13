UPDATE system.constants SET value = 'http://{{cp.pr.id}}.geoserver.{{cp.deployment.host}}/geoserver-calculator/wms?' WHERE key = 'GEOSERVER_CAPIBILITIES_ORIGINAL_URL';
UPDATE system.constants SET value = 'http://{{cp.pr.id}}.geoserver.{{cp.deployment.host}}/geoserver-calculator/wms?' WHERE key = 'SHARED_WMS_URL';
UPDATE system.constants SET value = 'http://{{cp.pr.id}}.calculator.{{cp.deployment.host}}/calculator/aerius-geo-wms?' WHERE key = 'INTERNAL_WMS_PROXY_URL';
UPDATE system.constants SET value = 'http://{{cp.pr.id}}.calculator.{{cp.deployment.host}}/calculator/aerius-geo-wms?' WHERE key = 'SHARED_WMS_PROXY_URL';
UPDATE system.constants SET value = 'http://webapp:8080/calculator/aerius/' WHERE key = 'SHARED_SLD_URL';

UPDATE system.layer_capabilities SET url = 'http://{{cp.pr.id}}.calculator.{{cp.deployment.host}}/calculator/aerius-geo-wms?' WHERE layer_capabilities_id = '1';
UPDATE system.layer_capabilities SET url = 'http://{{cp.pr.id}}.geoserver.{{cp.deployment.host}}/geoserver-calculator/wms?' WHERE layer_capabilities_id = '4';
