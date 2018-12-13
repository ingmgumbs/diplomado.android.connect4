# Connect 4 README

-	Aplicación que emula el famoso juego de mesa "Cuatro en Línea".
-	La app tiene un solo Activity que integra toda la funcionalidad.

-	LOGICA DEL JUEGO
•	Cada jugador tiene un juego de fichas que pueden ser Amarillas o Rojas.
•	Las fichas son desplegadas en el tablero de juego colocándolas encima de la columna deseada y la ficha cae a la posición disponible
•	Para ganar el juego se debe colocar en línea contigua cuatro fichas del mismo color.
•	La disposición de las líneas para ganar puede ser Horizontal, Vertical, Diagonal a la Derecha o Diagonal a la Izquierda.
•	Al tiempo de que el jugador intenta colocar sus fichas en línea, debe evitar que el jugador contrario haga lo mismo primero.

-	 MANEJO DEL APP
•	Al cargar la aplicación se muestra el board de juego.
•	Para colocar la ficha en posición se puede hacer touch(clic) en cualquiera de las posiciones del tablero y la ficha cae en la posición disponible (de abajo hacia arriba) de la columna seleccionada.
•	Cuando uno de los jugadores logra colocar las 4 fichas en línea continua se despliega el mensaje de WIN con la ficha del color ganador. Esta acción inactiva las jugadas hasta que se resetea el juego.
•	Para Resetear el juego se presiona el botón de color verde al lado del logo del juego, o se mueve el celular de un lado a otro (shake).
•	Si hay un ganador, la acción de resetear limpia el board inmediatamente. Si no, muestra un dialogo de confirmación para resetear el juego.

-	PENDIENTE PARA LA PROXIMA VERSION
•	Actualmente la aplicación funciona en 2-Players Mode. Queda pendiente la opción de 1-Player Mode.
•	Queda pendiente para la próxima versión el registro del(los) jugadores y el Score de Juego en una DB

