# trivago-light


    docker run -d -p 3306:3306    -v /tmp:/tmp  -e MYSQL_ROOT_PASSWORD=the_root_pw -e MYSQL_ROOT_HOST=172.17.0.1  -e MYSQL_USER=trivago -e  MYSQL_PASSWORD=trivago123 -e MYSQL_DATABASE=trivago  mysql/mysql-server:5.6