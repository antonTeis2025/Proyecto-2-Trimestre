# Nombre del archivo de salida
$outputFile = "project.txt"
$projectFolder = "C:\Users\conguchu\Documents\Gestor de incidencias\"

# Obtener todos los archivos .java en la carpeta actual
$javaFiles = Get-ChildItem -Path . -Filter *.java -File -Recurse

# Crear o sobrescribir el archivo de salida
Set-Content -Path $outputFile -Value "" -Encoding UTF8

# Procesar cada archivo .java
foreach ($file in $javaFiles) {
    # Escribir el nombre del archivo
    Add-Content -Path $outputFile -Value "<$($file.Name)>" -Encoding UTF8

    # Línea en blanco
    Add-Content -Path $outputFile -Value "" -Encoding UTF8

    # Leer y escribir el contenido del archivo
    $content = Get-Content -Path $file.FullName -Raw
    Add-Content -Path $outputFile -Value $content -Encoding UTF8

    # Opcional: otra línea en blanco entre archivos para mejor legibilidad
    Add-Content -Path $outputFile -Value "" -Encoding UTF8
}

Write-Host "✅ Archivo '$outputFile' generado con $($javaFiles.Count) archivos .java."