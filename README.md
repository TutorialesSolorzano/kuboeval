# kuboeval
Evalucacion

# como ejecutar la evaluacion

1.- Situarse en el directorio /src
2.- Agregar persona:
    java -cp "../lib/*:."  mx.kubo.eval.main.KuboEvalRun add {\"name\":\"Alberto Vera Padrón\"}
3.- Listar personas:
    java -cp "../lib/*:."  mx.kubo.eval.main.KuboEvalRun list
4.- Buscar:
    java -cp "../lib/*:."  mx.kubo.eval.main.KuboEvalRun fuzzy-search {\"search\":\"Alver\"}
    
    
# Algoritmo de busqueda

El algoritmo que realize para solucionar esta parte fuinciona de la siguiente manera:

1.- Recorre la palabra llave a buscar caracter por caracter.
2.- Busca coincidencias entre las personas existentes.
3.- Al terminar la iteracion la persona con mas coincidencias es la que se devuelve como resultado.



