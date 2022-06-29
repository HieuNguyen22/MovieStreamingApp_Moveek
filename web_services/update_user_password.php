<?php
require "dbCon.php";

$U_FID = $_POST['fidUser'];
$U_PASSWORD = $_POST['passwordUser'];

$query = "UPDATE user_table SET U_PASSWORD = '$U_PASSWORD'WHERE U_FID = '$U_FID'";

if(mysqli_query($connect, $query)){
	// thanh cong
	echo "success";
} else {
	// loi
	echo "error";
}
?>