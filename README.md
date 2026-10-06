# NumisPerú - Gestor & Catálogo Numismático 🇵🇪

![Versión](https://img.shields.io/badge/Versi%C3%B3n-0.1.2-gold?style=flat-style)
![Plataforma](https://img.shields.io/badge/Plataforma-Android-green?style=flat-style)
![Licencia](https://img.shields.io/badge/Licencia-Open%20Source-blue?style=flat-style)

**NumisPerú** es una aplicación Android nativa diseñada especialmente para coleccionistas numismáticos de monedas peruanas. Permite llevar un control exhaustivo, digital y ordenado de colecciones, series conmemorativas, monedas históricas, grados de conservación, cantidades y copias de seguridad.

---

## 🌟 Características Principales

### 1. 📚 Catálogo Especializado de Monedas del Perú
Organiza el catálogo de monedas peruanas por categorías y series históricas/modernas:
* **Series Conmemorativas de 1 Sol**:
  * *Riqueza y Orgullo del Perú* (26 monedas: Tumi de Oro, Sarcófago de Karajía, Estela de Raimondi, Machu Picchu, Gran Pajatén, etc.).
  * *Recursos Naturales del Perú* (3 monedas: La Anchoveta, El Cacao, La Quinua).
  * *Fauna Silvestre Amenazada del Perú* (10 monedas: Oso Andino, Cóndor, Tapir, Suri, Jaguar, Rana Gigante, etc.).
  * *Constructores de la República - Bicentenario* (9 monedas: Viscardo y Guzmán, Unanue, Rodríguez de Mendoza, Micaela Bastidas, etc.).
  * *La Mujer en el Proceso de Independencia* (3 monedas: Heroínas Toledo, Brígida Silva, María Parado de Bellido).
  * *Aniversarios BCRP y Batallas de Junín y Ayacucho* (Monedas conmemorativas BCRP 2015-2026).
* **Circulación Regular - Soles Modernos (1991 - Presente)**:
  * *1 Sol Regular*: Ordenadas por año y variantes de acuñación (variante Braille 1991, Firma 1994, BCRP).
  * *2 y 5 Soles Bimetálicas*: Diseños de los Colibríes y Guacamayos de las Líneas de Nasca y el Ave Fragata.
  * *Centavos*: Fracciones de 1, 5, 10, 20 y 50 Centavos de Sol.
* **Soles Históricos & Unidades Anteriores**:
  * *Soles de Oro (1930 - 1985)*: Denominaciones de 1/2 Sol de Oro a 100 Soles de Oro.
  * *Intis (1985 - 1991)*: De 1/2 Inti a 500 Intis.
  * *Monedas Históricas de Plata y República (1863 - 1935)*: Monedas de 1 Sol Plata ("Firme y Feliz por la Unión"), Dinero, Quinto, Real y cecas históricas (Lima/Lma, Cuzco, Arequipa, Pasco).

---

### 2. 🔍 Control Individual de Monedas (`CoinSlot`)
Cada ficha de moneda almacena detalles técnicos y numismáticos personalizados:
* **Cantidad y Duplicados (`quantity`)**: Permite indicar cuántos ejemplares posee el usuario de cada moneda (1, 2, 5, 10, etc.) con insignias visuales destacadas (`x1`, `x3`, `x12`).
* **Estado de Posesión (`isOwned`)**: Marcado rápido de posesión con colores distintivos (verde tenue numismático para poseídas y gris para faltantes).
* **Calificación Numismática (Escala Sheldon / Estándar)**:
  * *Poor (P-1)* / *Fair (FR-2)* / *Good (G-4)* / *Very Good (VG-8)*
  * *Fine (F-12)* / *Very Fine (VF-20)* / *Extremely Fine (XF-40)*
  * *About Uncirculated (AU-50)*
  * *Mint State (MS-60 a MS-65 Gem Unc)*
  * *PR-65 / Proof (Acuñación Prueba BCRP)*
* **Tipo de Acabado / Edición**: Circulación Regular, Brillante Sin Circular (BU), Proof / Prueba BCRP, Colorizada / Esmaltada, Blister / Estuche Oficial BCRP.
* **Notas Personales**: Espacio para registrar precio pagado, tienda o feria numismática, procedencia y detalles de conservación.

---

### 3. 🎨 Creador de Colecciones Personalizadas
* Permite crear **álbumes a medida** ingresando el nombre, categoría, rango de años (`startYear` y `endYear`), ceca y descripción.
* Genera casillas individuales para cada año del rango especificado.

---

### 4. 🔀 Reordenamiento de Álbumes
* Interfaz interactiva que permite cambiar la posición y el orden de visualización de los álbumes en la pantalla principal mediante botones de desplazamiento arriba/abajo.

---

### 5. 📊 Estadísticas Globales y Filtros Inteligentes
* **Panel de Resumen Global**: Muestra el total de monedas únicas poseídas, la cantidad acumulada de ejemplares y la barra de progreso de avance global.
* **Filtros por Categoría**: Filtro en la pantalla principal (*Todas*, *Series Conmemorativas*, *Soles Modernos*, *Soles Históricos*, *Históricas & Plata*).
* **Filtros por Álbum**: Opciones dentro del álbum (*Ver Todas*, *Ver Solo Poseídas*, *Ver Faltantes*).

---

### 6. 💾 Copia de Seguridad e Importación / Exportación
* Exporta e importa toda tu colección en formato JSON (`numisperu_backup.json`).
* Conserva notas personales, calificaciones, cantidades y álbumes personalizados sin pérdida de datos.

---

## 🛠️ Tecnología y Arquitectura

* **Lenguaje**: Java Nativo
* **Plataforma**: Android SDK (Min SDK 16 / Target SDK 35)
* **Base de Datos**: SQLite Local (`numisperu.db`)
* **Interfaz de Usuario**: Material Components (`Theme.MaterialComponents.DayNight.NoActionBar`), RecyclerView, CardView, CoordinatorLayout.

---

## 📱 Información del Repositorio

* **Versión Actual**: `0.1.2`
* **Desarrollador**: Francesco
* **Código Fuente**: [https://www.github.com/Francesco091011/com.faehpremium.numisperu](https://www.github.com/Francesco091011/com.faehpremium.numisperu)
