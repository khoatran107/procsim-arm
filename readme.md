Build:
```
javac -cp xercesImpl-2.12.2.jar -d . defpackage/*.java
jar cvf myprogram.jar defpackage/
```

Run:
```
java -cp "myprogram.jar:xercesImpl-2.12.2.jar" defpackage.ProcSim
```

