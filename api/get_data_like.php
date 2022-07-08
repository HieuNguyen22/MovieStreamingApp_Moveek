<?php
require "dbCon.php";

// $M_ID = $_POST['mID'];
// $M_ID = 4;

$query = "SELECT * FROM like_table";

$data = mysqli_query($connect, $query);

// 1. Tao class Like
class Like{
	public $L_ID;
	public $M_ID;
	public $U_FID;
	public function __construct($l_id, $m_id, $u_fid){
		$this->L_ID = $l_id;
		$this->M_ID = $m_id;
		$this->U_FID = $u_fid;
	}
}

// 2. Tao mang
$mang = array();

// 3. Them phan tu va mang
while($row = mysqli_fetch_assoc($data)){
	array_push($mang, new Like($row['L_ID'], $row['M_ID'], $row['U_FID']));
}

// 4. Chuyen dinh dang cua mang -> JSON
// echo "checked";
echo json_encode($mang);
?>