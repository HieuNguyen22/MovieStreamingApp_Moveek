<?php
require "dbCon.php";

$U_FID = $_POST['fidUser'];
$U_NAME = $_POST['nameUser'];
$U_PHONENUM = $_POST['phonenumUser'];

$query = "UPDATE user_table SET U_FULLNAME = '$U_NAME', U_PHONENUM = '$U_PHONENUM'WHERE U_FID = '$U_FID'";

if(mysqli_query($connect, $query)){
	// thanh cong
	echo "success";
} else {
	// loi
	echo "error";
}
?>