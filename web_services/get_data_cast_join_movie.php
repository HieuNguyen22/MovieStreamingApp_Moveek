<?php
require "dbCon.php";

$M_ID = $_POST['mID'];
// $M_ID = 4;

$query = "SELECT hire_table.H_ID, cast_table.C_NAME, cast_table.C_IMAGE, hire_table.H_CHAR_NAME FROM cast_table INNER JOIN hire_table ON (hire_table.M_ID = '$M_ID' AND cast_table.C_ID = hire_table.C_ID)";

$data = mysqli_query($connect, $query);

// 1. Tao class CastJoin
class CastJoin{
	public $ID;
	public $Name;
	public $Image;
	public $CharName;
	public function __construct($id, $name, $image, $charname){
		$this->ID = $id;
		$this->Name = $name;
		$this->Image = $image;
		$this->CharName = $charname;
	}
}

// 2. Tao mang
$mang = array();

// 3. Them phan tu va mang
while($row = mysqli_fetch_assoc($data)){
	array_push($mang, new CastJoin($row['H_ID'], $row['C_NAME'], $row['C_IMAGE'], $row['H_CHAR_NAME']));
}

// 4. Chuyen dinh dang cua mang -> JSON
// echo "checked";
echo json_encode($mang);
?>