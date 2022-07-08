<?php
require "dbCon.php";

$query = "SELECT * FROM user_table";

$data = mysqli_query($connect, $query);

// 1. Tao class User
class User{
	public $ID;
	public $FID;
	public $Email;
	public $Password;
	public $Fullname;
	public $Phonenum;
	public $Avatar;
	public $Bio;
	public function __construct($id, $fid, $email, $password, $fullname, $phonenum, $avatar, $bio){
		$this->ID = $id;
		$this->FID = $fid;
		$this->Email = $email;
		$this->Password = $password;
		$this->Fullname = $fullname;
		$this->Phonenum = $phonenum;
		$this->Avatar = $avatar;
		$this->Bio = $bio;
	}
}

// 2. Tao mang
$mang = array();

// 3. Them phan tu va mang
while($row = mysqli_fetch_assoc($data)){
	array_push($mang, new User($row['U_ID'], $row['U_FID'], $row['U_EMAIL'], $row['U_PASSWORD'], $row['U_FULLNAME'], $row['U_PHONENUM'], $row['U_AVATAR'], $row['U_BIO']));
}

// 4. Chuyen dinh dang cua mang -> JSON
// echo $mangSV;
echo json_encode($mang);
?>