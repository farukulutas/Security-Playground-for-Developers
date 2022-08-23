<!DOCTYPE html>
<html>
  <body>
    <form action="sql.php" method="get"> Name: <input type="text" name="id">
      <br>
      <input type="submit">
    </form> <?php

	$database ="mydb";  
  	$user = "root";
  	$password = "secret";  
  	$host = "mysql-container";

	// Write your code here. START
	$connection = new PDO("mysql:host=$host;port=3306;dbname=$database;charset=utf8", $user, $password);  
        $q = "select * from persons where id=:id";
        
        $sth = $connection->prepare($q);
      	// Bind parameters to statement variables.
      	$sth->bindParam(':id', $_GET['id']);
      	// Execute statement.
      	$sth->execute();
      	// Set fetch mode to FETCH_ASSOC to return an array indexed by column name.
      	$sth->setFetchMode(PDO::FETCH_ASSOC);
      	// Fetch result.
      	$result = $sth->fetchAll();
      	/**
       	* HTML encode our result using htmlentities() to prevent stored XSS and print the
       	* result to the page
       	*/
       	foreach($result as $row) {
        	echo $row['id'] .", ";
		echo $row['last_name'] .", ";
        	echo $row['first_name'] .", ";
        	echo $row['address'] .", ";
        	echo $row['city'];
  	}
    
    ?>
  </body>
</html>
