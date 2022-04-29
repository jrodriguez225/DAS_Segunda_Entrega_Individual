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

# Ejecutar la sentencia SQL
$resultado = mysqli_query($con, "SELECT * FROM Usuarios");

# Comprobar si se ha ejecutado correctamente
if (!$resultado) {
echo 'Ha ocurrido algún error: ' . mysqli_error($con);
}

$i = 0;
#Acceder al resultado
while (($fila = mysqli_fetch_row($resultado))!=null) {

# Generar el array con los resultados con la forma Atributo - Valor
$arrayresultados[$i] = array(
'nombre' => $fila[0],
'contrasena' => $fila[1],
'correo' => $fila[2],
'imagen' => $fila[3],
);
$i++;
}

#Devolver el resultado en formato JSON
echo json_encode($arrayresultados);
?>