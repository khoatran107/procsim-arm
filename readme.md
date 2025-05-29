Build:
```
javac -cp xercesImpl-2.12.2.jar -d . defpackage/*.java
jar cvf myprogram.jar defpackage/
```

Run (linux):
```
java -cp "myprogram.jar:xercesImpl-2.12.2.jar" defpackage.ProcSim
```

Run (windows):
```
java -cp "myprogram.jar;xercesImpl-2.12.2.jar" defpackage.ProcSim
```
