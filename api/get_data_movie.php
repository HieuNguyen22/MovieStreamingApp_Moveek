<?php
require "dbCon.php";

$query = "SELECT * FROM movie_table";

$data = mysqli_query($connect, $query);

// 1. Tao class Movie
class Movie{
	public $ID;
	public $Title;
	public $Poster;
	public $Year;
	public $Length;
	public $Type;
	public $Genre;
	public $Description;
	public $Trailer;
	public $Imdb;
	public function __construct($id, $title, $poster, $year, $length, $type, $genre, $description, $trailer, $imdb){
		$this->ID = $id;
		$this->Title = $title;
		$this->Poster = $poster;
		$this->Year = $year;
		$this->Length = $length;
		$this->Type = $type;
		$this->Genre = $genre;
		$this->Description = $description;
		$this->Trailer = $trailer;
		$this->Imdb = $imdb;
	}
}

// 2. Tao mang
$mang = array();

// 3. Them phan tu va mang
while($row = mysqli_fetch_assoc($data)){
	array_push($mang, new Movie($row['M_ID'], $row['M_TITLE'], $row['M_POSTER'], $row['M_YEAR'], $row['M_LENGTH'], $row['M_TYPE'], $row['M_GENRE'], $row['M_DESCRIPTION'], $row['M_TRAILER'], $row['M_IMDB']));
}

// 4. Chuyen dinh dang cua mang -> JSON
// echo $mangSV;
echo json_encode($mang);
?>