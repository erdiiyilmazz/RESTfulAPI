echo "Running MySql..."
docker run --restart always --name mysql8.0 --net dev-network -v C:\Users\eyilmaz\Documents\JavaWorkspace\database\mysql_db\8.0:/var/lib/mysql -p 3307:3307 -d -e MYSQL_ROOT_PASSWORD=Erdi123! mysql:8.0
