    
<?php
//---------------------------------------------------------------------
// Utilerias de Bases de Dato
// Alejandro Guzmán Zazueta
// Enero 2019
//---------------------------------------------------------------------
try{
    $Cn=new PDO("pgsql:host=127.0.0.1;port=5432;dbname=services_example;user=postgres;password=12345678");
    $Cn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    //$Cn->exec("SET CHARACTER SET utf8");
    $Cn->exec("SET CLIENT_ENCODING TO 'UTF8';");
}catch(Exception $e){
    die("Error: " . $e->GetMessage());
}
// Función para ejecutar consultas SELECT
function Consulta($query)
{
    global $Cn;
    try{    
        $result =$Cn->query($query);
        $resultado = $result->fetchAll(PDO::FETCH_ASSOC); 
        $result->closeCursor();
        return $resultado;
    }catch(Exception $e){
        die("Error en la LIN: " . $e->getLine() . ", MSG: " . $e->GetMessage());
    }
}
//Función para mandar ejecutar un INSERT,UPDATE o DELETE
function Ejecuta($sentencia){
    global $Cn;
        try{
            $result =$Cn->query($sentencia);
            $result->closeCursor();
            return 1;
        }catch(Exception $e){
            die("Error en la LIN: " . $e->getLine() . ", MSG: " . $e->GetMessage());
        }        
return $insert;
}
//Función que manda ejecutar un INSERT regresando el consucutivo
function EjecutaConsecutivo($sentencia){
    global $Cn;
        try{
            $result =$Cn->query($sentencia);
            $resultado = $result->fetchAll(PDO::FETCH_ASSOC);
            $result->closeCursor();
            return $resultado[0]["noControl"];
        }catch(Exception $e){
            //die("Error en la LIN: " . $e->getLine() . ", MSG: " . $e->GetMessage());
            return 0;
        }        
return $insert;
}
//-------------------------------------------------------------------
function addStudent($data)
{
    $noControl =  $data['noControl'];
    $nomEst    =  $data['nomEst'];
    $carrera   =  $data['carrera'];
    $edad      =  $data['edad'];
    $sentencia = "INSERT INTO estudiante(noControl,nomEst,carrera,edad) VALUES ('$noControl','$nomEst','$carrera',$edad)ON CONFLICT(noControl) DO UPDATE SET nomEst='$nomEst',carrera='$carrera',edad=$edad";
    return Ejecuta($sentencia);
}
function selectStudent(){
    $query="SELECT * from estudiante";
    return Consulta($query);
}