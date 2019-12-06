<?php
include "connect.php";

$maKH = $_POST['maKH'];
$tenKH = $_POST['tenKH'];
$matKhau = $_POST['matKhau'];
$dienThoai = $_POST['dienThoai'];
$email = $_POST['email'];
$diaChi = $_POST['diaChi'];
$gioiTinh = $_POST['gioiTinh'];

class CapNhatTaiKhoan
{
	
	function CapNhatTaiKhoan($makh, $tenkh, $matkhau, $dienthoai, $email, $diachi, $gioitinh)
	{
		$this->makh = $makh;
		$this->tenkh = $tenkh;
		$this->matkhau = $matkhau;
		$this->dienthoai = $dienthoai;
		$this->email = $email;
		$this->diachi = $diachi;
		$this->gioitinh = $gioitinh;
	}
}

$sqlU = "UPDATE khachhang Set TenKH='$tenKH', MatKhau= '$matKhau', DienThoai='$dienThoai',
		Email='$email', DiaChi='$diaChi', GioiTinh='$gioiTinh' Where MaKH =$maKH";
$update = mysqli_query($conn, $sqlU);
if($update){
	$sql = "SELECT* From khachhang Where MaKH=$maKH";
	$data = mysqli_query($conn,$sql);
	$arrTaiKhoanCapNhat = array();
	while($row = mysqli_fetch_assoc($data)) {
		array_push($arrTaiKhoanCapNhat,new CapNhatTaiKhoan($row['MaKH'], $row['TenKH'], $row['MatKhau'], $row['DienThoai'], $row['Email'], $row['DiaChi'], $row['GioiTinh'])); 
	}
}

echo json_encode($arrTaiKhoanCapNhat);//Trả về URL mảng JSON để Android đọc
?>