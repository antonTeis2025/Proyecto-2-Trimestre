#!/bin/bash

OUTPUT_FILE="project.txt"

# Crear o vaciar el archivo de salida
> "$OUTPUT_FILE"

# Contador para el mensaje final
count=0

# Buscar archivos .java y .html de forma recursiva
# Usamos -o para la condición "OR" (o uno o el otro)
find . -type f \( -name "*.java" -o -name "*.html" \) | while read -r file; do

    # Evitar procesar el archivo de salida si está en la misma carpeta
    # Eliminamos el "./" inicial para comparar si es necesario
    clean_path=$(echo "$file" | sed 's|^\./||')
    if [[ "$clean_path" == "$OUTPUT_FILE" ]]; then
        continue
    fi

    # Escribir la ruta relativa entre <>
    echo "<$file>" >> "$OUTPUT_FILE"
    echo "" >> "$OUTPUT_FILE"

    # Intentar leer el contenido del archivo
    if [ -r "$file" ]; then
        cat "$file" >> "$OUTPUT_FILE"
    else
        echo "[Error leyendo el archivo]" >> "$OUTPUT_FILE"
    fi

    # Línea en blanco adicional entre archivos
    echo -e "\n" >> "$OUTPUT_FILE"

    # Incrementar contador (esto solo funciona dentro del subshell)
    # Para un reporte preciso, contamos al final fuera del bucle
done

# Contar cuántos archivos se procesaron para el mensaje final
total_files=$(find . -type f \( -name "*.java" -o -name "*.html" \) ! -name "$OUTPUT_FILE" | wc -l)

echo "✅ Archivo '$OUTPUT_FILE' generado con $total_files archivos (Java y HTML)."