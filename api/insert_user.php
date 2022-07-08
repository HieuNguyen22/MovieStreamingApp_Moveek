<?php
require "dbCon.php";

$U_FID = $_POST['fid'];
$U_EMAIL = $_POST['email'];
$U_PASSWORD = $_POST['password'];
$U_FULLNAME = $_POST['fullname'];
$U_PHONENUM = $_POST['phonenum'];
$U_AVATAR = $_POST['avatar'];
$U_BIO = $_POST['bio'];

$query = "INSERT INTO user_table VALUES(null, '$U_FID', '$U_EMAIL', '$U_PASSWORD', '$U_FULLNAME', '$U_PHONENUM', '$U_AVATAR', '$U_BIO')";

if(mysqli_query($connect, $query)){
	echo "success";
}else{
	echo "error";
}

?>