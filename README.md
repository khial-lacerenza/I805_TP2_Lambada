# TP Compilation : Génération d'arbres abstraits
### KHIAL Omar & LACERENZA Loris 

# Rendu du TP1-2 :
## Après analyse le programme nous retourne :
### Run Exercice 1 :
``java -cp .\build\libs\I805_TP2_Lambada.jar fr.usmb.m1isc.compilation.tp.Main .\test.txt``
#### Resultat : 
```
(; 
    (let prixTtc 
        (/ 
            (* prixHt 119 )
            100 
        )
    )
    (+ prixTtc 100 )
)
```

### Run Exercice 2 :
`java -cp .\build\libs\I805_TP2_Lambada.jar fr.usmb.m1isc.compilation.tp.Main .\test2.txt `
#### Resultat:
```
(; 
    (let a input )
    (; 
        (let b input )
        (; 
            (while (< 0 b )
                (; 
                    (let aux (mod a b ))
                    (; 
                        (let a b )
                        (let b aux )
                    )
                )
            )
        output a 
        )
    )
)
```

# Rendu du TP3-4 :
Comparativement au TP1-2, la classe Arbre du TP3-4 à un nouvel attribut Type nous permettant de définir les types des caractères que nous rencontrons.
<br>
Nous avons aussi une nouvelle classe ParserAssembler qui va s'occuper de lire notre arbre afin d'écrit notre fichier asm à l'aide la classe WriterAsm.
<br>
La quasi totalité des fonctionnalité sont là, sauf le not qui est écrit mais ne fonctionne pas et le Nil qui n'est pas implementé.
<br>

Le "logiciel" générera automatiquement le fichier asm à partir d'un fichier .txt, à lancer à l'aide de la VM fourni. Le fichier asm ne se supprime pas lorsque le txt est modifié, il faudra penser à supprimer son contenu avant.

## Exemple : 

### Exercice 1 : 

* Contenu du .txt :

```
let prixHt = 200;
let prixTtc =  prixHt * 119 / 100 .
```

* Résutat du .asm :

```asm
DATA SEGMENT
prixHt DD
prixTtc DD
DATA ENDS
CODE SEGMENT
	mov eax, 200
	mov prixHt, eax
	mov eax, prixHt
	push eax
	mov eax, 119
	pop ebx
	mul eax, ebx
	push eax
	mov eax, 100
	pop ebx
	div ebx, eax
	mov eax, ebx
	mov prixTtc, eax
CODE ENDS
```

### Exercice 2 : 

* Contenu du .txt :

```
let a = input;
let b = input;
while (0 < b)
do (let aux=(a mod b); let a=b; let b=aux );
output a
.
```

* Résutat du .asm :

```asm
DATA SEGMENT
a DD
b DD
aux DD
DATA ENDS
CODE SEGMENT
	in eax
	mov a, eax
	in eax
	mov b, eax
debut_while_1:
	mov eax, 0
	push eax
	mov eax, b
	pop ebx
	sub eax, ebx
	jle faux_gt_1
	mov eax, 1
	jmp sortie_gt_1
faux_gt_1:
	mov eax, 0
sortie_gt_1:
	jz sortie_while_1
	mov eax, a
	push eax
	mov eax, b
	pop ebx
	mov ecx, ebx
	div ebx, eax
	mul eax, ebx
	sub ecx, eax
	mov eax, ecx
	mov aux, eax
	mov eax, b
	mov a, eax
	mov eax, aux
	mov b, eax
	jmp debut_while_1
sortie_while_1:
	mov eax, a
	out eax
CODE ENDS
```

### Exercice if et and : 

* Contenu du .txt :

```
if ( (1 = 12) AND (1=1))
then let res = 1
else let res = 0;
output res.
```

* Résutat du .asm :

```asm
DATA SEGMENT
res DD
DATA ENDS
CODE SEGMENT
	mov eax, 1
	push eax
	mov eax, 12
	pop ebx
	sub eax, ebx
	jnz faux_equal_3
	mov eax, 1
	jmp sortie_equal_3
faux_equal_3:
	mov eax, 0
sortie_equal_3:
	jz sortie_and_1
	mov eax, 1
	push eax
	mov eax, 1
	pop ebx
	sub eax, ebx
	jnz faux_equal_4
	mov eax, 1
	jmp sortie_equal_4
faux_equal_4:
	mov eax, 0
sortie_equal_4:
sortie_and_1:
	jz else_1
	mov eax, 1
	mov res, eax
	jmp sortie_if_1
else_1:
	mov eax, 1
	push eax
	mov eax, 1
	pop ebx
	sub eax, ebx
	jnz faux_equal_5
	mov eax, 1
	jmp sortie_equal_5
faux_equal_5:
	mov eax, 0
sortie_equal_5:
sortie_if_1:
	mov eax, res
	out eax
CODE ENDS
```

### Exercice if et OR : 

* Contenu du .txt :

```
if ( (1 = 12) OR (1=1))
then let res = 1
else let res = 0;
output res.
```

* Résutat du .asm :

```asm
DATA SEGMENT
res DD
DATA ENDS
CODE SEGMENT
	mov eax, 1
	push eax
	mov eax, 12
	pop ebx
	sub eax, ebx
	jnz faux_equal_3
	mov eax, 1
	jmp sortie_equal_3
faux_equal_3:
	mov eax, 0
sortie_equal_3:
	jnz sortie_or_1
	mov eax, 1
	push eax
	mov eax, 1
	pop ebx
	sub eax, ebx
	jnz faux_equal_4
	mov eax, 1
	jmp sortie_equal_4
faux_equal_4:
	mov eax, 0
sortie_equal_4:
sortie_or_1:
	jz else_1
	mov eax, 1
	mov res, eax
	jmp sortie_if_1
else_1:
	mov eax, 1
	push eax
	mov eax, 1
	pop ebx
	sub eax, ebx
	jnz faux_equal_5
	mov eax, 1
	jmp sortie_equal_5
faux_equal_5:
	mov eax, 0
sortie_equal_5:
sortie_if_1:
	mov eax, res
	out eax
CODE ENDS
```

### Exercice moins unaire : 

* Contenu du txt:

```
let a = -1+2;
output a.
```

* Résultat en asm :
```
DATA SEGMENT
a DD
DATA ENDS
CODE SEGMENT
	mov eax, 1
	push eax
	mov ebx, 0
	sub ebx, eax
	mov eax, ebx
	push eax
	mov eax, 2
	pop ebx
	add eax, ebx
	mov a, eax
	mov eax, a
	out eax
CODE ENDS
```