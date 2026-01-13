# Admin Bot Application

Выключение https в keycloak
```bash
docker exec -it {contaierID} bash
cd /opt/keycloak/bin
./kcadm.sh config credentials --server http://localhost:8080 --realm master --user admin
./kcadm.sh update realms/master -s sslRequired=NONE
```