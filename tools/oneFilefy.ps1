$outputFile = "project.txt"

# --- MODIFICACIÓN ---
# Se agregó el parámetro -Include para filtrar solo .java y .html
$allFiles = Get-ChildItem -Path . -File -Recurse -Include "*.java", "*.html"

# Crear o sobrescribir el archivo de salida
Set-Content -Path $outputFile -Value "" -Encoding UTF8

# Procesar cada archivo
foreach ($file in $allFiles) {
    # Evitar que el script se lea a sí mismo o al archivo de salida (por seguridad)
    if ($file.FullName -eq (Resolve-Path $outputFile -ErrorAction SilentlyContinue).Path) { continue }

    # Calcular la ruta relativa desde el directorio actual
    $relativePath = Resolve-Path -Path $file.FullName -Relative

    # Escribir la ruta relativa entre <>
    Add-Content -Path $outputFile -Value "<$relativePath>" -Encoding UTF8

    # Línea en blanco
    Add-Content -Path $outputFile -Value "" -Encoding UTF8

    # Leer y escribir el contenido del archivo
    try {
        $content = Get-Content -Path $file.FullName -Raw -ErrorAction Stop
        Add-Content -Path $outputFile -Value $content -Encoding UTF8
    } catch {
        Add-Content -Path $outputFile -Value "[Error leyendo el archivo]" -Encoding UTF8
    }

    # Línea en blanco adicional entre archivos
    Add-Content -Path $outputFile -Value "" -Encoding UTF8
}

Write-Host "✅ Archivo '$outputFile' generado con $($allFiles.Count) archivos (Java y HTML)."