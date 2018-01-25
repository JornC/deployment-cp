rm site.conf
cp site_bu.conf site.conf
a2dissite test
service apache2 reload
