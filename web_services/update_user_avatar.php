<?php
require "dbCon.php";

$U_FID = $_POST['fidUser'];
$U_AVATAR = $_POST['avatarUser'];

$query = "UPDATE user_table SET U_AVATAR = '$U_AVATAR' WHERE U_FID = '$U_FID'";

if(mysqli_query($connect, $query)){
	// thanh cong
	echo "success";
} else {
	// loi
	echo "error";
}
?>