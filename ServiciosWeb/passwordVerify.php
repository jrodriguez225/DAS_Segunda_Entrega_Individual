<?php
$contraseña = $_POST["contraseña"];
$hash = $_POST["hash"];

if (password_verify($contraseña, $hash)) {
    echo 'true';
} else {
    echo 'false';
}
?>