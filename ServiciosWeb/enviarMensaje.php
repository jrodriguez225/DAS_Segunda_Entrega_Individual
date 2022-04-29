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
$resultado = mysqli_query($con, "SELECT * FROM Tokens");

# Comprobar si se ha ejecutado correctamente
if (!$resultado) {
echo 'Ha ocurrido algún error: ' . mysqli_error($con);
}

$registration_ids = array();
#Acceder al resultado
while (($fila = mysqli_fetch_row($resultado))!=null) {
$registration_ids[] = $fila[0];
}

$cabecera = array(
'Authorization: key=AAAAzkuRG5E:APA91bFl0-n_W3-Dv79qKeiwReoX2KmzOn5MEuUnm2Xq9w22IfTWkLDE2-qVR5KgHDPQAoAiArNcyJcy4aWghOPL9J9ku4QuLgFPd4m-EoSc0_k026xKjgBrNQvKY1Rm45NHQVTZHUWv',
'Content-Type: application/json'
);

$msg = array(
'registration_ids' => $registration_ids,
'notification' => array (
"body" => "¡No pierdas más tiempo, empieza ya!",
"title" => "¿Estás listo para ofrecer empleos o servicios?",
"icon" => "ic_stat_ic_notification"
)
);

$msgJSON = json_encode($msg);

$ch = curl_init(); #inicializar el handler de curl
#indicar el destino de la petición, el servicio FCM de google
curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
#indicar que la conexión es de tipo POST
curl_setopt($ch, CURLOPT_POST, true );
#agregar las cabeceras
curl_setopt($ch, CURLOPT_HTTPHEADER, $cabecera);
#Indicar que se desea recibir la respuesta a la conexión en forma de string
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true );
#agregar los datos de la petición en formato JSON
curl_setopt($ch, CURLOPT_POSTFIELDS, $msgJSON );
#ejecutar la llamada
$resultado = curl_exec($ch);
#cerrar el handler de curl
curl_close($ch);

if (curl_errno($ch)) {
print curl_error($ch);
}
echo $resultado;

?>