@echo off
cd "C:\Program Files\Java\jdk-11.0.3\bin\"
START java --module-path C:\GemJam\lib\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.swing,javafx.media -Dfile.encoding=windows-1252 -jar C:\GemJam\GemJam.jar
