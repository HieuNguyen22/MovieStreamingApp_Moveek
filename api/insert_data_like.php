<?php
require "dbCon.php";

$U_FID = $_POST['fidUser'];
$M_ID = $_POST['m_id'];


$query = "INSERT INTO like_table VALUES(null, '$M_ID', '$U_FID')";

if(mysqli_query($connect, $query)){
	echo "success";
}else{
	echo "error";
}

?>