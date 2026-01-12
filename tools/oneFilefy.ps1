# Nombre del archivo de salida
$outputFile = "project.txt"

# Obtener todos los archivos en la carpeta actual y subcarpetas (sin filtrar por extensión)
$allFiles = Get-ChildItem -Path . -File -Recurse

# Crear o sobrescribir el archivo de salida
Set-Content -Path $outputFile -Value "" -Encoding UTF8

# Procesar cada archivo
foreach ($file in $allFiles) {
    # Calcular la ruta relativa desde el directorio actual
    $relativePath = Resolve-Path -Path $file.FullName -Relative

    # Escribir la ruta relativa entre <>
    Add-Content -Path $outputFile -Value "<$relativePath>" -Encoding UTF8

    # Línea en blanco
    Add-Content -Path $outputFile -Value "" -Encoding UTF8

    # Leer y escribir el contenido del archivo (como texto)
    # Nota: Esto puede fallar o producir basura con archivos binarios.
    try {
        $content = Get-Content -Path $file.FullName -Raw -ErrorAction Stop
        Add-Content -Path $outputFile -Value $content -Encoding UTF8
    } catch {
        # Si no se puede leer como texto (ej. archivo binario), indicarlo
        Add-Content -Path $outputFile -Value "[Contenido binario o no legible]" -Encoding UTF8
    }

    # Línea en blanco adicional entre archivos
    Add-Content -Path $outputFile -Value "" -Encoding UTF8
}

Write-Host "✅ Archivo '$outputFile' generado con $($allFiles.Count) archivos."