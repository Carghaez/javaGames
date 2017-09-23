@echo off
color 03
title Compilazione di Halma
echo Eliminazione dei vecchi file .class...
del *.class /s > nul 2>&1
:INIZIO
echo Compilazione nuovi file .class in corso...
echo Le versioni disponibili sono {0} scegli la versione da creare:
set /p v=
IF %v%==0 GOTO 0
GOTO ERR

:0
if EXIST LogV%v%.log del LOGV%v%.log
javac -cp "..\.." "v%v%/*.java" -Xlint:unchecked > "LogV%v%.log" 2>&1
echo E' stato creato il file di log
echo Premi '0' per visualizzarlo, qualsiasi altro tasto per uscire
set /p scelta=
if %scelta%==0 GOTO STAMPA
GOTO END

:STAMPA
type "LogV%v%.log"

:END
pause>nul
exit

:ERR
echo Opzione errata
pause>nul
cls
goto INIZIO