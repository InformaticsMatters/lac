#!/bin/sh
#
# sets up the containers ready for use.
# once comple run them with run-containers.sh

docker-compose stop
docker-compose rm -f
docker-compose build

./setenv.sh
base=$PWD

echo "Setting up for server ${SQUONK_HOST}"

echo "preaparing postgres docker image ..."
docker-compose -f docker-compose.yml -f docker-compose-setup.yml up -d postgres rabbitmq

# we need to wait for postgres to start as the next step is to populate the database
attempt=0
until nc -z $SQUONK_HOST 5432
#until docker exec -it deploy_postgres_1 psql -U squonk -c 'select version()' > /dev/null 2>&1
do
    if [ $attempt -gt 10 ]; then 
        echo "Giving up on postgres"
	    docker-compose stop
	exit 1
    fi
    echo "waiting for postgres container..."
    sleep 1
    attempt=$(( $attempt + 1 ))
done
echo "postgres is up"

# now we can start keycloak (needs postgres to be setup before it starts)
docker-compose -f docker-compose.yml -f docker-compose-setup.yml up -d keycloak

echo "creating db tables ..."
cd ../../components
SQUONK_DB_SERVER=$SQUONK_HOST
./gradlew database:flywayMigrate
echo "... tables created"
cd $base

echo "preparing rabbitmq docker image ..."
./rabbitmq-setup.sh deploy_rabbitmq_1
echo "... rabbitmq container configured"
docker-compose stop rabbitmq

keycloak_url="https://${SQUONK_HOST}:8443/auth"
echo "keycloak_url: $keycloak_url"

# substitute the realm json file
sed "s/192.168.59.103/${SQUONK_HOST}/g" squonk-realm.json > yyy.json


attempt=0
until $(curl --output /dev/null -s -k --head --fail ${keycloak_url}); do
	if [ $attempt -gt 30 ]; then 
        echo "Giving up on keycloak"
		exit 1
		fi
	attempt=$(( $attempt + 1 ))
  	echo 'waiting for keycloak container ...'
  	sleep 1
done
echo "keycloak is up"

token=$(curl -s -k -X POST "${keycloak_url}/realms/master/protocol/openid-connect/token" -H "Content-Type: application/x-www-form-urlencoded" -d "username=admin" -d "password=${KEYCLOAK_PASSWORD:-squonk}" -d "grant_type=password" -d "client_id=admin-cli" | jq -r '.access_token')
echo "token: $token"

curl -s -k -X POST -T yyy.json "${keycloak_url}/admin/realms" -H "Authorization: Bearer $token" -H "Content-Type: application/json"
echo "squonk realm added to keycloak"

docker-compose stop
echo finished