<?php
require "dbCon.php";

$U_FID = $_POST['fidUser'];
$M_ID = $_POST['m_id'];


$query = "DELETE FROM like_table WHERE M_ID = '$M_ID' AND U_FID = '$U_FID'";

if(mysqli_query($connect, $query)){
	echo "success";
}else{
	echo "error";
}

?>