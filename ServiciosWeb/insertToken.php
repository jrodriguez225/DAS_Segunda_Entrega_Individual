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

$token = $_POST["token"];

# Ejecutar la sentencia SQL
$resultado = mysqli_query($con, "INSERT INTO Tokens (Id) VALUES ('$token')");

# Comprobar si se ha ejecutado correctamente
if (!$resultado) {
echo 'Ha ocurrido algún error: ' . mysqli_error($con);
}
?>