echo "Building microservice micro-mgmtApi-users..."
mvn install -DskipTests
echo "Dockerizing microservice micro-mgmtApi-users..."
docker rm micro-mgmtApi-users
docker build -t micro-mgmtApi-users .
echo "Running container micro-mgmtApi-users..."
docker run --name micro-mgmtApi-users --network dev-network -p 8090:8090 micro-mgmtApi-users
