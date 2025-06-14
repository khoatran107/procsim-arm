set -e
zsh -c "javac -cp xercesImpl-2.12.2.jar -d . defpackage/**/*.java" 
jar cvf myprogram.jar defpackage/
find . -name "*.class" -delete