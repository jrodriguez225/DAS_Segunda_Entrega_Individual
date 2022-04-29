<?php
$DB_SERVER="localhost"; #la dirección del servidor
$DB_USER="Xjrodriguez225"; #el usuario para esa base de datos
$DB_PASS="*SDS0LHX9z"; #la clave para ese usuario
$DB_DATABASE="Xjrodriguez225_offerapp"; #la base de datos a la que hay que conectarse

# Se establece la conexión:
$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);

#Comprobamos conexión
if (mysqli_connect_errno($con)) {
echo 'Error de conexion: ' . mysqli_connect_error();
exit();
}

$image = $_POST["imagen"];
$usuario = $_POST["usuario"];

# Ejecutar la sentencia SQL
if ($image=='null') {
$resultado = mysqli_query($con, "UPDATE Usuarios SET Imagen = NULL WHERE Nombre = '$usuario'");
}
else {
$resultado = mysqli_query($con, "UPDATE Usuarios SET Imagen = '$image' WHERE Nombre = '$usuario'");
}

# Comprobar si se ha ejecutado correctamente
if (!$resultado) {
echo 'Ha ocurrido algún error: ' . mysqli_error($con);
}
?>