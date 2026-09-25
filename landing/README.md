# Landing FixCampus

HTML, CSS y JavaScript sin framework ni proceso de compilación. Ilustración SVG local incluida en el HTML; sin dependencia de imágenes o fuentes externas.

```bash
python -m http.server 5500 --bind 127.0.0.1
```

Ejecutar desde esta carpeta y abrir http://localhost:5500. También puede abrirse `index.html`, aunque se recomienda servidor HTTP para probar correctamente.

Editar `config.js` para apuntar al frontend desplegado. Por defecto todos los llamados a la acción abren http://localhost:4200. El selector ES/EN traduce textos, título y etiquetas accesibles; persiste es_419/en_US en localStorage. Privacidad y términos usan el elemento nativo `dialog`, con cierre por botón o Escape.

La relación con ODS 11 y meta 11.7 es orientativa para espacios comunes del campus, no una certificación ni una medición del indicador oficial. Los textos de privacidad y términos describen el prototipo académico; para producción se debe completar responsable institucional, retención y canales de atención.
